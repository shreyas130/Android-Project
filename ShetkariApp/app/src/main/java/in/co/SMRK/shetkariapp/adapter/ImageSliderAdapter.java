package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;

/**
 * Created by Jack on 8/14/2017.
 */

public class ImageSliderAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater = null;
    private Context mContext;

    public ImageSliderAdapter(Context context, ArrayList<String> images) {

        this.mContext = context;
        this.images=images;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_image, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.slide_image1);

        Picasso.with(mContext)
                .load(images.get(position))
                .into(myImage);

        view.addView(myImageLayout, 0);

        return myImageLayout;
    }
}
