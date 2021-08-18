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
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.QuestionAdapter;
import in.co.SMRK.shetkariapp.model.QuestionModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class AskExpertActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;
    private ImageView imageView;
    private ProgressDialog dialog;
    private ArrayList<QuestionModel> questionList;
    private JSONParser questionParser;
    private int farmerId;
    private SharedPreferences mPreference;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_expert);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageView = (ImageView) findViewById(R.id.img_newQuestion);
        mListView = (ListView) findViewById(R.id.list_allQuestion);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);


        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        questionParser = new JSONParser();

        detector = new ConnectionDetector(this);

        if (detector.networkConnectivity()){
            new loadAllQuestionTask().execute();

        }else {

            Toast.makeText(AskExpertActivity.this,getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AskExpertActivity.this, NewQuestionActivity.class);
                startActivity(intent);
            }
        });

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AskExpertActivity.this,MainActivity.class));

            }
        });

    }

    private class loadAllQuestionTask extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AskExpertActivity.this);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            farmerId = mPreference.getInt("login_farmerId",0);
            questionList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.allQuestionUrl,ServiceHandler.POST,para);
           // JSONObject jsonObject = questionParser.makeHttpRequest(Config.allQuestionUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);

                    int success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            int questionId = c.getInt("questid");
                            String questionTitle = c.getString("question");
                            String questionDateTime = c.getString("questiondate");

                            QuestionModel model = new QuestionModel(questionId, questionTitle, questionDateTime);

                            questionList.add(model);
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

            if (dialog.isShowing())
                dialog.dismiss();

            QuestionAdapter adapter = new QuestionAdapter(AskExpertActivity.this,android.R.layout.simple_list_item_1,questionList);

            adapter.notifyDataSetChanged();

            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    QuestionModel data = (QuestionModel)parent.getItemAtPosition(position);

                    int QId = data.getQuestionId();
                    Intent intent = new Intent(AskExpertActivity.this, ViewAnswerActivity.class);
                    mPreference.edit().putInt("questionId",QId).commit();
                    // intent.putExtra("questionId",QId);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AskExpertActivity.this,MainActivity.class));

    }
}
