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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.WeatherModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class WeatherActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView btn_backArrow;
    private ProgressDialog mDialog;
    private String village,date;
    private SharedPreferences mPreference;
    private JSONParser weather_parser;
    private int success;
    private WeatherModel model;
    private TextView tv_city,tv_date,tv_min,tv_max, tv_humanity,tv_desc;
    private ImageView img_weatherIcon;
    Calendar calendar;
    SimpleDateFormat mdformat;
    private ConnectionDetector detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        weather_parser = new JSONParser();
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        detector = new ConnectionDetector(WeatherActivity.this);

        tv_city = (TextView) findViewById(R.id.tv_weatherCity);
        tv_date = (TextView) findViewById(R.id.tv_weatherDate);
        tv_max = (TextView) findViewById(R.id.tv_maxTempValue);
        tv_min = (TextView) findViewById(R.id.tv_minTempValue);
        tv_humanity = (TextView) findViewById(R.id.tv_huminity_value);
        tv_desc = (TextView) findViewById(R.id.tv_weatherDesc);
        img_weatherIcon = (ImageView) findViewById(R.id.img_weatherIcon);

        calendar = Calendar.getInstance();
        mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        date = mdformat.format(calendar.getTime());

        if (detector.networkConnectivity()){

            new loadWeatherData().execute();

        }else {

            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }


        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WeatherActivity.this,MainActivity.class));
            }
        });

    }

    private class loadWeatherData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(WeatherActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            village = mPreference.getString("village",null);
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("CityName",village));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.weatherUrl,ServiceHandler.GET,para);
          /JSONObject jsonObject = weather_parser.makeHttpRequest(Config.weatherUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    success = jsonObject.getInt("success");
                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            String city = c.getString("CityName");
                            String desc = c.getString("Description");
                            String imgUrl = c.getString("WeatherIconImageUrl");
                            String minTemp = c.getString("TempMin");
                            String a = minTemp.substring(0, minTemp.length() - 4);
                            String maxTemp = c.getString("TempMax");
                            String b = maxTemp.substring(0, maxTemp.length() - 4);
                            String huminity = c.getString("Humidity");

                            model = new WeatherModel(city, a, b, imgUrl, desc, huminity);
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

            if (success ==1) {
                tv_city.setText(model.getWeatherLoc());
                tv_date.setText(date);
                tv_min.setText(model.getMinTemp()+ " \u2103");
                tv_max.setText(model.getMaxTemp()+ " \u2103");
                tv_humanity.setText(model.getHuminity());
                tv_desc.setText(model.getDesc());

                Glide.with(WeatherActivity.this)
                        .load(model.getThmbnails())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_weatherIcon);
            }else {

                Toast.makeText(WeatherActivity.this, "Weather not available", Toast.LENGTH_SHORT).show();
            }
        }
    }
 }
