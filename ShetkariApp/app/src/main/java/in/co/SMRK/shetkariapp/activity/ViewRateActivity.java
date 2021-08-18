package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.AllrateAdapter;
import in.co.SMRK.shetkariapp.model.MarketRateModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class ViewRateActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    int cropId,rateSuccess;
    private ListView mListview;
    private ProgressDialog mDiaglog;
    private JSONParser rate_parser;
    private ArrayList<MarketRateModel> rateList;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rate);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cropId = getIntent().getExtras().getInt("Cid");

        mListview = (ListView) findViewById(R.id.list_allRate);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        rate_parser = new JSONParser();
        detector = new ConnectionDetector(ViewRateActivity.this);

        if (detector.networkConnectivity()){

            new loadListTask().execute();

        }else {

            Toast.makeText(ViewRateActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ViewRateActivity.this,MarketRateActivity.class));
            }
        });


    }

    private class loadListTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDiaglog = new ProgressDialog(ViewRateActivity.this);
            mDiaglog.setMessage(getResources().getString(R.string.please_wait));
            mDiaglog.setCancelable(false);
            mDiaglog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            rateList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("CropId",String.valueOf(cropId)));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerMarketUrl,ServiceHandler.GET,para);
          //  JSONObject jsonObject = rate_parser.makeHttpRequest(Config.farmerMarketUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    rateSuccess = jsonObject.getInt("success");

                    if (rateSuccess == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            int rateId = c.getInt("rateid");
                            String placeName = c.getString("marketcityname");
                            String currentDate = c.getString("ratedate");
                            String min = c.getString("minrate");
                            String max = c.getString("maxrate");

                            MarketRateModel model = new MarketRateModel(rateId, placeName, currentDate, min, max);
                            rateList.add(model);
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

            if (mDiaglog.isShowing())
                mDiaglog.dismiss();

            if (rateSuccess ==1){

                AllrateAdapter adapter = new AllrateAdapter(ViewRateActivity.this,android.R.layout.simple_list_item_1,rateList);
                mListview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Toast.makeText(ViewRateActivity.this, "Daily rate...", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(ViewRateActivity.this, "Rate not available..", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if ((mDiaglog != null) && mDiaglog.isShowing())
            mDiaglog.dismiss();
        mDiaglog = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewRateActivity.this,MarketRateActivity.class));

    }
}
