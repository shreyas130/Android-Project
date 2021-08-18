package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import in.co.SMRK.shetkariapp.model.DistrictModel;

/**
 * Created by Jack on 8/28/2017.
 */

public class DistrictAdapter extends ArrayAdapter<DistrictModel> {

    private Context mContext;
    private ArrayList<DistrictModel> list;
    private static LayoutInflater inflater = null;
    private SharedPreferences mPreference;


    public DistrictAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DistrictModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.list = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View row;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.spinner_item,parent,false);

        TextView tv_newsTitle = (TextView) row.findViewById(R.id.title);

        DistrictModel districtModel = list.get(position);
        if (districtModel !=null) {

            int districtId = list.get(position).getDistrictId();
            tv_newsTitle.setText(list.get(position).getDistrictName());

            mPreference = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
            mPreference.edit().putInt("districtId",districtId).commit();
        }
        return row;
    }
}
