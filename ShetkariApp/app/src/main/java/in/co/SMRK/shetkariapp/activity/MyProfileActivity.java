package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.FarmerDetailsModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class MyProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button btn_updateProfile;
    private ProgressDialog mDialog;
    private ArrayList<FarmerDetailsModel> list;
    private int farmerId,login_farmerId,signUp_farmerId,success;
    private JSONParser farmer_parser;
    private FarmerDetailsModel model;
    private TextView tv_username,tv_mobileNumber,tv_villageName;
    private SharedPreferences mPreference;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        farmer_parser = new JSONParser();

        btn_updateProfile = (Button) findViewById(R.id.btn_updateUserProfile);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_mobileNumber = (TextView) findViewById(R.id.tv_userMobileNo);
        tv_villageName = (TextView) findViewById(R.id.tv_userVillage);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);


        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        detector = new ConnectionDetector(this);

        if (detector.networkConnectivity()){

            new loadProfileTask().execute();

        }else {

            Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }


        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyProfileActivity.this,MyProfileDetailsActivity.class);
                intent.putExtra("fId",farmerId);
                startActivity(intent);
            }
        });

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyProfileActivity.this,MainActivity.class));

            }
        });


    }

    private class loadProfileTask extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MyProfileActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            farmerId = mPreference.getInt("login_farmerId",0);

            list = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerDetailsUrl,ServiceHandler.GET,para);
          //  JSONObject jsonObject = farmer_parser.makeHttpRequest(Config.farmerDetailsUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonstr);

                    success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);
                            String firstname = c.getString("firstname");
                            String lastName = c.getString("lastname");
                            String mobileNo = c.getString("mobileno");
                            String villageName = c.getString("villagename");

                            model = new FarmerDetailsModel(firstname, lastName, mobileNo, villageName);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            mPreference.edit().putString("village",village).commit();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyProfileActivity.this,MainActivity.class));

    }
}
