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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import in.co.SMRK.shetkariapp.adapter.ScheduleTypeAdapter;
import in.co.SMRK.shetkariapp.adapter.SchedulerAdapter;
import in.co.SMRK.shetkariapp.model.CropSchdulerModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class Schedule_TypeActivity extends AppCompatActivity {

    private MaterialSpinner mScheduleType;
    private ListView mListView;
    private ProgressDialog mDialog;
    private ArrayList<CropSchdulerModel> arrayList,typeArraylist;
    private CropSchdulerModel model;
    private int cropId,schedulerId;
    private JSONParser jsonParser;
    private SharedPreferences mPreference;
    private Toolbar mToolbar;
    private TextView typeOfSoil,typesOfSpary;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_type);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //cropId = getIntent().getExtras().getInt("cropId");
        mScheduleType = (MaterialSpinner) findViewById(R.id.spinner_cropManage);
        mListView = (ListView) findViewById(R.id.list_types_manage1);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        jsonParser = new JSONParser();


        cropId = mPreference.getInt("cropId",0);
        detector = new ConnectionDetector(Schedule_TypeActivity.this);

        if (detector.networkConnectivity()){

            new loadspinnerTask().execute();

        }else {

            Toast.makeText(Schedule_TypeActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Schedule_TypeActivity.this,CropScheduleActivity.class));
            }
        });


    }
        private class loadspinnerTask extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mDialog = new ProgressDialog(Schedule_TypeActivity.this);
                mDialog.setMessage(getResources().getString(R.string.please_wait));
                mDialog.setCancelable(false);
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                arrayList = new ArrayList<>();
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("cropid",String.valueOf(cropId)));
                ServiceHandler handler = new ServiceHandler();
                String jsonstr = handler.makeServiceCall(Config.selectedCropTypeUrl,ServiceHandler.GET,para);

             //   JSONObject object = jsonParser.makeHttpRequest(Config.selectedCropTypeUrl,"GET",para);
                JSONArray jsonArray = null;
                if (jsonstr !=null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonstr);
                        int success = jsonObject.getInt("success");

                        if (success == 1) {

                            jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject c = jsonArray.getJSONObject(i);

                                int scheduleTypeId = c.getInt("schedulerid");
                                String scheduleType = c.getString("SchedulerName");

                                model = new CropSchdulerModel(scheduleTypeId, scheduleType);
                                arrayList.add(model);
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

                SchedulerAdapter adapter = new SchedulerAdapter(Schedule_TypeActivity.this,android.R.layout.simple_spinner_dropdown_item,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mScheduleType.setAdapter(adapter);


                mScheduleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position != -1){

                            new loadlistTask().execute();
                            mPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            schedulerId = mPreference.getInt("scheduleId",0);
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }

        private class loadlistTask extends AsyncTask<String,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mDialog = new ProgressDialog(Schedule_TypeActivity.this);
                mDialog.setMessage(getResources().getString(R.string.please_wait));
                mDialog.setCancelable(false);
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                typeArraylist = new ArrayList<>();
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("cropid",String.valueOf(cropId)));
                para.add(new BasicNameValuePair("schedulerid",String.valueOf(schedulerId)));
                ServiceHandler handler = new ServiceHandler();
                String jsonstr = handler.makeServiceCall(Config.soilTypeUrl,ServiceHandler.GET,para);
             //   JSONObject jsonObject = jsonParser.makeHttpRequest(Config.soilTypeUrl,"GET",para);
                JSONArray jsonArray = null;
                if (jsonstr !=null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonstr);
                        int success = jsonObject.getInt("success");

                        if (success == 1) {

                            jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject c = jsonArray.getJSONObject(i);

                                String type = c.getString("TypeName");
                                int typeId = c.getInt("typeid");

                                CropSchdulerModel model = new CropSchdulerModel(type, typeId);
                                typeArraylist.add(model);
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

                ScheduleTypeAdapter adapter = new ScheduleTypeAdapter(Schedule_TypeActivity.this,android.R.layout.simple_list_item_1,typeArraylist);
                mListView.setAdapter(adapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CropSchdulerModel model = (CropSchdulerModel)parent.getItemAtPosition(position);
                        int typeId = model.getTypeId();

                        Intent intent = new Intent(Schedule_TypeActivity.this,AllSchedulerListActivity.class);
                        /*intent.putExtra("cropId",cropId);
                        intent.putExtra("schedulerId",schedulerId);
                        intent.putExtra("typeId",typeId);*/
                        mPreference.edit().putInt("sc_cropId",cropId).commit();
                        mPreference.edit().putInt("sc_schedulerId",schedulerId).commit();
                        mPreference.edit().putInt("sc_typeId",typeId).commit();

                        startActivity(intent);
                    }
                });


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
        startActivity(new Intent(Schedule_TypeActivity.this,CropScheduleActivity.class));

    }
}
