package in.co.SMRK.shetkariapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.co.SMRK.shetkariapp.activity.AskExpertActivity;
import in.co.SMRK.shetkariapp.activity.CropScheduleActivity;
import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.activity.MarketRateActivity;
import in.co.SMRK.shetkariapp.activity.MyProfileActivity;
import in.co.SMRK.shetkariapp.activity.NewsActivity;
import in.co.SMRK.shetkariapp.activity.RegistrationActivity;
import in.co.SMRK.shetkariapp.activity.WeatherActivity;
import in.co.SMRK.shetkariapp.adapter.GridDashboardAdapter;
import in.co.SMRK.shetkariapp.adapter.ImageSliderAdapter;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment{

    private ConnectionDetector detector;
    private static ViewPager mPager;
    private static int currentPage ;
    private GridView mGridview;
    private CircleIndicator mIndicator;
    private ProgressDialog mDialog;
    private ArrayList<String> imagesList;
    private int success,farmerId;
    private SharedPreferences mPreference;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        mGridview = (GridView) view.findViewById(R.id.grid_dashboard);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mIndicator = (CircleIndicator) view.findViewById(R.id.indicator);

        detector = new ConnectionDetector(getActivity());

        mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        farmerId = mPreference.getInt("login_farmerId",0);

        Log.d("loginId",String.valueOf(farmerId));

        if (detector.networkConnectivity()){

            new checkFarmerTask().execute();

        }else {

            Toast.makeText(getActivity(),getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        mGridview.setAdapter(new GridDashboardAdapter(getActivity()));

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                displayView(position);

            }
        });

        return view;
    }

    private void displayView(int position){

        switch (position){

            case 0:
                startActivity(new Intent(getActivity(), MarketRateActivity.class));
                break;

            case 1:
                startActivity(new Intent(getActivity(),WeatherActivity.class));
                break;

            case 2:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;

            case 3:
                startActivity(new Intent(getActivity(), CropScheduleActivity.class));
                break;

            case 4:
                startActivity(new Intent(getActivity(), MyProfileActivity.class));
                break;

            case 5:
                startActivity(new Intent(getActivity(), AskExpertActivity.class));
                break;

            default:
                new DashboardFragment();
        }

    }

    private class loadImagesTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            imagesList = new ArrayList<>();

            List<NameValuePair> para = new ArrayList<>();
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.sliderImgUrl,ServiceHandler.POST,para);

            JSONArray jsonArray = null;

            if (jsonstr !=null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonstr);
                    int success_image = jsonObject.getInt("success");

                    if (success_image == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            String img = c.getString("imagename");

                            imagesList.add(img);
                        }

                    }else {

                        Toast.makeText(getContext(), "slider images not available", Toast.LENGTH_SHORT).show();
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

            if (getActivity() !=null) {

                mPager.setAdapter(new ImageSliderAdapter(getActivity(), imagesList));
                mIndicator.setViewPager(mPager);

            }

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == imagesList.size()) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            },2500,5000);
        }
    }

    private class checkFarmerTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerCheckUrl,ServiceHandler.POST,para);

            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    success = jsonObject.getInt("success");

                } catch (JSONException e) {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (success == 1){

                new loadImagesTask().execute();

            }else {

                Toast.makeText(getActivity(), "Farmer not available", Toast.LENGTH_SHORT).show();
                mPreference.edit().remove("login_farmerId").commit();
                startActivity(new Intent(getActivity(), RegistrationActivity.class));
                getActivity().finish();
            }


        }
    }
}

