package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
import in.co.SMRK.shetkariapp.adapter.CropSchedulerAdapter;
import in.co.SMRK.shetkariapp.model.CropListModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class CropScheduleActivity extends AppCompatActivity {

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
    private Toolbar mToolbar;
    private ImageView btn_backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_schedule);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mCropList = (GridView) findViewById(R.id.gridview_cropList);
        paid_layout = (RelativeLayout) findViewById(R.id.layout_paid);
        unpaid_layout = (RelativeLayout) findViewById(R.id.layout_unpaid);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        jsonParser = new JSONParser();

        detector = new ConnectionDetector(this);

        if (detector.networkConnectivity()){

            new paidUnpaidTask().execute();
        }else {

            Toast.makeText(CropScheduleActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        mCropList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CropListModel model = (CropListModel) parent.getItemAtPosition(position);
                int cropId = model.getCropId();
                Intent intent = new Intent(CropScheduleActivity.this,Schedule_TypeActivity.class);
               // intent.putExtra("cropId",cropId);
                mPreference.edit().putInt("cropId",cropId).commit();
                startActivity(intent);
            }
        });

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CropScheduleActivity.this,MainActivity.class));
            }
        });
    }

    private class paidUnpaidTask extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(CropScheduleActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            farmerId = mPreference.getInt("login_farmerId",0);
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.paidOrunpaidUrl,ServiceHandler.GET,para);
          //  JSONObject jsonObject = jsonParser.makeHttpRequest(Config.paidOrunpaidUrl,"GET",para);
            if (jsonstr !=null) {
                try {
                    Thread.sleep(3000);

                    JSONObject jsonObject = new JSONObject(jsonstr);

                    checkSuccess = jsonObject.getInt("success");

                    if (checkSuccess == 1) {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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
            mDialog = new ProgressDialog(CropScheduleActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            cropList = new ArrayList<>();

            List<NameValuePair> para = new ArrayList<>();
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.cropListUrl,ServiceHandler.GET,para);
         //   JSONObject jsonObject = jsonParser.makeHttpRequest(Config.cropListUrl,"GET",para);
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            cropId = c.getInt("cropid");
                            String a = String.valueOf(cropId);
                            cropName = c.getString("cropname");
                            String flag = c.getString("imagepath");

                            Log.e("cropId", a);
                            Log.e("cropName", cropName);

                            cropListModel = new CropListModel(cropId, cropName, flag);
                            cropList.add(cropListModel);
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

            if (success ==1){

                CropSchedulerAdapter adapter = new CropSchedulerAdapter(CropScheduleActivity.this,android.R.layout.simple_list_item_1,cropList);
                mCropList.setAdapter(adapter);

            }else {

                Toast.makeText(CropScheduleActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CropScheduleActivity.this,MainActivity.class));

    }
}
