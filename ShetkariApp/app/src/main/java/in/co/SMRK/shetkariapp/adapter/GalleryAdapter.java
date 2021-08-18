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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.GalleryModel;

/**
 * Created by Jack on 8/23/2017.
 */

public class GalleryAdapter extends ArrayAdapter<GalleryModel> {

    private Context mContext;
    private GalleryModel model;
    private ArrayList<GalleryModel> list;
    private static LayoutInflater inflater=null;

    public GalleryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<GalleryModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gridview_croplist, null);

            ImageView cropImage = (ImageView) view.findViewById(R.id.img_grid_CropImage);

            TextView tv_title = (TextView) view.findViewById(R.id.tv_grid_Croptitle);
            model = list.get(position);

            if (model !=null){

                Glide.with(mContext)
                        .load(list.get(position).getThumnails())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(cropImage);

                String name = list.get(position).getGalleryTitle();
                tv_title.setText(name);
            }
        }


        return view;
    }
}
