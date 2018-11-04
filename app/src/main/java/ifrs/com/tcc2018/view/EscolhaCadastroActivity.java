package ifrs.com.tcc2018.view;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.model.Carro;
import ifrs.com.tcc2018.model.CarroWS;
import ifrs.com.tcc2018.model.iAsyncResponseObj;
import ifrs.com.tcc2018.repositorio.CadastroRepositorio;
import ifrs.com.tcc2018.repositorio.CarroRepositorio;
import ifrs.com.tcc2018.util.Localizacao;
import ifrs.com.tcc2018.util.Permissao;
import ifrs.com.tcc2018.util.PushNotif;

public class EscolhaCadastroActivity extends AppCompatActivity implements iAsyncResponseObj {

    private static final int RESULT_IMAGENS = 9010;
    private Carro carro;
    private Toolbar toolbar;
    private FloatingActionButton _btnAdd;
    private EscolhaCadastroActivity act;
    private EditText _campoNome;
    private EditText _campoEnd;
    private EditText _campoEmail;
    private EditText _campoData;
    private TextView _preco;
    private TextView _modelo;
    private String nome;
    private String end;
    private String data;
    private String email;
    private CarroWS carroWs;
    private long startTime;
    private Button _btnImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_cadastro);
        buscaParametrosIntent();

        setToolbar();
        act = this;
        validaPermissoes(this);
        _btnAdd = (FloatingActionButton) findViewById(R.id.escolha_cadastro_bt_add);
        _btnImg = (Button) findViewById(R.id.escolha_cadastro_btfotos);
        _preco = (TextView) findViewById(R.id.escolha_cadastro_tvpreco);
        _modelo = (TextView) findViewById(R.id.escolha_cadastro_tvmodelo);


        _btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent capturarImagem = new Intent(act, CapturarImagemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(act.getString(R.string.param_id), 0);
                bundle.putBoolean(act.getString(R.string.param_modify), true);
                List<Integer> listaImagem = new ArrayList<>();
                /*for (Imagem im : inventarioLido.getListaImagens()) {
                    listaImagem.add((int) im.getId());
                }*/
                bundle.putSerializable(act.getString(R.string.param_imagens),
                        (Serializable) listaImagem);
                capturarImagem.putExtras(bundle);
                if (act instanceof Activity)
                    act.startActivityForResult(capturarImagem, RESULT_IMAGENS);
            }
        });

        _btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                _campoNome = (EditText) act.findViewById(R.id.escolha_cadastro_ednome);
                _campoEnd = (EditText) act.findViewById(R.id.escolha_cadastro_edend);
                _campoEmail = (EditText) act.findViewById(R.id.escolha_cadastro_edemail);
                _campoData = (EditText) act.findViewById(R.id.escolha_cadastro_ededata);

                nome = _campoNome.getText().toString();
                end = _campoEnd.getText().toString();
                email = _campoEmail.getText().toString();
                data = _campoData.getText().toString();

                if(validaInformacoes()){
                    //new Localizacao().realizaConexao(act, (Activity) act, act);
                    startTime = System.currentTimeMillis();
                    new Localizacao().buscaLocalizacao(act, EscolhaCadastroActivity.this);
                    new CadastroRepositorio().inserir(nome, end, email, data,
                            carro != null  ? carro.getNome() :  carroWs.getNome(),
                            carro != null  ? carro.getPreco() : carroWs.getPreco());
                }

                /*Intent intent = new Intent(EscolhaCadastroActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);*/
            }
        });

        _preco.setText(String.valueOf(carro != null ? carro.getPreco() : carroWs.getPreco()));
        _modelo.setText(carro!=null ? carro.getNome() : carroWs.getNome());

    }

    private boolean validaInformacoes() {
        boolean valido = true;

        if (nome.isEmpty() ) {
            _campoNome.setError("insira o nome");
            valido = false;
        } else {
            _campoNome.setError(null);
        }

        if (end.isEmpty() ) {
            _campoEnd.setError("insira o endereço");
            valido = false;
        } else {
            _campoEnd.setError(null);
        }

        if (email.isEmpty() ) {
            _campoEmail.setError("insira o email");
            valido = false;
        } else {
            _campoEmail.setError(null);
        }

        if (data.isEmpty() ) {
            _campoData.setError("insira a data");
            valido = false;
        } else {
            _campoData.setError(null);
        }

        return valido;    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void buscaParametrosIntent() {
        Intent intent = this.getIntent();
        int idCarro= (int) intent.getSerializableExtra("id");

        if (idCarro ==0){
            carroWs = (CarroWS) getIntent().getSerializableExtra("carroWs");
        }
        else{
            carro = new CarroRepositorio().selectById(idCarro);
        }
    }

    public void validaPermissoes(Activity activity) {
        new Permissao().permissoes(activity);
    }

    @Override
    public void processoEncerrado(Object obj) {
        long estimatedTime = (System.currentTimeMillis() - startTime);
        Log.v("diego", "ms- " + String.valueOf(estimatedTime));
        double lat = ((Location) obj).getLatitude();
        double longit = ((Location) obj).getLongitude();
        String texto = "Latitude: " + lat + " Long : " + longit;
        PushNotif.exibir(act, "Geolocal", texto);
    }
}
