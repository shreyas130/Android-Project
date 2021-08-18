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

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.CropListModel;

/**
 * Created by Jack on 8/19/2017.
 */

public class CropSchedulerAdapter extends ArrayAdapter<CropListModel> {

    private Context mContext;
    private ArrayList<CropListModel> list;
    private static LayoutInflater inflater = null;

    public CropSchedulerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<CropListModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.schedule_gridview,parent,false);
        ImageView imageView = (ImageView) row.findViewById(R.id.img_sceduler);
        TextView tv_scheduleName = (TextView) row.findViewById(R.id.tv_scedulerTitle);

        CropListModel model = list.get(position);

        if (model !=null){

            tv_scheduleName.setText(list.get(position).getCropName());

            Glide.with(mContext)
                    .load(list.get(position).getThumnails())
                    .into(imageView);


        }

        return row;
    }
}
