package in.co.SMRK.shetkariapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.activity.MyProfileDetailsActivity;
import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.FarmerDetailsModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    private Button btn_updateProfile;
    private ProgressDialog mDialog;
    private ArrayList<FarmerDetailsModel> list;
    private int farmerId,login_farmerId,signUp_farmerId,success;
    private JSONParser farmer_parser;
    private FarmerDetailsModel model;
    private TextView tv_username,tv_mobileNumber,tv_villageName;
    private SharedPreferences mPreference;
    private ConnectionDetector detector;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        farmer_parser = new JSONParser();

        btn_updateProfile = (Button) view.findViewById(R.id.btn_updateUserProfile);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_mobileNumber = (TextView) view.findViewById(R.id.tv_userMobileNo);
        tv_villageName = (TextView) view.findViewById(R.id.tv_userVillage);

        mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        detector = new ConnectionDetector(getActivity());

        if (detector.networkConnectivity()){

            new loadProfileTask().execute();

        }else {

            Toast.makeText(getActivity(), "no internet connection...", Toast.LENGTH_SHORT).show();
        }


        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),MyProfileDetailsActivity.class);
                intent.putExtra("fId",farmerId);
                startActivity(intent);
            }
        });

        return view;
    }

    private class loadProfileTask extends AsyncTask<String,Void,String>{


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

            list = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));
            JSONObject jsonObject = farmer_parser.makeHttpRequest(Config.farmerDetailsUrl,"GET",para);
            JSONArray jsonArray = null;

            try {
                Thread.sleep(2000);

                success = jsonObject.getInt("success");

                if (success ==1){

                    jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject c = jsonArray.getJSONObject(i);
                        String firstname = c.getString("firstname");
                        String lastName = c.getString("lastname");
                        String mobileNo = c.getString("mobileno");
                        String villageName = c.getString("villagename");

                        model = new FarmerDetailsModel(firstname,lastName,mobileNo,villageName);

                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
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

            String first = model.getFirstName();
            String last = model.getLastName();
            String mobile = model.getMobileNo();
            String village = model.getVillageName();
            tv_username.setText(first + "\t" +last);
            tv_mobileNumber.setText(mobile);
            tv_villageName.setText(village);

            mPreference.edit().putString("first",first).commit();
            mPreference.edit().putString("last",last).commit();
            mPreference.edit().putString("mobileNO",mobile).commit();

        }
    }

    /*@Override
    public void onPause() {
        super.onPause();

        if ((mDialog != null) && mDialog.isShowing())
            mDialog.dismiss();
        mDialog = null;
    }*/

}
