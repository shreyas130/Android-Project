package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import in.co.SMRK.shetkariapp.model.CropModelTwo;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class SelectCropRateActivity extends AppCompatActivity {

    private ListView mAllCroplist;
    private JSONParser crop_parser;
    private int farmerId, cropSuccess, cropId, addSuccess;
    private ProgressDialog mDialog;
    private Toolbar mToolbar;
    private ArrayList<CropModelTwo> cropList;
    private SharedPreferences mPreference;
    private ImageView img_cropRate_refresh,btn_backArrow;
    private ConnectionDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crop_rate);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAllCroplist = (ListView) findViewById(R.id.list_AllCrop);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);
        img_cropRate_refresh = (ImageView) findViewById(R.id.img_cropRate_refresh);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        detector = new ConnectionDetector(SelectCropRateActivity.this);

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectCropRateActivity.this,MarketRateActivity.class));

            }
        });

        mPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        crop_parser = new JSONParser();

        if (detector.networkConnectivity()){

            new loadCropTask().execute();

        }else {

            Toast.makeText(SelectCropRateActivity.this,getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }


        img_cropRate_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new loadCropTask().execute();
            }
        });
    }


    private class loadCropTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(SelectCropRateActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            farmerId = mPreference.getInt("login_farmerId", 0);
            cropList = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("FarmerId", String.valueOf(farmerId)));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerAllCropUrl,ServiceHandler.GET,para);
           // JSONObject jsonObject = crop_parser.makeHttpRequest(Config.farmerAllCropUrl, "GET", para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    cropSuccess = jsonObject.getInt("success");
                    if (cropSuccess == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("cropid");
                            String cropName = object.getString("cropname");
                            String thumbnails = object.getString("Column1");

                            CropModelTwo cropModel = new CropModelTwo(id, cropName, thumbnails);

                            cropList.add(cropModel);
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


            if (cropSuccess == 1) {

                cropDetailsAdapter adapter = new cropDetailsAdapter(SelectCropRateActivity.this,android.R.layout.simple_list_item_1, cropList);
                mAllCroplist.setAdapter(adapter);

                mAllCroplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    }
                });

            }
        }
    }

    private class cropAddTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(SelectCropRateActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> para1 = new ArrayList<>();
            para1.add(new BasicNameValuePair("CropId", String.valueOf(cropId)));
            para1.add(new BasicNameValuePair("FarmerId", String.valueOf(farmerId)));
            para1.add(new BasicNameValuePair("Type", "I"));
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.farmerAddorDeleteCropUrl,ServiceHandler.GET,para1);
          //  JSONObject jsonObject = crop_parser.makeHttpRequest(Config.farmerAddorDeleteCropUrl, "GET", para1);
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    addSuccess = jsonObject.getInt("success");

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

            if (addSuccess == 1) {

                new loadCropTask().execute();

                Toast.makeText(SelectCropRateActivity.this, "Crop added Successfully...", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(SelectCropRateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class cropDetailsAdapter extends ArrayAdapter<CropModelTwo> {

        private Context mContext;
        private ArrayList<CropModelTwo> imageList;
        private LayoutInflater inflater = null;
       // private CropModelTwo modelTwo;

        public cropDetailsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<CropModelTwo> data) {
            super(context, resource, data);

            this.mContext = context;
            this.imageList = data;


        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

         //   ViewHolder viewHolder;
            View row;

                inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.rate_listview,parent,false);
              //  viewHolder = new ViewHolder();

                ImageView flag = (ImageView) row.findViewById(R.id.img_cropImage);
               TextView tvTitle = (TextView) row.findViewById(R.id.tv_cropTitle);
               ImageView btn_Add = (ImageView) row.findViewById(R.id.img_addCrop);

               // view.setTag(viewHolder);

              CropModelTwo  modelTwo = imageList.get(position);

                if (modelTwo !=null){

                    tvTitle.setText(imageList.get(position).getCropName());

                    Glide.with(mContext)
                            .load(imageList.get(position).getThumbnails())
                            .into(flag);

                    btn_Add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cropId = imageList.get(position).getCropId();

                            new cropAddTask().execute();

                        }
                    });

            }/*else {

              //  viewHolder = (ViewHolder)view.getTag();
            }*/

            return row;
        }

        /*public  class ViewHolder{

            ImageView img_cropImage;
            TextView tvTitle;
            ImageView btn_Add;

        }*/
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
        startActivity(new Intent(SelectCropRateActivity.this,MarketRateActivity.class));

    }
}