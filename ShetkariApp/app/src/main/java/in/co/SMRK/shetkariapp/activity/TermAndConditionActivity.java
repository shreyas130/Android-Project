package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class TermAndConditionActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView tv_Title;
    private TextView tv_OtherText;
    private ProgressDialog mDialog;
    private JSONParser term_parser;
    private int success;
    private String title,desc;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_Title = (TextView) findViewById(R.id.tv_termTitle);
        tv_OtherText = (TextView) findViewById(R.id.tv_otherText);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);


        term_parser = new JSONParser();
        detector = new ConnectionDetector(this);

        if (detector.networkConnectivity()){

            new termConditionTask().execute();


        }else {
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();

        }

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TermAndConditionActivity.this,MainActivity.class));
            }
        });


    }

    private class termConditionTask extends AsyncTask<String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(TermAndConditionActivity.this);

            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> para = new ArrayList<>();
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.termConditionUrl,ServiceHandler.GET,para);
          //  JSONObject jsonObject = term_parser.makeHttpRequest(Config.termConditionUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            title = c.getString("pagetitle");
                            desc = c.getString("pagedescription");
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

                tv_Title.setText(title);
                tv_OtherText.setText(desc);

            }else {

                Toast.makeText(TermAndConditionActivity.this, "Term and condition not available", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(TermAndConditionActivity.this,MainActivity.class));
    }
}
