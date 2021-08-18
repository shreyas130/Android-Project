package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.GalleryAdapter;
import in.co.SMRK.shetkariapp.model.GalleryModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class GalleryHomeActivity extends AppCompatActivity {

    private GridView galleryView;
    private GalleryModel model;
    private ProgressDialog mDialog;
    private ArrayList<GalleryModel> list;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        detector = new ConnectionDetector(this);

        galleryView = (GridView)findViewById(R.id.grid_gallery);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(GalleryHomeActivity.this,MainActivity.class));
            }
        });

        if (detector.networkConnectivity()){

            new loadGalleryTitle().execute();

        }else {

            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private class loadGalleryTitle extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(GalleryHomeActivity.this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            list = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.galleryFloderUrl,ServiceHandler.GET,para);
            // JSONObject jsonObject = jsonParser.makeHttpRequest(Config.galleryFloderUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    int success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            String galleryId = c.getString("galleryid");
                            String galleryTitle = c.getString("eventname");
                            String flag = c.getString("imagepath");

                            model = new GalleryModel(galleryId, galleryTitle, flag);

                            list.add(model);
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


            galleryView.setAdapter(new GalleryAdapter(GalleryHomeActivity.this,android.R.layout.simple_list_item_1,list));

            galleryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    model = (GalleryModel) parent.getItemAtPosition(position);
                    String galleryId = model.getGalleryId();
                    Intent intent = new Intent(GalleryHomeActivity.this, GalleryActivity.class);
                    intent.putExtra("galleryId",galleryId);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(GalleryHomeActivity.this,MainActivity.class));
    }
}
