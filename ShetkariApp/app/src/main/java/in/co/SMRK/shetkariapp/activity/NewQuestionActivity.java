package in.co.SMRK.shetkariapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.utlis.Config;
import in.co.SMRK.shetkariapp.utlis.ConnectionDetector;
import in.co.SMRK.shetkariapp.utlis.ServiceHandler;

public class NewQuestionActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText edtNewQues;
    private ImageView preview1,preview2;
    int success,farmerId;
    private String question,question_end;
    private ProgressDialog dialog;
    private int PICK_IMAGE_REQUEST1 = 1;
    private int PICK_IMAGE_REQUEST2 = 2;
    private Bitmap bitmap1,bitmap2;
    private SharedPreferences mPreference;
    private ConnectionDetector detector;
    private ImageView btn_backArrow;
    private Button btn_upload;
    private String imageOne = "";
    private String imageTwo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        edtNewQues = (EditText) findViewById(R.id.edt_typeQuestion);
        preview1 = (ImageView) findViewById(R.id.preview1);
        preview2 = (ImageView) findViewById(R.id.preview2);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);
        btn_upload = (Button) findViewById(R.id.btn_upload);

        mPreference = PreferenceManager.getDefaultSharedPreferences(NewQuestionActivity.this);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question = edtNewQues.getText().toString().trim();
                try {
                    question_end = URLEncoder.encode(question,"UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (question.length() ==0){

                    edtNewQues.setError(getResources().getString(R.string.text_message));

                }else {
                    new postData().execute();
                }


            }
        });

        preview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitmap1 !=null){
                    bitmap1.recycle();
                    bitmap1 = null;
                    showFileChooser(PICK_IMAGE_REQUEST1);
                }else {

                    showFileChooser(PICK_IMAGE_REQUEST1);
                }

            }
        });

        preview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser(PICK_IMAGE_REQUEST2);
            }
        });

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NewQuestionActivity.this,AskExpertActivity.class));

            }
        });

    }


    private class postData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQuestionActivity.this);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            farmerId = mPreference.getInt("login_farmerId",0);
            if (bitmap1 !=null){

                imageOne = getStringImage(bitmap1);
            }
            if (bitmap2 !=null){

                imageTwo = getStringImage(bitmap2);
            }
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("FarmerId",String.valueOf(farmerId)));
                para.add(new BasicNameValuePair("Question",question_end));
                para.add(new BasicNameValuePair("Image1",imageOne));
                para.add(new BasicNameValuePair("Image1_Extension","jpg"));
                para.add(new BasicNameValuePair("Image2",imageTwo));
                para.add(new BasicNameValuePair("Image2_Extension","jpg"));
                para.add(new BasicNameValuePair("Image3",""));
                para.add(new BasicNameValuePair("Image3_Extension",""));
                para.add(new BasicNameValuePair("Image4",""));
                para.add(new BasicNameValuePair("Image4_Extension",""));

            ServiceHandler handler = new ServiceHandler();
            String jsonstr = handler.makeServiceCall(Config.sendQuestion,ServiceHandler.POST,para);
          //  JSONObject jsonObject = jsonParser.makeHttpRequest(Config.sendQuestion,"POST",para);
            if (jsonstr !=null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonstr);
                    success = jsonObject.getInt("success");

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
            if (success ==1){

                Toast.makeText(NewQuestionActivity.this, "Question inserted successfully", Toast.LENGTH_SHORT).show();
                edtNewQues.setText("");
                startActivity(new Intent(NewQuestionActivity.this,AskExpertActivity.class));

            }else {

                Toast.makeText(NewQuestionActivity.this, "Question Not inserted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser(int requestCode) {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ) {
            Uri filePath = data.getData();

            if (requestCode == PICK_IMAGE_REQUEST1){

                try {
                    //Getting the Bitmap from Gallery
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //Setting the Bitmap to ImageView
                    preview1.setImageBitmap(bitmap1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == PICK_IMAGE_REQUEST2){

                try {
                    //Getting the Bitmap from Gallery
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //Setting the Bitmap to ImageView
                    preview2.setImageBitmap(bitmap2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewQuestionActivity.this,AskExpertActivity.class));

    }
}
