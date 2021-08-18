package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.CropSchdulerModel;

/**
 * Created by Jack on 8/21/2017.
 */

public class AllSchedulerAdapter extends ArrayAdapter<CropSchdulerModel> {
    private Context mContext;
    private ArrayList<CropSchdulerModel> list;
    private static LayoutInflater inflater = null;

    public AllSchedulerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<CropSchdulerModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.list = objects;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.croplist_listview,parent,false);
        TextView tv_scheduleName = (TextView) row.findViewById(R.id.crop_title);

        CropSchdulerModel model = list.get(position);

        if (model !=null){

            tv_scheduleName.setText(list.get(position).getSchedulerTitle());
        }
        return row;
    }
}
