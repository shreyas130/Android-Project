package in.co.SMRK.shetkariapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import in.co.SMRK.shetkariapp.R;

public class AccountDetailsActivity extends AppCompatActivity {

    private ImageView btn_backArrow;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_backArrow = (ImageView) findViewById(R.id.btn_backArrow);

        btn_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AccountDetailsActivity.this,MainActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(AccountDetailsActivity.this,MainActivity.class));
    }
}
