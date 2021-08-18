package in.co.SMRK.shetkariapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.activity.Schedule_TypeActivity;
import in.co.SMRK.shetkariapp.adapter.CropSchedulerAdapter;

import in.co.SMRK.shetkariapp.model.CropListModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class CropSchduleFragment extends Fragment {

    private GridView mCropList;
    private CropSchedulerAdapter mAdapter;
    private ProgressDialog mDialog;
    private ArrayList<CropListModel> cropList;
    private JSONParser jsonParser;
    private JSONArray jsonArray;
    private int cropId,checkSuccess,farmerId,success;
    private String cropName;
    private CropListModel cropListModel;
    private ConnectionDetector detector;
    private RelativeLayout paid_layout,unpaid_layout;
    private SharedPreferences mPreference;


    public CropSchduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_crop_schdule, container, false);

        mCropList = (GridView) view.findViewById(R.id.gridview_cropList);
        paid_layout = (RelativeLayout) view.findViewById(R.id.layout_paid);
        unpaid_layout = (RelativeLayout) view.findViewById(R.id.layout_unpaid);
        mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        jsonParser = new JSONParser();

        detector = new ConnectionDetector(getActivity());

        if (detector.networkConnectivity()){

                new paidUnpaidTask().execute();
        }else {

            Toast.makeText(getActivity(), "no internet connection...", Toast.LENGTH_SHORT).show();
        }

        mCropList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CropListModel model = (CropListModel) parent.getItemAtPosition(position);
                int cropId = model.getCropId();
                Intent intent = new Intent(getActivity(),Schedule_TypeActivity.class);
                intent.putExtra("cropId",cropId);
                startActivity(intent);
            }
        });

        return view;


    }

    private class paidUnpaidTask extends AsyncTask<String,Void,String>{


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
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));
            JSONObject jsonObject = jsonParser.makeHttpRequest(Config.paidOrunpaidUrl,"GET",para);

            try {
                Thread.sleep(3000);

                checkSuccess = jsonObject.getInt("success");

                if (checkSuccess ==1){


                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (mDialog.isShowing())
                mDialog.dismiss();

            if (checkSuccess ==1){

                paid_layout.setVisibility(RelativeLayout.VISIBLE);
                unpaid_layout.setVisibility(RelativeLayout.INVISIBLE);
                new CropListTask().execute();


            }else {

                paid_layout.setVisibility(RelativeLayout.INVISIBLE);
                unpaid_layout.setVisibility(RelativeLayout.VISIBLE);
            }
        }
    }

    private class CropListTask extends AsyncTask<String,Void,String>{

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

            cropList = new ArrayList<>();

            List<NameValuePair> para = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(Config.cropListUrl,"GET",para);

            try{

                success = jsonObject.getInt("success");

                if (success ==1) {

                    jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject c = jsonArray.getJSONObject(i);

                        cropId = c.getInt("cropid");
                        String a = String.valueOf(cropId);
                        cropName = c.getString("cropname");
                        String flag = c.getString("imagepath");

                        Log.e("cropId", a);
                        Log.e("cropName", cropName);

                        cropListModel = new CropListModel(cropId, cropName,flag);
                        cropList.add(cropListModel);
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

            if (success ==1){

                CropSchedulerAdapter adapter = new CropSchedulerAdapter(getActivity(),android.R.layout.simple_list_item_1,cropList);
                mCropList.setAdapter(adapter);

            }else {

                Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }

        }
    }

  /*  @Override
    public void onPause() {
        super.onPause();

        if ((mDialog != null) && mDialog.isShowing())
            mDialog.dismiss();
        mDialog = null;
    }*/



}
