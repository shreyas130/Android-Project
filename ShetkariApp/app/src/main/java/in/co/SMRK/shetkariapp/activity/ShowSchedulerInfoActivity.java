package in.co.SMRK.shetkariapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.co.SMRK.shetkariapp.R;

public class ShowSchedulerInfoActivity extends AppCompatActivity {

    private TextView tv_showInformation;
    String info;
    private Toolbar mToolbar;
    private ImageView btn_backArrow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scheduler_info);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);


        tv_showInformation = (TextView) findViewById(R.id.tv_show_info);

        info = getIntent().getExtras().getString("showInfo");

        tv_showInformation.setText(info);

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShowSchedulerInfoActivity.this,AllSchedulerListActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowSchedulerInfoActivity.this,AllSchedulerListActivity.class));

    }
}
