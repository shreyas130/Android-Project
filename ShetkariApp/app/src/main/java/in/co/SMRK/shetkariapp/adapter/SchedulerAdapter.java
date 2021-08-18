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
import in.co.SMRK.shetkariapp.model.CropSchdulerModel;

/**
 * Created by KK on 20/08/2017.
 */

public class SchedulerAdapter extends ArrayAdapter<CropSchdulerModel> {

    private ArrayList<CropSchdulerModel> data;
    private CropSchdulerModel model;
    private Context mContext;
    private LayoutInflater inflater = null;
    private SharedPreferences mPreference;

    public SchedulerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<CropSchdulerModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.data = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.croplist_spinner,parent,false);

        TextView title = (TextView) row.findViewById(R.id.crop_title);

        CropSchdulerModel model = data.get(position);

        if (model !=null){

            title.setText(data.get(position).getSchedulerType());
            int id = data.get(position).getSchedulerId();

            mPreference = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
            mPreference.edit().putInt("scheduleId",id).commit();
        }
        return row;
    }
}
