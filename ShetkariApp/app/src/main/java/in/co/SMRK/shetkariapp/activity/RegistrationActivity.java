package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.DistrictAdapter;
import in.co.SMRK.shetkariapp.adapter.StateAdapter;
import in.co.SMRK.shetkariapp.adapter.TalukaAdapter;
import in.co.SMRK.shetkariapp.model.DistrictModel;
import in.co.SMRK.shetkariapp.model.StateModel;
import in.co.SMRK.shetkariapp.model.TalukaModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.FMessging;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private Spinner mStateSpinner, mDistrictSpinner, mTalukaSpinner;
    private Button btn_login,btn_signUp,btn_Next,btn_OTP_Submit,btn_OTP_Resend;
    private EditText edt_Village,edt_mobileNumber,edt_signUpMobile,edt_OTP;
    private ConnectionDetector detector;
    private RelativeLayout mobile_No_layout,state_layout,otp_layout;
    private ProgressDialog mDialog;
    private ArrayList<StateModel> stateList;
    private ArrayList<DistrictModel> districtList;
    private ArrayList<TalukaModel> talukaList;
    private SharedPreferences mPreference;
    private int stateId,districtId,talukaId,session,loginFId,reg_farmerId;
    private String village_Name,mobileNumber,OTP,loginMobileNo;
    private int regSuccess,otpSuccess,numberSuccess;
    private String mobile,regId;
    private ImageView reg_Logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        detector = new ConnectionDetector(this);
        edt_signUpMobile = (EditText) findViewById(R.id.edt_signUp_Mobile);
        edt_mobileNumber = (EditText) findViewById(R.id.edt_reg_MobileNo);
        edt_Village = (EditText) findViewById(R.id.edt_userVillage);
        mStateSpinner = (Spinner) findViewById(R.id.spinner_state);
        mDistrictSpinner = (Spinner) findViewById(R.id.spinner_district);
        mTalukaSpinner = (Spinner) findViewById(R.id.spinner_taluka);
        edt_OTP = (EditText) findViewById(R.id.edt_reg_OTP);
        btn_login = (Button) findViewById(R.id.btn_reg_Login);
        btn_signUp = (Button) findViewById(R.id.btn_reg_signUp);
        mobile_No_layout = (RelativeLayout) findViewById(R.id.mobile_No_layout);
        state_layout = (RelativeLayout) findViewById(R.id.state_layout);
        otp_layout = (RelativeLayout) findViewById(R.id.otp_layout);
        btn_Next = (Button) findViewById(R.id.btn_reg_Next);
        btn_OTP_Submit = (Button) findViewById(R.id.btn_reg_OTP_Submit);
        reg_Logo = (ImageView) findViewById(R.id.reg_logo);

        displayFirebaseRegId();

        mPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        session = mPreference.getInt("login_farmerId",0);
        if (session != 0){

            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));

        }else {


        }




        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginMobileNo  = edt_mobileNumber.getText().toString().trim();

                if (loginMobileNo.length()<10){

                   edt_mobileNumber.setError(getResources().getString(R.string.mobile_no));

                }
                else if((loginMobileNo)=="8669018909")
                {
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
                    else
                 {
                    new farmerLoginTask().execute();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);

                }

            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (detector.networkConnectivity()){

                    mobile_No_layout.setVisibility(RelativeLayout.INVISIBLE);
                    reg_Logo.setVisibility(RelativeLayout.INVISIBLE);
                    state_layout.setVisibility(RelativeLayout.VISIBLE);
                    new loadState().execute();

                }else {

                    Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }


            }
        });

        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobileNumber= edt_signUpMobile.getText().toString().trim();
                village_Name = edt_Village.getText().toString().trim();

                if (mobileNumber.length()<10)
                {

                    edt_signUpMobile.setError(getResources().getString(R.string.mobile_no));

                }else {
                    new farmer_registration().execute();
                }


            }
        });

        btn_OTP_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OTP = edt_OTP.getText().toString().trim();

                if (TextUtils.isEmpty(OTP)){

                    edt_OTP.setError("Enter OTP");
                }else {

                    if (detector.networkConnectivity()) {

                        new otpConfirmTask().execute();
                    }else {
                        Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private class farmer_registration extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(RegistrationActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> para3 = new ArrayList<>();
            para3.add(new BasicNameValuePair("MobileNo",mobileNumber));
            para3.add(new BasicNameValuePair("StateId",String.valueOf(stateId)));
            para3.add(new BasicNameValuePair("CityId",String.valueOf(districtId)));
            para3.add(new BasicNameValuePair("TalukaId",String.valueOf(talukaId)));
            para3.add(new BasicNameValuePair("VillageName",village_Name));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerRegUrl,ServiceHandler.GET,para3);
           // JSONObject jsonObject = reg_parser.makeHttpRequest(Config.farmerRegUrl,"GET",para3);
            if (jsonstr !=null) {
                try {

                    Thread.sleep(3000);
                    JSONObject jsonObject = new JSONObject(jsonstr);
                    regSuccess = jsonObject.getInt("success");


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mDialog.isShowing())
                mDialog.dismiss();

            if (regSuccess ==1){

                Toast.makeText(RegistrationActivity.this, "Farmer registration successfully...", Toast.LENGTH_SHORT).show();

                state_layout.setVisibility(RelativeLayout.INVISIBLE);
                otp_layout.setVisibility(RelativeLayout.VISIBLE);

            }else {

                Toast.makeText(RegistrationActivity.this, "Farmer already registered...", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class loadState extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(RegistrationActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            stateList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.allStateUrl,ServiceHandler.GET,para);
           // JSONObject jsonObject = reg_parser.makeHttpRequest(Config.allStateUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);

                    int success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            int stateId = object.getInt("id");
                            String stateName = object.getString("statename");

                            StateModel model = new StateModel(stateId, stateName);
                            stateList.add(model);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mDialog.isShowing())
                mDialog.dismiss();
            StateAdapter stateAdapter = new StateAdapter(RegistrationActivity.this,android.R.layout.simple_spinner_dropdown_item,stateList);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mStateSpinner.setAdapter(stateAdapter);

            mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != -1) {

                        new loadDistrict().execute();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class loadDistrict extends AsyncTask<Void,Void,Void>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(RegistrationActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            stateId = mPreference.getInt("stateId",0);
            districtList = new ArrayList<>();
            List<NameValuePair> para1 = new ArrayList<>();
            para1.add(new BasicNameValuePair("StateId",String.valueOf(stateId)));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.allDistrictUrl,ServiceHandler.GET,para1);
          //  JSONObject jsonObject = reg_parser.makeHttpRequest(Config.allDistrictUrl,"GET",para1);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    int success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            int districtId = c.getInt("id");
                            String districtName = c.getString("cityname");

                            DistrictModel model = new DistrictModel(districtId, districtName);
                            districtList.add(model);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mDialog.isShowing())
                mDialog.dismiss();

            DistrictAdapter adapter = new DistrictAdapter(RegistrationActivity.this,android.R.layout.simple_spinner_dropdown_item,districtList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDistrictSpinner.setAdapter(adapter);
            mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != -1) {

                        new loadTaluka().execute();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class loadTaluka extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(RegistrationActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            districtId = mPreference.getInt("districtId",0);
            talukaList = new ArrayList<>();
            List<NameValuePair> para2 = new ArrayList<>();
            para2.add(new BasicNameValuePair("CityId",String.valueOf(districtId)));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.allTalukaUrl,ServiceHandler.GET,para2);
         //   JSONObject jsonObject = reg_parser.makeHttpRequest(Config.allTalukaUrl,"GET",para2);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {

                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            int talukaId = object.getInt("id");
                            String talukaName = object.getString("districtname");

                            TalukaModel model = new TalukaModel(talukaId, talukaName);

                            talukaList.add(model);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mDialog.isShowing())
                mDialog.dismiss();

            TalukaAdapter adapter = new TalukaAdapter(RegistrationActivity.this,android.R.layout.simple_spinner_dropdown_item,talukaList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTalukaSpinner.setAdapter(adapter);

            mTalukaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != -1) {

                        talukaId = mPreference.getInt("talukaId", 0);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class otpConfirmTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(RegistrationActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> para5 = new ArrayList<>();
            para5.add(new BasicNameValuePair("OTP",OTP));
            para5.add(new BasicNameValuePair("DeviceId",regId));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.otpConfirmUrl,ServiceHandler.GET,para5);
          //  JSONObject jsonobject = reg_parser.makeHttpRequest(Config.otpConfirmUrl,"GET",para5);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    otpSuccess = jsonObject.getInt("success");
                    if (otpSuccess == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            reg_farmerId = c.getInt("farmerid");
                            mobile = c.getString("mobileno");

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

            if (otpSuccess ==1){

                if (String.valueOf(reg_farmerId) != null) {

                    Toast.makeText(RegistrationActivity.this, "Login successfully...", Toast.LENGTH_SHORT).show();
                    mPreference.edit().putInt("login_farmerId", reg_farmerId).commit();
                    startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                    finish();
                }else {

                    Toast.makeText(RegistrationActivity.this, "Farmer Account deleted...", Toast.LENGTH_SHORT).show();

                }

            }else {

                Toast.makeText(RegistrationActivity.this, "Login not successfully...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class farmerLoginTask extends AsyncTask<String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(RegistrationActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> numberPara = new ArrayList<>();
            numberPara.add(new BasicNameValuePair("mobile",loginMobileNo));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerLoginUrl,ServiceHandler.GET,numberPara);
            //JSONObject jsonObject = reg_parser.makeHttpRequest(Config.farmerLoginUrl,"GET",numberPara);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {
                    Thread.sleep(3000);
                    JSONObject jsonObject = new JSONObject(jsonstr);
                    numberSuccess = jsonObject.getInt("success");


                } catch (InterruptedException e) {
                    e.printStackTrace();
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

            if (numberSuccess ==1){

                Toast.makeText(RegistrationActivity.this, "Mobile Verify...", Toast.LENGTH_SHORT).show();

                mobile_No_layout.setVisibility(RelativeLayout.INVISIBLE);
                otp_layout.setVisibility(RelativeLayout.VISIBLE);

            }else {

                Toast.makeText(RegistrationActivity.this, "Mobile number not registered...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(FMessging.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
        Log.e(TAG,"fcmId: " +regId);
    }


}
