package in.co.SMRK.shetkariapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.activity.SelectCropRateActivity;
import in.co.SMRK.shetkariapp.activity.ViewRateActivity;
import in.co.SMRK.shetkariapp.model.CropListModel;
import in.co.SMRK.shetkariapp.model.CropModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class BhavFragment extends Fragment {

    private GridView mAllCrop;
    private ProgressDialog mDialog;
    private ArrayList<CropModel> cropList;
    private CropListModel cropListModel;
    private JSONParser jsonParser;
    private int cropId,farmerId,addSuccess;
    private String cropName;
    private ConnectionDetector detector;
    private TextView tv_AddCrop,tv_back;
    private SharedPreferences mPreference;

    public BhavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bhav, container, false);

        mAllCrop = (GridView) view.findViewById(R.id.grid_bhav_cropList);
        tv_AddCrop = (TextView) view.findViewById(R.id.tv_AddCrop);

        jsonParser = new JSONParser();

        mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        detector = new ConnectionDetector(getActivity());
        if (detector.networkConnectivity()){

            new cropListTask().execute();

        }else {

            Toast.makeText(getActivity(), "no internet connection...", Toast.LENGTH_SHORT).show();
        }

        tv_AddCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SelectCropRateActivity.class));
            }
        });


        return view;

    }

    private class cropListTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            farmerId = mPreference.getInt("login_farmerId",0);
            cropList = new ArrayList<>();
            List<NameValuePair> sList = new ArrayList<>();
            sList.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));
            JSONObject jsonObject = jsonParser.makeHttpRequest(Config.farmerSelectedCropUrl,"GET",sList);
            JSONArray jsonArray;

            try {

                int success = jsonObject.getInt("success");

                if (success ==1){

                    jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length();i++ ){

                        JSONObject c = jsonArray.getJSONObject(i);

                        int cId = c.getInt("cropid");
                        int farId = c.getInt("farmerid");
                        String cropName = c.getString("cropname");
                        String thumbanails = c.getString("imagepath");

                        CropModel model = new CropModel(farId,cId,cropName,thumbanails);
                        cropList.add(model);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (mDialog.isShowing())
                mDialog.dismiss();

            mAllCrop.setAdapter(new CropRemoveAdapter(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,cropList));
            mAllCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CropModel model = (CropModel)parent.getItemAtPosition(position);
                    int cId = model.getCropId();

                    Intent intent = new Intent(getActivity(), ViewRateActivity.class);
                    intent.putExtra("Cid",cId);
                    startActivity(intent);
                }
            });
        }
    }

    public class CropRemoveAdapter extends ArrayAdapter<CropModel> {

        private Context context;
        private ArrayList<CropModel> list;
        private LayoutInflater inflater=null;
        private CropModel model;

        public CropRemoveAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull  ArrayList<CropModel> objects) {
            super(context, resource, objects);

            this.context = context;
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.grid_removecrop,parent,false);

                ImageView cropImage = (ImageView) view.findViewById(R.id.img_rate_CropImage);
                TextView title = (TextView) view.findViewById(R.id.tv_rate_Croptitle);
                ImageView removeImage = (ImageView) view.findViewById(R.id.img_removeCrop);

                model = list.get(position);

                if (model !=null){

                    Glide.with(context)
                            .load(list.get(position).getFlag())
                            .into(cropImage);
                    title.setText(list.get(position).getCropName());

                    removeImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cropId = list.get(position).getCropId();

                            new cropAddTask().execute();
                        }
                    });

                }

            }

            return view;
        }
    }

    private class cropAddTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> para1 = new ArrayList<>();
            para1.add(new BasicNameValuePair("CropId",String.valueOf(cropId)));
            para1.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));
            para1.add(new BasicNameValuePair("Type","D"));
            JSONObject jsonObject = jsonParser.makeHttpRequest(Config.farmerAddorDeleteCropUrl,"GET",para1);


            try {

                addSuccess = jsonObject.getInt("success");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (mDialog.isShowing())
                mDialog.dismiss();

            if (addSuccess ==1){

                Toast.makeText(getActivity(), "Crop remove...", Toast.LENGTH_SHORT).show();

            }else {

                Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
    }

   /* @Override
    public void onPause() {
        super.onPause();

        if ((mDialog != null) && mDialog.isShowing())
            mDialog.dismiss();
        mDialog = null;
    }*/

}
