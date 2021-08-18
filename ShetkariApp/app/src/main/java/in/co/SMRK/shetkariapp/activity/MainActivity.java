package in.co.SMRK.shetkariapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.fragment.DashboardFragment;
import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.utlis.FMessging;
import in.co.SMRK.shetkariapp.utlis.NotificationUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    NavigationView navigationView;
    public static int navItemIndex = 0;
    private static final String TAG_HOME = "nav_home";
    public static String CURRENT_TAG = TAG_HOME;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private static long back_pressed;
    private SharedPreferences mPreference;
    String mobile,first,last;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mHandler = new Handler();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setItemIconTintList(null);
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        mobile = mPreference.getString("mobileNO",null);
        first = mPreference.getString("first",null);
        last = mPreference.getString("last",null);

        TextView tv_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_nav_username);
        TextView tv_mobile = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_mobileNo);
        tv_name.setText(first +"\t"+ last);
        tv_mobile.setText(mobile);


        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;

            loadHomeFragment();
        }

           mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(FMessging.REGISTRATION_COMPLETE)){

                    FirebaseMessaging.getInstance().subscribeToTopic(FMessging.TOPIC_GLOBAL);


                }else if (intent.getAction().equals(FMessging.PUSH_NOTIFICATION)) {

                    String message = intent.getStringExtra("message");

                }

            }
        };


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                DashboardFragment dashboardFragment = new DashboardFragment();
                return dashboardFragment;


            default:
                return new DashboardFragment();
        }
    }

    private void loadHomeFragment() {

        selectNavMenu();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            mDrawerLayout.closeDrawers();

            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.nav_layout, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        //Closing drawer on item click
        mDrawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void displayView(int position)
    {
        Fragment fragment = null;

        switch (position)
        {
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                fragment = new DashboardFragment();
                break;

            case R.id.nav_expert_ask:
                startActivity(new Intent(MainActivity.this,AskExpertActivity.class));
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_gallery:
                startActivity(new Intent(MainActivity.this,GalleryHomeActivity.class));
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_account:
                startActivity(new Intent(MainActivity.this,AccountDetailsActivity.class));
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nav_condition:
                startActivity(new Intent(MainActivity.this,TermAndConditionActivity.class));
                mDrawerLayout.closeDrawers();
                break;

            default:
                navItemIndex = 0;

        }

        if (fragment != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.nav_layout,fragment);
            ft.commit();
            mDrawerLayout.closeDrawers();
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            //loadHomeFragment();
        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Confirm Exit...");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to exit?");

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return;


                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(FMessging.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(FMessging.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

            displayView(item.getItemId());

            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            item.setChecked(true);
            loadHomeFragment();
            return true;

    }
}
