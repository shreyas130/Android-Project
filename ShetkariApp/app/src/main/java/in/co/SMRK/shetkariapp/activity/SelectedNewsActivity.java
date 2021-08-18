package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.SelectedNewsAdapter;
import in.co.SMRK.shetkariapp.model.SelectedNewsModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class SelectedNewsActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private JSONParser jsonParser;
    private ProgressDialog mDialog;
    private ArrayList<SelectedNewsModel> arrayList;
    private SelectedNewsModel model;
    private SelectedNewsAdapter adapter;
    private TextView tv_title,tv_date,tv_longDescr;
    private int newsId;
    private ImageView expanded_image;
    private SharedPreferences mPreference;
    private String image;
    private ConnectionDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_news);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(false);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.long_info));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        tv_title = (TextView) findViewById(R.id.tv_newsShortTitle);
        tv_date = (TextView) findViewById(R.id.tv_newsDate);
        tv_longDescr =(TextView) findViewById(R.id.tv_newsLongDescription);
        expanded_image = (ImageView) findViewById(R.id.expanded_image);

        newsId = getIntent().getExtras().getInt("id");
        System.out.println("id1"+newsId);
        image = getIntent().getExtras().getString("image");
        jsonParser = new JSONParser();
        detector = new ConnectionDetector(SelectedNewsActivity.this);

        if (detector.networkConnectivity()){

            new selectedNewsTask().execute();

        }else {

            Toast.makeText(SelectedNewsActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }


    }


    private class selectedNewsTask extends AsyncTask<String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(SelectedNewsActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            int a=4;

            arrayList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("newsid",String.valueOf(newsId)));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.selectedNewsUrl,ServiceHandler.GET,para);
          //  JSONObject jsonObject = jsonParser.makeHttpRequest(Config.selectedNewsUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    int success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);


                            String title = c.getString("newsname");
                            String date = c.getString("newsdate");
                            String longDesc = c.getString("longdescp");

                            model = new SelectedNewsModel(title, date, longDesc);
                            // arrayList.add(model);

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

            //adapter = new SelectedNewsAdapter(SelectedNewsActivity.this,R.layout.news_layout,arrayList);

            Glide.with(SelectedNewsActivity.this)
                    .load(image)
                    .into(expanded_image);
            tv_title.setText(model.getShortTitle());
            tv_date.setText(model.getNewsDate());
            tv_longDescr.setText(model.getLongDescription());
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
        startActivity(new Intent(SelectedNewsActivity.this,NewsActivity.class));
    }
}
