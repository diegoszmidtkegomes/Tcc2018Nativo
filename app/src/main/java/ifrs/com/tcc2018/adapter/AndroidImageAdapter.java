package ifrs.com.tcc2018.adapter;

import android.support.v4.view.PagerAdapter;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AndroidImageAdapter extends PagerAdapter {

    private List<String> sliderImagesId;
    Context mContext;

    public AndroidImageAdapter(Context context, List<String> fotos) {
        this.mContext = context;
        this.sliderImagesId =fotos;
    }

    @Override
    public int getCount() {
        return sliderImagesId.size();
    }

    /* private int[] sliderImagesId = new int[]{
            R.drawable.image1, R.drawable.image2, R.drawable.cat,
            R.drawable.image1, R.drawable.image2, R.drawable.cat,
    };*/

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((ImageView) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //mImageView.setImageResource(sliderImagesId[i]);
        Picasso.get().load(sliderImagesId.get(i)).into(mImageView);
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);
    }
}
