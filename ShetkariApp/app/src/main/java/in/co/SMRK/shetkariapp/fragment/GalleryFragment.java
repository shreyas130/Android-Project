package in.co.SMRK.shetkariapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.activity.GalleryActivity;
import in.co.SMRK.shetkariapp.adapter.GalleryAdapter;
import in.co.SMRK.shetkariapp.model.GalleryModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;


public class GalleryFragment extends Fragment {

    private GridView galleryView;
    private GalleryModel model;
    private GalleryAdapter adapter;
    private JSONParser jsonParser;
    private ProgressDialog mDialog;
    private ArrayList<GalleryModel> list;
    private ConnectionDetector detector;

    public GalleryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        detector = new ConnectionDetector(getActivity());
        jsonParser = new JSONParser();
        galleryView = (GridView) view.findViewById(R.id.grid_gallery);

        if (detector.networkConnectivity()){

            new loadGalleryTitle().execute();

        }else {

            Toast.makeText(getActivity(), getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }



        return view;
    }

    private class loadGalleryTitle extends AsyncTask<String,Void,String>{

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


            galleryView.setAdapter(new GalleryAdapter(getActivity(),android.R.layout.simple_list_item_1,list));

            galleryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    model = (GalleryModel) parent.getItemAtPosition(position);

                    String galleryTitle = model.getGalleryTitle();

                    String galleryId = model.getGalleryId();
                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                    intent.putExtra("galleryId",galleryId);
                    startActivity(intent);

                    Toast.makeText(getActivity(), "Title"+galleryTitle, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*@Override
    public void onPause() {
        super.onPause();

        if ((mDialog != null) && mDialog.isShowing())
            mDialog.dismiss();
        mDialog = null;
    }*/


}
