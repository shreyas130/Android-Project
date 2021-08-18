package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.NewsAdapter;
import in.co.SMRK.shetkariapp.model.NewsModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class NewsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private JSONParser jsonParser;
    private ListView listView;
    private NewsAdapter adapter;
    private NewsModel model;
    private ArrayList<NewsModel> arrayList;
    ArrayList<HashMap<String, String>> contactList;
    private ProgressDialog mDialog;
    int newsId;
    String image;
    private SharedPreferences mPreference;
    private ConnectionDetector detector;
    private ImageView btn_backArrow,btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.newsList);
        btn_refresh = (ImageView) findViewById(R.id.img_news_refresh);

        jsonParser = new JSONParser();
        detector = new ConnectionDetector(this);
        if (detector.networkConnectivity()){

            new newsListTask().execute();

        }else {

            Toast.makeText(NewsActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsModel newsModel = (NewsModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(NewsActivity.this,SelectedNewsActivity.class);
                newsId = newsModel.getNewsId();
                image = newsModel.getThumbnails();
                intent.putExtra("id",newsId);
                intent.putExtra("image",image);
                Log.e("id",String.valueOf(newsId));
                startActivity(intent);
            }
        });

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NewsActivity.this,MainActivity.class));
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new newsListTask().execute();
            }
        });
    }

    private class newsListTask extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(NewsActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            arrayList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
           // JSONObject jsonObject = jsonParser.makeHttpRequest(Config.allNewsUrl,"GET",para);

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.allNewsUrl,ServiceHandler.GET,para);
            JSONArray jsonArray=null;

            if (jsonstr !=null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonstr);

                    int success = jsonObject.getInt("success");

                    if (success == 1) {


                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            int newsId1 = c.getInt("newsid");
                            String newsTitle = c.getString("newsname");
                            String newsDate = c.getString("newsdate");
                            String newsImage = c.getString("imagename");

                            model = new NewsModel(newsId1, newsTitle, newsDate, newsImage);
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

            NewsAdapter adapter = new NewsAdapter(NewsActivity.this,R.layout.listview_row,arrayList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewsActivity.this,MainActivity.class));

    }
}
