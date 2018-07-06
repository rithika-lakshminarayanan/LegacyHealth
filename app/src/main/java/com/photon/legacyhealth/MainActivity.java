package com.photon.legacyhealth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.photon.legacyhealth.pojo.MetaData;
import com.photon.legacyhealth.pojo.NeighborhoodListData;
import com.photon.legacyhealth.pojo.NeighborhoodListDataResponseData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private boolean dialogBoxOk;
    private SimpleFragmentPagerAdapter adapter;
    private double latitude, longitude;
    public static final String MyPREFERENCES = "MyPrefs";
    private TabLayout tabLayout;
    private DrawerLayout mDrawerLayout;
    private boolean isLocationSupported;
    private APIInterface apiInterface;
    private ArrayList<NeighborhoodListDataResponseData> allNeighborhoodDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        getAllNeighborhoods();

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);                                                    /* Add hamburger menu icon to action bar */
        mDrawerLayout = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        final ArrayList<Feeling> fl = intent.getParcelableArrayListExtra("feelings_list");
        final ArrayList<Feeling> doingList = intent.getParcelableArrayListExtra("activity_list");
        final ArrayList<Feeling> indoor = intent.getParcelableArrayListExtra("indoor_activities");
        final ArrayList<Feeling> outdoor = intent.getParcelableArrayListExtra("outdoor_activities");
        final ArrayList<FeelingFeedback> ffList = intent.getParcelableArrayListExtra("feeling_feedback_list");
        final ArrayList<FeelingFeedback> dfList = intent.getParcelableArrayListExtra("doing_feedback_list");
        final ArrayList<String> tipSymName = intent.getStringArrayListExtra("tip_symptom_list");
        final ArrayList<String> tipImgList = intent.getStringArrayListExtra("tip_img_list");
        final ArrayList<MetaData> annotations = intent.getParcelableArrayListExtra("activity_annotations");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String locationSupported = sharedpreferences.getString("isLocationSupported", "");
        String rText;
        if (locationSupported.equalsIgnoreCase("true")) {
            rText = sharedpreferences.getString("neighborhood", "");                           /* Get the current location from sharedPreferences and set it as the title */
            isLocationSupported = true;
        } else {
            isLocationSupported = false;
            rText = getString(R.string.stumptown_mactivity);
        }

        if (!isLocationSupported) {
            new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme)
                    .setMessage(R.string.too_far_away_dialog)                          /* If user is not in supported neighbourhood, give them option to check the supported neighbourhood */
                    .setPositiveButton(R.string.okay_dialog_box, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogBoxOk = true;
                            latitude = 45.52;
                            longitude = -122.57;
                            isLocationSupported = true;
                            NonSwipeableViewPager viewPager = findViewById(R.id.viewpager);

                            // Create an adapter that knows which fragment should be shown on each page
                            adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),MainActivity.this,  latitude,longitude, isLocationSupported, dialogBoxOk, fl, doingList, indoor,  outdoor, ffList, dfList, tipImgList, tipSymName, annotations, allNeighborhoodDetails);

                            // Set the adapter onto the view pager
                            viewPager.setAdapter(adapter);

                            // Give the TabLayout the ViewPager
                            tabLayout =  findViewById(R.id.tab_lyt);
                            tabLayout.setupWithViewPager(viewPager);

                            setCustomFont();
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogBoxOk = false;
                            latitude = Float.parseFloat(sharedpreferences.getString("latitude", ""));
                            longitude = Float.parseFloat(sharedpreferences.getString("longitude", ""));
                            NonSwipeableViewPager viewPager = findViewById(R.id.viewpager);

                            // Create an adapter that knows which fragment should be shown on each page
                            adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),MainActivity.this,  latitude,longitude, isLocationSupported, dialogBoxOk, fl, doingList, indoor,  outdoor, ffList, dfList, tipImgList, tipSymName, annotations, allNeighborhoodDetails);

                            // Set the adapter onto the view pager
                            viewPager.setAdapter(adapter);

                            // Give the TabLayout the ViewPager
                            tabLayout =  findViewById(R.id.tab_lyt);
                            tabLayout.setupWithViewPager(viewPager);

                            setCustomFont();
                        }
                    })
                    .show();
        }
        else {

            latitude = Float.parseFloat(sharedpreferences.getString("latitude", ""));
            longitude = Float.parseFloat(sharedpreferences.getString("longitude", ""));
            NonSwipeableViewPager viewPager = findViewById(R.id.viewpager);

            // Create an adapter that knows which fragment should be shown on each page
            adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),MainActivity.this,  latitude,longitude, isLocationSupported, dialogBoxOk, fl, doingList, indoor,  outdoor, ffList, dfList, tipImgList, tipSymName, annotations, allNeighborhoodDetails);

            // Set the adapter onto the view pager
            viewPager.setAdapter(adapter);

            // Give the TabLayout the ViewPager
            tabLayout =  findViewById(R.id.tab_lyt);
            tabLayout.setupWithViewPager(viewPager);

            setCustomFont();
        }

        title.setText(rText);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                ImageButton crsBtn = findViewById(R.id.menu_closebtn);
                crsBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCustomFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Typeface typeface = ResourcesCompat.getFont(getBaseContext(), R.font.klinic_slab_medium);
                    ((TextView) tabViewChild).setTypeface(typeface,Typeface.NORMAL);
                }
            }
        }
    }

    public void setNeighborhood(String nhood){
        TextView title = findViewById(R.id.toolbar_title);
        if(nhood!=null)
        title.setText(nhood);
    }

    public void getAllNeighborhoods()
    {
        allNeighborhoodDetails = new ArrayList<>();
        Call<NeighborhoodListData> metadataResponse = apiInterface.neighborhood_list();
        metadataResponse.enqueue(new Callback<NeighborhoodListData>() {
            @Override
            public void onResponse(Call<NeighborhoodListData> call, Response<NeighborhoodListData> response) {
                NeighborhoodListData resource = response.body();
                for(int i=0;i<resource.getResponseData().size();i++)
                    allNeighborhoodDetails.add(resource.getResponseData().get(i));
            }

            @Override
            public void onFailure(Call<NeighborhoodListData> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
