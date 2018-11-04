package ifrs.com.tcc2018.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.adapter.AndroidImageAdapter;
import ifrs.com.tcc2018.model.Carro;
import ifrs.com.tcc2018.model.CarroWS;
import ifrs.com.tcc2018.repositorio.CarroRepositorio;

public class EscolhaActivity extends AppCompatActivity {

    private Carro carro;
    private CarroWS carroWs;
    private Toolbar toolbar;
    private FloatingActionButton _btnAdd;
    private int idCarro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha);
        buscaParams();
        buscaCarro();
        setToolbar();

        //slider fotos
        sliderFotos();

        //list veiculo
        listVeiculo();

        _btnAdd = (FloatingActionButton) findViewById(R.id.escolha_bt_add);

        _btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent escolha = new Intent(
                        EscolhaActivity.this,
                        EscolhaCadastroActivity.class);
                escolha.putExtra("carroWs", carroWs);
                escolha.putExtra("id", idCarro);
                startActivity(escolha);
            }
        });

    }

    private void buscaCarro() {
        carro = new CarroRepositorio().selectById(idCarro);
    }

    private void buscaParams() {
        Intent intent = this.getIntent();
        idCarro= (int) intent.getSerializableExtra("id");

        if (idCarro ==0){
            carroWs = (CarroWS) getIntent().getSerializableExtra("carroWs");
        }
    }

    private void listVeiculo() {
        final ListView lv = (ListView) findViewById(R.id.escolha_listview_veiculo);

        String[] itens = new String[] {
                carro != null ? carro.getNome() : carroWs.getNome(), ("R$ " + (carro != null ? carro.getPreco() : carroWs.getPreco()))
        };

        final List<String> itemList = new ArrayList<String>(Arrays.asList(itens));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, itemList);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header,lv,false);
        lv.addHeaderView(header);
        lv.setAdapter(arrayAdapter);
    }

    private void sliderFotos() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this, carro != null ? carro.getFotos() : carroWs.getFotos());
        mViewPager.setAdapter(adapterView);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Escolha");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
