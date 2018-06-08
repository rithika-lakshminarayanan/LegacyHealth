package com.photon.legacyhealth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.photon.legacyhealth.pojo.NeighborhoodData;

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
    private boolean hasrequested = false;

    SharedPreferences sharedpreferences;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            apiInterface = APIClient.getClient().create(APIInterface.class);
            responseText = (TextView) findViewById(R.id.responseText);

            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {

                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                }, 10);

            }
            else
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                Log.d("Test123","Testing 123");
                String rText=sharedpreferences.getString("neighborhood","");
                if(!rText.isEmpty())
                responseText.setText(rText+"!");
                else
                    responseText.setText("Stumptown!");
            }
//            Log.d("Test223","Testing 223");
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
                    if(isNhoodSupported)
                    i.putExtra("isLocationSupported","true");
                    else
                        i.putExtra("isLocationSupported","false");
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

    void getLocation() {
//        Log.d("Test323","Testing 323");
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
        if(hasrequested == false)
        {

                Call<NeighborhoodData> metadataResponse = apiInterface.neighborhood(latitude,longitude);
                metadataResponse.enqueue(new Callback<NeighborhoodData>() {
                    @Override
                    public void onResponse(Call<NeighborhoodData> call, Response<NeighborhoodData> response) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        NeighborhoodData resource = response.body();
                        if(resource!=null) {
                                if (resource.getResponseData().isLocationSupported())
                                    isNhoodSupported = true;
                                if (isNhoodSupported) {
                                nhood = resource.getResponseData().getNeighborhood();
                                editor.putString("neighborhood", nhood);
                                editor.commit();
                            }
                        }
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

        hasrequested = true;
    }

    @Override
    public void onLocationChanged(Location location) {
        /*Log.d("Test423","Testing 423");
        Log.d("Location:","My location is "+String.valueOf(location.getLatitude())+" "+String.valueOf(location.getLongitude()));*/
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
}

