package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.DistrictAdapter;
import in.co.SMRK.shetkariapp.adapter.StateAdapter;
import in.co.SMRK.shetkariapp.adapter.TalukaAdapter;
import in.co.SMRK.shetkariapp.model.DistrictModel;
import in.co.SMRK.shetkariapp.model.StateModel;
import in.co.SMRK.shetkariapp.model.TalukaModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class MyProfileDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout first,last,village;
    private EditText edt_FirstName,edt_lastName,edt_village;
    private Spinner stateSpinner,districtSpinner,talukaSpinner;
    private Button btnUpdateProfile;
    private JSONParser myProf_parser;
    private ProgressDialog mDialog;
    private ArrayList<StateModel> stateList;
    private ArrayList<DistrictModel> districtList;
    private ArrayList<TalukaModel> talukaList;
    private int stateId,districtId,talukaId,updateSuccess,farmerId;
    private SharedPreferences mPreference;
    private String firstName,lastName,village_Name;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);


        farmerId = getIntent().getExtras().getInt("fId");

        myProf_parser = new JSONParser();

        stateSpinner = (MaterialSpinner) findViewById(R.id.profile_state_spinner);
        districtSpinner = (MaterialSpinner) findViewById(R.id.profile_district_spinner);
        talukaSpinner = (MaterialSpinner) findViewById(R.id.profile_taluka_spinner);
        first = (TextInputLayout) findViewById(R.id.input_userName);
        last = (TextInputLayout) findViewById(R.id.input_userLastName);
        village = (TextInputLayout) findViewById(R.id.input_userVillage);
        edt_FirstName = (EditText) findViewById(R.id.edt_userName);
        edt_lastName = (EditText) findViewById(R.id.edt_userLastName);
        edt_village = (EditText) findViewById(R.id.edt_user_MYPRof_Village);
        edt_FirstName.addTextChangedListener(new MyTextWatcher(edt_FirstName));
        btnUpdateProfile = (Button) findViewById(R.id.btn_myprofile_submit);

        mPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        detector = new ConnectionDetector(MyProfileDetailsActivity.this);
        if (detector.networkConnectivity()){

            new loadState().execute();

        }else {

            Toast.makeText(MyProfileDetailsActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }



        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = edt_FirstName.getText().toString().trim();
                lastName = edt_lastName.getText().toString().trim();
                village_Name = edt_village.getText().toString().trim();
                if (firstName.length()==0 && lastName.length() ==0 && village_Name.length() ==0){

                    edt_FirstName.setError(getResources().getString(R.string.name));
                    edt_village.setError(getResources().getString(R.string.village));

                }else {
                    new farmerUpdateTask().execute();
                }
            }
        });

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyProfileDetailsActivity.this,MyProfileActivity.class));

            }
        });

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_userName:
                   // validateName();
                    break;
                case R.id.edt_userLastName:
                    //validateLastName();
                    break;
                case R.id.edt_userVillage:
                   // validate();
                    break;

            }
        }
    }


    private class loadState extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MyProfileDetailsActivity.this);
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

           // JSONObject jsonObject = myProf_parser.makeHttpRequest(Config.allStateUrl,"GET",para);
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

            StateAdapter adapter = new StateAdapter(MyProfileDetailsActivity.this,android.R.layout.simple_spinner_dropdown_item,stateList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stateSpinner.setAdapter(adapter);

            stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            mDialog = new ProgressDialog(MyProfileDetailsActivity.this);
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

          //  JSONObject jsonObject = myProf_parser.makeHttpRequest(Config.allDistrictUrl,"GET",para1);
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

            DistrictAdapter adapter = new DistrictAdapter(MyProfileDetailsActivity.this,android.R.layout.simple_spinner_dropdown_item,districtList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            districtSpinner.setAdapter(adapter);

            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            mDialog = new ProgressDialog(MyProfileDetailsActivity.this);
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
         //   JSONObject jsonObject = myProf_parser.makeHttpRequest(Config.allTalukaUrl,"GET",para2);
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

            TalukaAdapter adapter = new TalukaAdapter(MyProfileDetailsActivity.this,android.R.layout.simple_spinner_dropdown_item,talukaList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            talukaSpinner.setAdapter(adapter);

            talukaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private class farmerUpdateTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MyProfileDetailsActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> para3 = new ArrayList<>();
            para3.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));
            para3.add(new BasicNameValuePair("FirstName",firstName));
            para3.add(new BasicNameValuePair("LastName",lastName));
            para3.add(new BasicNameValuePair("StateId",String.valueOf(stateId)));
            para3.add(new BasicNameValuePair("CityId",String.valueOf(districtId)));
            para3.add(new BasicNameValuePair("TalukaId",String.valueOf(talukaId)));
            para3.add(new BasicNameValuePair("VillageName",village_Name));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerUpdateUrl,ServiceHandler.GET,para3);

           // JSONObject jsonObject = myProf_parser.makeHttpRequest(Config.farmerUpdateUrl,"GET",para3);
            if (jsonstr !=null) {
                try {

                    Thread.sleep(2000);

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    updateSuccess = jsonObject.getInt("success");


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

            if (updateSuccess ==1){

                Toast.makeText(MyProfileDetailsActivity.this, "Farmer updated successfully...", Toast.LENGTH_SHORT).show();


            }else {

                Toast.makeText(MyProfileDetailsActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((mDialog != null) && mDialog.isShowing())
            mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyProfileDetailsActivity.this,MyProfileActivity.class));

    }
}






