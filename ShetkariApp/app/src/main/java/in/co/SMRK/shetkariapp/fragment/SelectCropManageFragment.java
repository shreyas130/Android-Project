package in.co.SMRK.shetkariapp.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.CropSchdulerModel;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.JSONParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCropManageFragment extends Fragment {

    private MaterialSpinner mScheduleType;
    private ListView mListView;
    private ProgressDialog mDialog;
    private ArrayList<CropSchdulerModel> arrayList;
    private CropSchdulerModel model;
    private int cropId;
    private JSONParser jsonParser;
    private ConnectionDetector detector;


    public SelectCropManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_select_crop, container, false);

        cropId = getActivity().getIntent().getExtras().getInt("cropId");
        mScheduleType = (MaterialSpinner) view.findViewById(R.id.spinner_cropManage);
        mListView = (ListView) view.findViewById(R.id.list_types_manage);

        jsonParser = new JSONParser();

        detector = new ConnectionDetector(getActivity());
        if (detector.networkConnectivity()){

            new loadspinnerTask().execute();

        }else {

            Toast.makeText(getActivity(), "no internet connection...", Toast.LENGTH_SHORT).show();
        }


        mScheduleType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CropSchdulerModel model1 = (CropSchdulerModel)parent.getItemAtPosition(position);
                int s_id = model1.getSchedulerId();
                Toast.makeText(getActivity(), "sId :"+s_id, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class loadspinnerTask extends AsyncTask<String,Void,String>{

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
            para.add(new BasicNameValuePair("cropid",String.valueOf(cropId)));
            JSONObject object = jsonParser.makeHttpRequest(Config.selectedCropTypeUrl,"GET",para);
            JSONArray jsonArray = null;

            try {

                int success = object.getInt("success");

                if (success ==1){

                    jsonArray = object.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject c = jsonArray.getJSONObject(i);

                        int scheduleTypeId = c.getInt("schedulerid");
                        String scheduleType = c.getString("SchedulerName");

                        model = new CropSchdulerModel(scheduleTypeId,scheduleType);
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

            mScheduleType.setAdapter(new ArrayAdapter<CropSchdulerModel>(getActivity(),android.R.layout.simple_list_item_1,arrayList));

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
