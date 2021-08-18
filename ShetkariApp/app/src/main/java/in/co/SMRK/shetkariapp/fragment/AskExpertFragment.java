package in.co.SMRK.shetkariapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.co.SMRK.shetkariapp.activity.ViewAnswerActivity;
import in.co.SMRK.shetkariapp.activity.NewQuestionActivity;
import in.co.SMRK.shetkariapp.adapter.QuestionAdapter;
import in.co.SMRK.shetkariapp.model.QuestionModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class AskExpertFragment extends Fragment {

    private ListView mListView;
    private ImageView imageView;
    private ProgressDialog dialog;
    private ArrayList<QuestionModel> questionList;
    private JSONParser questionParser;
    private int farmerId;
    private SharedPreferences mPreference;
    private ConnectionDetector detector;

    public AskExpertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ask_expert, container, false);

        questionParser = new JSONParser();

        imageView = (ImageView) view.findViewById(R.id.img_newQuestion);
        mListView = (ListView) view.findViewById(R.id.list_allQuestion);

        mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        detector = new ConnectionDetector(getActivity());

        if (detector.networkConnectivity()){

            new loadAllQuestionTask().execute();

        }else {

            Toast.makeText(getActivity(), getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewQuestionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private class loadAllQuestionTask extends AsyncTask<String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
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
            String jsonstr = handler.makeServiceCall(Config.allQuestionUrl,ServiceHandler.GET,para);
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

            QuestionAdapter adapter = new QuestionAdapter(getActivity(),android.R.layout.simple_list_item_1,questionList);

            adapter.notifyDataSetChanged();

                mListView.setAdapter(adapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        QuestionModel data = (QuestionModel)parent.getItemAtPosition(position);

                        int QId = data.getQuestionId();
                        Intent intent = new Intent(getActivity(), ViewAnswerActivity.class);
                        intent.putExtra("questionId",QId);
                        startActivity(intent);
                    }
                });
        }
    }

    /*@Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }*/
}


