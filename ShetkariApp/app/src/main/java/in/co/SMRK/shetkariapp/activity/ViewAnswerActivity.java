package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.QuestionModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class ViewAnswerActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private int questionId;
    private TextView tv_Question,tv_Answer,tv_AnswerDateTime;
    private JSONParser answerParser;
    private ProgressDialog dialog;
    QuestionModel model;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;
    private SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_Question = (TextView) findViewById(R.id.tv_askQuestion);
        tv_Answer = (TextView) findViewById(R.id.tv_questionAnswer);
        tv_AnswerDateTime = (TextView) findViewById(R.id.tv_answerDate);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);
        tv_Answer.setMovementMethod(new ScrollingMovementMethod());
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

      //  questionId = getIntent().getExtras().getInt("questionId");

        answerParser = new JSONParser();

        detector = new ConnectionDetector(ViewAnswerActivity.this);
        if (detector.networkConnectivity()){

            new answerDataTask().execute();

        }else {

            Toast.makeText(ViewAnswerActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ViewAnswerActivity.this,AskExpertActivity.class));

            }
        });



    }

    private class answerDataTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ViewAnswerActivity.this);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            questionId = mPreference.getInt("questionId",questionId);
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("QuestionId",String.valueOf(questionId)));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.viewQuestionUrl,ServiceHandler.GET,para);
          //  JSONObject jsonObject = answerParser.makeHttpRequest(Config.viewQuestionUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    int success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            String question = c.getString("question");
                            String answer = c.getString("answer");
                            String questionDate = c.getString("questiondate");
                            String answerDate = c.getString("answerdate");

                            model = new QuestionModel(question, questionDate, answer, answerDate);
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

            String question = model.getQuestion();
            String answer = model.getAnswerTitle();
            String answerDate = model.getAnswerDate();

                tv_Question.setText(question);
                tv_Answer.setText(answer);
                tv_AnswerDateTime.setText(answerDate);



        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }
}
