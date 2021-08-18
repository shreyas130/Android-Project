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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.adapter.GalleryRecyclerAdapter;
import in.co.SMRK.shetkariapp.model.GalleryModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class GalleryActivity extends AppCompatActivity {

    private String TAG = GalleryActivity.class.getSimpleName();
    private ArrayList<GalleryModel> list;
    private GalleryModel model;
    private ProgressDialog pDialog;
    private GalleryRecyclerAdapter mAdapter;
    private GridView gridView;
    private String galleryId;
    private JSONParser parser;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        gridView = (GridView) findViewById(R.id.all_gallery_grid);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        galleryId = getIntent().getExtras().getString("galleryId");

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(GalleryActivity.this,GalleryHomeActivity.class));
            }
        });
        pDialog = new ProgressDialog(this);
        detector = new ConnectionDetector(GalleryActivity.this);
        if (detector.networkConnectivity()) {

            new fetchImages().execute();
        }else {

            Toast.makeText(GalleryActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }


    }

    private class fetchImages extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            list = new ArrayList<>();
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("GalleryId",galleryId));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.galleryImageUrl,ServiceHandler.GET,para);
          //  JSONObject jsonObject = parser.makeHttpRequest(Config.galleryImageUrl,"GET",para);
            JSONArray jsonArray = null;
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);

                    int success = jsonObject.getInt("success");

                    if (success == 1) {

                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            String images = c.getString("photooriginalpath");

                            model = new GalleryModel(images);

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

            if (pDialog.isShowing())
                pDialog.dismiss();

            gridView.setAdapter(new GalleryRecyclerAdapter(GalleryActivity.this,android.R.layout.simple_list_item_1,list));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    GalleryModel model1 = (GalleryModel)parent.getItemAtPosition(position);
                    Intent intent = new Intent(GalleryActivity.this,FullGalleryViewActivity.class);
                    intent.putExtra("image",model1.getGalleryImage());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(GalleryActivity.this,GalleryHomeActivity.class));
    }*/
}
