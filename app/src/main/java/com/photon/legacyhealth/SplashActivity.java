package com.photon.legacyhealth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.photon.legacyhealth.pojo.MetaData;
import com.photon.legacyhealth.pojo.MetaDataResponse;
import com.photon.legacyhealth.pojo.NeighborhoodData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements LocationListener{    //implements LocationListener

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private LocationManager locationManager;
    private String latitude;
    private String longitude;
    private String nhood;
    private boolean isNhoodSupported = false;
    private APIInterface apiInterface;
    private TextView responseText;
    private boolean hasRequested = false;

    SharedPreferences sharedpreferences;
    private ArrayList<Feeling> fl, doingList, Indoor, Outdoor;
    private ArrayList<FeelingFeedback> ffList, dfList;
    private ArrayList<String> tipImgList, tipSymName;
    private ArrayList<MetaData> annotations;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_splash);
            apiInterface = APIClient.getClient().create(APIInterface.class);
            getMetadata();
            responseText = (TextView) findViewById(R.id.responseText);

            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {

                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                }, 10);
                getLocation();

            }
            else
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String rText=sharedpreferences.getString("neighborhood","");
                if(!rText.isEmpty())
                responseText.setText(rText+"!");
                else
                    responseText.setText("Stumptown!");
            }
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putParcelableArrayListExtra("feelings_list",fl);
                    i.putParcelableArrayListExtra("activity_list",doingList);
                    i.putParcelableArrayListExtra("indoor_activities", Indoor);
                    i.putParcelableArrayListExtra("outdoor_activities", Outdoor);
                    i.putParcelableArrayListExtra("feeling_feedback_list", ffList);
                    i.putParcelableArrayListExtra("doing_feedback_list", dfList);
                    i.putStringArrayListExtra("tip_img_list", tipImgList);
                    i.putStringArrayListExtra("tip_symptom_list", tipSymName);
                    i.putParcelableArrayListExtra("activity_annotations", annotations);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public void getNeighborhood()
    {
        if(hasRequested == false)
        {

                Call<NeighborhoodData> metadataResponse = apiInterface.neighborhood(latitude,longitude);
                metadataResponse.enqueue(new Callback<NeighborhoodData>() {
                    @Override
                    public void onResponse(Call<NeighborhoodData> call, Response<NeighborhoodData> response) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear().commit();
                        NeighborhoodData resource = response.body();
                        if(resource.getResponseData()!=null) {
                                if (resource.getResponseData().isLocationSupported())
                                    isNhoodSupported = true;
                                if (isNhoodSupported) {
                                nhood = resource.getResponseData().getNeighborhood();
                                editor.putString("neighborhood", nhood);
                                editor.commit();
                            }
                        }
                        editor.putString("isLocationSupported",String.valueOf(isNhoodSupported));
                        editor.putString("latitude", latitude);
                        editor.commit();
                        editor.putString("longitude", longitude);
                        editor.commit();
                    }

                    @Override
                    public void onFailure(Call<NeighborhoodData> call, Throwable t) {
                        call.cancel();
                    }
                });
            }

        hasRequested = true;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=String.valueOf(location.getLatitude());
        longitude=String.valueOf(location.getLongitude());
        if(latitude!=null && longitude !=null) {
            getNeighborhood();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SplashActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void getMetadata() {                     /* Method to fetch feeling images and names using the API and populate the feelings arraylist */
        /*
         GET List Resources
         */
        fl = new ArrayList<>();
        tipImgList = new ArrayList<>();
        tipSymName = new ArrayList<>();
        doingList = new ArrayList<>();
        final ArrayList<Feeling> expandDoingFilters = new ArrayList<>();
        Indoor = new ArrayList<>();
        Outdoor = new ArrayList<>();
        ffList = new ArrayList<>();
        dfList = new ArrayList<>();
        annotations = new ArrayList<>();

        Call<MetaDataResponse> metadataResponse = apiInterface.metadata();
        metadataResponse.enqueue(new Callback<MetaDataResponse>() {
            @Override
            public void onResponse(Call<MetaDataResponse> call, Response<MetaDataResponse> response) {


                MetaDataResponse resource = response.body();
//                TO DO: Add those values to arraylist of Feeling type that have key="feeling"
                ArrayList<MetaData> md = resource.getResponseData().getMetadata();
                int len = md.size();
                for (int i = 0; i < len; i++) {
                    if (md.get(i).getKey().equalsIgnoreCase(getString(R.string.str_feeling))) {                                    //Symptom list that will be used for feeling filters
                        Feeling f = new Feeling(md.get(i).getImgUrl(), md.get(i).getName(), md.get(i).getImgSelUrl());
                        fl.add(f);
                        tipImgList.add(md.get(i).getImgUrl());
                        tipSymName.add(md.get(i).getName());
                    } else if (md.get(i).getKey().equalsIgnoreCase(getString(R.string.str_symptom))) {
                        FeelingFeedback ff = new FeelingFeedback(md.get(i).getImgUrl(), md.get(i).getName());
                        ffList.add(ff);
                    }
                    else if (md.get(i).getKey().equalsIgnoreCase(getString(R.string.activity_group))) {
                        Feeling f = new Feeling(md.get(i).getImgUrl(), md.get(i).getName(), md.get(i).getImgSelUrl());
                        doingList.add(f);
                    }
                    else if (md.get(i).getKey().equalsIgnoreCase(getString(R.string.str_feedback_option))) {
                        FeelingFeedback ff = new FeelingFeedback(md.get(i).getDoingFeedbackUrl(), md.get(i).getName());
                        dfList.add(ff);
                    }
                    else if (md.get(i).getKey().equalsIgnoreCase(getString(R.string.key_activity))) {
                        Feeling f = new Feeling(md.get(i).getImgUrl(), md.get(i).getName(), md.get(i).getImgSelUrl());
                        if (!md.get(i).getName().equalsIgnoreCase("market"))
                            expandDoingFilters.add(f);
                    }
                    else if (md.get(i).getKey().equalsIgnoreCase("annotation")) {
                        annotations.add(md.get(i));
                    }
                }
                for (int i = 0; i < expandDoingFilters.size(); i++) {
                    if (i < 4)
                        Indoor.add(expandDoingFilters.get(i));
                    else
                        Outdoor.add(expandDoingFilters.get(i));
                }
            }

            @Override
            public void onFailure(Call<MetaDataResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

}

