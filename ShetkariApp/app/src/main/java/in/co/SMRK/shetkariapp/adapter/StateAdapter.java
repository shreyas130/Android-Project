package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.StateModel;

/**
 * Created by Jack on 8/28/2017.
 */

public class StateAdapter extends ArrayAdapter<StateModel>{

    private Context mContext;
    private ArrayList<StateModel> list;
    private static LayoutInflater inflater = null;
    private SharedPreferences mPreference;


    public StateAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull  ArrayList<StateModel> objects) {
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

        StateModel stateModel = list.get(position);
        if (stateModel !=null) {

            int stateId = list.get(position).getStateId();
            tv_newsTitle.setText(list.get(position).getStateName());

            mPreference = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
            mPreference.edit().putInt("stateId",stateId).commit();
            Log.d("stateId",String.valueOf(stateId));
        }
        return row;

    }
}
