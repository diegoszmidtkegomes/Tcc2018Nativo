package ifrs.com.tcc2018.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ifrs.com.tcc2018.R;
import ifrs.com.tcc2018.adapter.ImagemAdapter;
import ifrs.com.tcc2018.model.Imagem;

/**
 * The type Imagem activity.
 */
public class ImagemActivity extends AppCompatActivity {

    /**
     * The Toolbar.
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * The M view pager.
     */
    @BindView(R.id.act_imagem_viewpager)
    ViewPager mViewPager;
    /**
     * The Activity imagem.
     */
    @BindView(R.id.activity_imagem)
    RelativeLayout activityImagem;
    private float scale;
    private int width;
    private int height;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_imagem);
        setToolbar("", false);
        toolbar = findViewById(R.id.toolbar);
        //Log.i("diego", "alpha: " + toolbar.getBackground().getAlpha());
        //toolbar.getBackground().setAlpha(0);
        ButterKnife.bind(this);

        int posicao = getIntent().getIntExtra(getString(R.string.param_posicaoselecionada), 0);

        Intent i = getIntent();
        List<Imagem> imagens = (List<Imagem>) i.getSerializableExtra(getString(R.string.param_imagens));

        //ViewPager mViewPager = (ViewPager) findViewById(R.id.act_imagem_viewpager);
        ImagemAdapter adapterView = new ImagemAdapter(this, imagens);
        mViewPager.setAdapter(adapterView);
        mViewPager.setCurrentItem(posicao);

    }

    public void setToolbar(String titulo, boolean backButton){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retorno;
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_OK);
                toolbar.getBackground().setAlpha(255);
                finish();
                retorno = true;
                break;
            default:
                retorno = super.onOptionsItemSelected(item);
                break;
        }
        return retorno;
    }

    @Override
    public void onBackPressed() {
        toolbar.getBackground().setAlpha(255);
        super.onBackPressed();
    }
}
