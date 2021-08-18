package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.CropSchdulerModel;

/**
 * Created by Jack on 8/16/2017.
 */

public class ScheduleTypeAdapter extends ArrayAdapter<CropSchdulerModel> {

    private Context mContext;
    private ArrayList<CropSchdulerModel> data;
    private static LayoutInflater inflater = null;
    private CropSchdulerModel cropListModel;
    private int id;
    private String name;




    public ScheduleTypeAdapter(Context context, @LayoutRes int resource, ArrayList<CropSchdulerModel> data){

        super(context,resource,data);
        this.mContext = context;
        this.data = data;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView ==null){

            view = inflater.inflate(R.layout.croplist_listview,parent,false);

            TextView tv_CropTitle = (TextView) view.findViewById(R.id.crop_title);

            cropListModel = data.get(position);

            if (cropListModel !=null){

                tv_CropTitle.setText(data.get(position).getTypeName());
                /*name = data.get(position).getSchedulerType();
                tv_CropTitle.setText(name);*/


            }
        }
        return view;
    }
}
