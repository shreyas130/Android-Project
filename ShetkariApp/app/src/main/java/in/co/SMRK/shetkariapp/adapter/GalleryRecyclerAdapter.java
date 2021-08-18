package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.GalleryModel;

/**
 * Created by Jack on 8/24/2017.
 */

public class GalleryRecyclerAdapter extends ArrayAdapter<GalleryModel> {

    private ArrayList<GalleryModel> galleryList;
    private Context mContext;
    private LayoutInflater inflater;
    private GalleryModel model;


    public GalleryRecyclerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<GalleryModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.galleryList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View view = convertView;

        if (view == null) {

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gallery_thumbnail, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.thumbnail);
            model = galleryList.get(position);

            if (model !=null){

                Glide.with(mContext).load(galleryList.get(position).getGalleryImage())
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(imageView);

            }
        }


        return view;
    }
}
