
package ifrs.com.tcc2018.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.parceler.Parcels;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.adapter.CapturarImagemAdapter;
import ifrs.com.tcc2018.model.Imagem;

/**
 * activity para Capturar imagem .
 */
public class CapturarImagemActivity extends AppCompatActivity {

    /**
     * CODIGO_CAMERA para validação .
     */
    public static final int CODIGO_CAMERA = 567;
    /**
     * Lista de imagens.
     */
    public List<Imagem> listaImagens = new ArrayList<>();
    /**
     * Toolbar.
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * Grid imagens.
     */
    @BindView(R.id.lista_imagem)
    GridView gridImagens;
    /**
     * Botão de  Nova imagem.
     */
    @BindView(R.id.nova_image)
    FloatingActionButton novaImagem;

    private String caminhoFoto;
    private CapturarImagemAdapter adapter;

    private int id;
    private List<Integer> listaIdImagem;
    private boolean registroEspecifico;

    private File arquivoFoto;
    private boolean modificar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_capturar_imagem);
        ButterKnife.bind(this);
        setToolbar("Imagens", false);
        registerForContextMenu(gridImagens);

        carregaParametrosIntent();

        if (savedInstanceState != null) {
            listaImagens = (List<Imagem>) savedInstanceState.getSerializable(getString(R.string.param_imagens));
            adapter = new CapturarImagemAdapter(listaImagens, this);
            caminhoFoto = savedInstanceState.getString(getString(R.string.param_caminhofot));

            gridImagens.setAdapter(adapter);
        } else {
            assert listaIdImagem != null;

            if (listaIdImagem != null && listaIdImagem.size() > 0) {
                Integer[] ids = new Integer[listaIdImagem.size()];
                //buscar imagens pelo id
                //listaImagens = new ImagemRepositorio().buscaImagensPeloId(listaIdImagem.toArray(ids));
                adapter = new CapturarImagemAdapter(listaImagens, this);
                gridImagens.setAdapter(adapter);
            }
        }
    }

    public void setToolbar(String titulo, boolean backButton){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void carregaParametrosIntent() {
        try {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            id = bundle.getInt(getString(R.string.param_id));
            //Log.i("diego", "id questao recebido: " + id);

            listaIdImagem = (List<Integer>) bundle.getSerializable(getString(R.string.param_imagens));
            //Log.i("diego", "ids imagens: " + listaIdImagem.toString());

            modificar = bundle.getBoolean(getString(R.string.param_modify));

            try {
                registroEspecifico = bundle.getBoolean(getString(R.string.param_reg_esp));
            } catch (Exception ex) {
                registroEspecifico = false;
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA) {
                carregaImagem(caminhoFoto);
            }
        }
    }

    /**
     * Carrega imagem.
     *
     * @param caminhoFoto the caminho foto
     */
    public void carregaImagem(String caminhoFoto) {
        String base64 = "";
        if (caminhoFoto != null) {
            listaImagens.add(new Imagem(0, caminhoFoto, arquivoFoto.getName(), base64));
            adapter = new CapturarImagemAdapter(listaImagens, CapturarImagemActivity.this);
            gridImagens.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_activity_ok:
                if (modificar) {
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt(getString(R.string.param_id), id);
                    bundle.putBoolean(getString(R.string.param_reg_esp), registroEspecifico);
                    bundle.putInt(getString(R.string.param_posicaoscroll), id);
                    bundle.putParcelable(getString(R.string.param_imagens), Parcels.wrap(listaImagens));
                    data.putExtras(bundle);
                    if (getParent() == null)
                        setResult(Activity.RESULT_OK, data);
                    else
                        getParent().setResult(Activity.RESULT_OK, data);
                }
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actv_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("imagens", (Serializable) listaImagens);
        outState.putString("caminhofoto", caminhoFoto);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (modificar)
            menu.add("Deletar");
    }

    /**
     * On click nova imagem.
     *
     * @param v the v
     */
    @OnClick(R.id.nova_image)
    public void onClickNovaImagem(View v) {
        if (modificar) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".JPG";
                arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            } else {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".JPG";
                arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getTitle().toString()) {
            case "Deletar":
                Log.i("id", String.valueOf(info.position));
                listaImagens.remove(info.position);
                adapter = new CapturarImagemAdapter(listaImagens, this);
                gridImagens.setAdapter(adapter);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

