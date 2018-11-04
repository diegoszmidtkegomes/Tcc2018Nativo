package ifrs.com.tcc2018.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import ifrs.com.tcc2018.model.Imagem;
import ifrs.com.tcc2018.view.ImagemActivity;
import ifrs.com.tcc2018.view.TouchImageView;


/**
 * Adapter para a lista de imagens
 *
 * @author diego.gomes
 * @version 1.0.0
 */
public class ImagemAdapter extends PagerAdapter {

    /**
     * Activity que está instanciando o Adapter
     */
    private ImagemActivity activity;
    /**
     * ista de imagens que serão exibidas
     */
    private List<Imagem> listaImagens;
    private int _width;
    private int _height;

    /**
     * Construtor da classe
     *
     * @param act          Activity que está instanciando o Adapter
     * @param listaImagens Lista de imagens que serão exibidas
     */
    public ImagemAdapter(ImagemActivity act, List<Imagem> listaImagens) {
        this.activity = act;
        this.listaImagens = listaImagens;
        float _scale = activity.getResources().getDisplayMetrics().density;
        this._width = activity.getResources().getDisplayMetrics().widthPixels - (int) (14 * _scale + 0.5f);
        this._height = (_width / 16) * 9;
    }

    /**
     * Construtor da classe
     */
    public ImagemAdapter() {
    }

    @Override
    public int getCount() {
        return listaImagens.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        TouchImageView mImageView = new TouchImageView(activity);
        mImageView.setMaxZoom(4f);
        mImageView.setBackgroundColor(Color.BLACK);

        Imagem img = listaImagens.get(i);
        Uri uri = Uri.parse(img.getCaminhoFoto());
        final File arquivo = new File(uri.toString());

        /*.thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)*/
        Glide.with(activity)
                .load(arquivo)
                .override((_width), (_height))
                .fitCenter()
                .into(mImageView);
        container.addView(mImageView, 0);

        mImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((ImageView) obj);
    }
}
