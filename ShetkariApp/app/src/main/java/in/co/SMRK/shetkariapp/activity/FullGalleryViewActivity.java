package in.co.SMRK.shetkariapp.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.GalleryModel;

public class FullGalleryViewActivity extends AppCompatActivity {

    private ImageView imageView;
    private ArrayList<GalleryModel> models;
    private int selectedPosition = 0;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_gallery_view);

        String pos = getIntent().getExtras().getString("image");
       // Bitmap bitmap = getIntent().getParcelableExtra("image");

        imageView = (ImageView) findViewById(R.id.img_fullScreen);

        Glide.with(FullGalleryViewActivity.this).load(pos).into(imageView);
      //  imageView.setImageBitmap(bitmap);

    }
}
