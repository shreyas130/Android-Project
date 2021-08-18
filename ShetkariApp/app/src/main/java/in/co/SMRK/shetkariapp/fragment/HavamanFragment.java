package in.co.SMRK.shetkariapp.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.WeatherModel;
import in.co.SMRK.shetkariapp.utlis.JSONParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HavamanFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<WeatherModel> list;
    private WeatherModel model;
    private ProgressDialog dialog;
    private JSONParser parser;
    String q= "girnare";
    private static final String apiKey = "e4b7fd0aaee4e08b859e0f0744d3859d";


    public HavamanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_havaman, container, false);

        parser = new JSONParser();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_weatherList);

        new loadRecylerView().execute();

        return view;
    }

    private class loadRecylerView extends AsyncTask<String,Void,String>{

        private String url = "http://api.openweathermap.org/data/2.5/forecast?q="+q+"&APPID="+apiKey;
       http://api.openweathermap.org/data/2.5/forecast?q=girnare&appid=e4b7fd0aaee4e08b859e0f0744d3859d

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            list = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();

            JSONObject jsonObject = parser.makeHttpRequest(url,"GET",para);
            JSONArray jsonArray = null;

            try {

                int code = jsonObject.getInt("cod");

                if (code == 200){

                    jsonArray = jsonObject.getJSONArray("weather");

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject c = jsonArray.getJSONObject(i);

                        String id = c.getString("id");
                        Log.e("Wid",id);


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

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    

}
