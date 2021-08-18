package in.co.SMRK.shetkariapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import in.co.SMRK.shetkariapp.activity.SelectedNewsActivity;
import in.co.SMRK.shetkariapp.adapter.NewsAdapter;
import in.co.SMRK.shetkariapp.model.NewsModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;


public class NewsFragment extends Fragment {

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



    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        contactList = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.newsList);

        jsonParser = new JSONParser();
        detector = new ConnectionDetector(getActivity());
        if (detector.networkConnectivity()){

            new newsListTask().execute();

        }else {

            Toast.makeText(getActivity(), "no internet connection...", Toast.LENGTH_SHORT).show();
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsModel newsModel = (NewsModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),SelectedNewsActivity.class);
                newsId = newsModel.getNewsId();
                image = newsModel.getThumbnails();
                intent.putExtra("id",newsId);
                intent.putExtra("image",image);
                Log.e("id",String.valueOf(newsId));
                startActivity(intent);
            }
        });

        return view;
    }


    private class newsListTask extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arrayList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(Config.allNewsUrl,"GET",para);
            JSONArray jsonArray=null;

            try
            {
                int success = jsonObject.getInt("success");

                if (success ==1){

                    jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject c = jsonArray.getJSONObject(i);

                        int newsId1 = c.getInt("newsid");
                        String newsTitle = c.getString("newsname");
                        String newsDate = c.getString("newsdate");
                        String newsImage = c.getString("imagename");

                        model = new NewsModel(newsId1,newsTitle,newsDate,newsImage);
                        arrayList.add(model);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (mDialog.isShowing())
                mDialog.dismiss();

            NewsAdapter adapter = new NewsAdapter(getActivity(),R.layout.listview_row,arrayList);
            listView.setAdapter(adapter);


        }
    }

   /* @Override
    public void onPause() {
        super.onPause();

        if ((mDialog != null) && mDialog.isShowing())
            mDialog.dismiss();
        mDialog = null;
    }*/


}
