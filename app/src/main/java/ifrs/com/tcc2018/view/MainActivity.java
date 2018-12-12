package ifrs.com.tcc2018.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ifrs.com.tcc2018.model.Carro;
import ifrs.com.tcc2018.adapter.CarrosAdapter;
import ifrs.com.tcc2018.model.CarroWS;
import ifrs.com.tcc2018.repositorio.CarroRepositorio;
import ifrs.com.tcc2018.service.IntegracaoRest;
import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.model.iAsyncResponseObj;
import ifrs.com.tcc2018.service.RastreamentoService;
import ifrs.com.tcc2018.util.Permissao;

public class MainActivity extends AppCompatActivity implements iAsyncResponseObj {

    private static final String TAG = "MyService";
    private List<Carro> carroList;
    private List<CarroWS> carroListOnline;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private Toolbar toolbar;
    private ProgressDialog progress;
    private int posicaoSelecionada;
    private long startTime;
    private boolean buscarTodos;
    private int JOB_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        buscarTodos = false;
        buscarListaCarros();
        verificaService();
    }

    private void verificaService() {

        final JobScheduler jobScheduler = (JobScheduler) getSystemService(
                Context.JOB_SCHEDULER_SERVICE);

        // The JobService that we want to run
        final ComponentName name = new ComponentName(this, RastreamentoService.class);

        // Schedule the job
        final int result = jobScheduler.schedule(getJobInfo(123, 1, name));

        // If successfully scheduled, log this thing
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Scheduled job successfully!");
        }

    }


    private JobInfo getJobInfo(final int id, final long hour, final ComponentName name) {
        final long interval = TimeUnit.SECONDS.toMillis(hour); // run every hour
        final boolean isPersistent = true; // persist through boot
        final int networkType = JobInfo.NETWORK_TYPE_ANY; // Requires some sort of connectivity

        final JobInfo jobInfo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }

        return jobInfo;
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tcc2018");
        setSupportActionBar(toolbar);
    }

    public void buscarListaCarros(){
        startTime = System.currentTimeMillis();
        inicializaProgress();
        new CarroRepositorio().deleteAll();
        if(buscarTodos){
            Log.e("diego", "buscar 1000 itens offline");
            new IntegracaoRest().buscaCarrosWS1000Itens(this);
        }
        else{
            new IntegracaoRest().buscaCarrosWS100Itens(this);
        }
    }

    private void inicializaProgress() {
        progress = ProgressDialog.show(this, "Informações",
                "Buscando informações..", true);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
    }

    @Override
    public void processoEncerrado(Object obj) {
        if (obj instanceof ArrayList) {
            List<CarroWS> carroLista= (List<CarroWS>) obj;

            if(buscarTodos){
                salvaCarros(carroLista);
            }
            else{
                carroListOnline = carroLista;
            }
            carregaGrid();
            finalizaProgress();
            long estimatedTime = (System.currentTimeMillis() - startTime);
            Log.v("diego", String.valueOf(estimatedTime));
        }
    }

    private void salvaCarros(List<CarroWS> carroLista) {
        new CarroRepositorio().inserir(carroLista);
        carroList = new CarroRepositorio().buscaCarros();
    }

    private void finalizaProgress() {
        if (progress != null && progress.isShowing()) {
            try {
                progress.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_offline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_offline) {
            buscaOffline();
        }
        else if(id == R.id.action_online){
            buscaOnline();
        }
        return super.onOptionsItemSelected(item);
    }

    private void buscaOnline() {
        Log.e("diego", "busca online");
        buscarTodos = false;
        startTime = System.currentTimeMillis();
        buscarListaCarros();
    }

    private void buscaOffline() {
        Log.e("diego", "busca offline");
        buscarTodos = true;
        startTime = System.currentTimeMillis();
        buscarListaCarros();
    }

    private void carregaGrid() {

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.view_carros_swipe);
        mRecyclerView = (RecyclerView) findViewById(R.id.view_carros_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        mLayoutManager.scrollToPosition(0);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        posicaoSelecionada = position;

                        try{
                            if((carroList ==null || carroList.size() == 0)  && (carroListOnline ==null ||
                                    carroListOnline.size() == 0) ){
                                buscarListaCarros();
                            }
                            else{
                                Carro carro;
                                CarroWS carroWs = null;
                                int id;
                                if(carroList!=null){
                                    carro = carroList.get(posicaoSelecionada);
                                    id = carro.getId();
                                }
                                else{
                                    carroWs = carroListOnline.get(posicaoSelecionada);
                                    id = 0;
                                }
                                try {
                                    Intent escolha = new Intent(
                                            MainActivity.this,
                                            EscolhaActivity.class);
                                    //escolha.putExtra("Carro", carro);
                                    //Bundle bundle = new Bundle();
                                    //bundle.putSerializable(getString(R.string.idCarro), id);
                                    escolha.putExtra("carroWs", carroWs);
                                    escolha.putExtra("id", id);
                                    startActivity(escolha);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                            restartAct();
                        }

                    }
                })
        );

        mAdapter = new CarrosAdapter((ArrayList<Carro>) carroList, (ArrayList<CarroWS>)carroListOnline,  mRecyclerView, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void restartAct() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
