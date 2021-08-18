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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.AllSchedulerAdapter;
import in.co.SMRK.shetkariapp.model.CropSchdulerModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class AllSchedulerListActivity extends AppCompatActivity {

    private ListView mListView;
    private ProgressDialog mDialog;
    private ArrayList<CropSchdulerModel> arrayList;
    private ArrayList<String>list;
    private CropSchdulerModel model;
    private JSONParser jsonParser;
    private ArrayList<HashMap<String,String>> schedulerList;
    private Toolbar mToolbar;
    private int cropId,schedulerId,typeId;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;
    private SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_scheduler_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

       /* cropId = getIntent().getExtras().getInt("cropId");
        schedulerId = getIntent().getExtras().getInt("schedulerId");
        typeId = getIntent().getExtras().getInt("typeId");*/
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);


        mListView = (ListView) findViewById(R.id.listview_allScheduler);
        detector = new ConnectionDetector(AllSchedulerListActivity.this);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        jsonParser = new JSONParser();
        if (detector.networkConnectivity()) {

            new loadListData().execute();

        }else {

            Toast.makeText(AllSchedulerListActivity.this,getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CropSchdulerModel model1 = (CropSchdulerModel) parent.getItemAtPosition(position);
                String info = model1.getSchedulerInfo();

                Intent intent = new Intent(AllSchedulerListActivity.this,ShowSchedulerInfoActivity.class);
                intent.putExtra("showInfo",info);
                startActivity(intent);

            }
        });

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AllSchedulerListActivity.this,Schedule_TypeActivity.class));
            }
        });

    }

    private class loadListData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(AllSchedulerListActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            cropId = mPreference.getInt("sc_cropId",0);
            schedulerId = mPreference.getInt("sc_schedulerId",0);
            typeId = mPreference.getInt("sc_typeId",0);
            arrayList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();

            para.add(new BasicNameValuePair("CropId",String.valueOf(cropId)));
            para.add(new BasicNameValuePair("SchedulerId",String.valueOf(schedulerId)));
            para.add(new BasicNameValuePair("TypeId",String.valueOf(typeId)));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.allSchedule,ServiceHandler.POST,para);

           // JSONObject object = jsonParser.makeHttpRequest(Config.allSchedule,"GET",para);
            JSONArray jsonArray = null;

            if (jsonstr !=null) {
                try {

                    JSONObject object = new JSONObject(jsonstr);

                    int success = object.getInt("success");

                    if (success == 1) {

                        jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            String scheduleTitle = c.getString("title");
                            String scheduleInfo = c.getString("information");

                            model = new CropSchdulerModel(scheduleTitle, scheduleInfo);
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


            if (arrayList.isEmpty()){

                Toast.makeText(AllSchedulerListActivity.this, "data not available", Toast.LENGTH_SHORT).show();

            }else {

                AllSchedulerAdapter allSchedulerAdapter = new AllSchedulerAdapter(AllSchedulerListActivity.this, android.R.layout.simple_list_item_1, arrayList);
                mListView.setAdapter(allSchedulerAdapter);
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
        startActivity(new Intent(AllSchedulerListActivity.this,Schedule_TypeActivity.class));

    }
}
