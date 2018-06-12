package com.photon.legacyhealth;


import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.photon.legacyhealth.pojo.CoordinatesFeelings;
import com.photon.legacyhealth.pojo.FeelingTips;
import com.photon.legacyhealth.pojo.FeelingsData;
import com.photon.legacyhealth.pojo.FeelingsResponse;
import com.photon.legacyhealth.pojo.MetaData;
import com.photon.legacyhealth.pojo.MetaDataResponse;
import com.photon.legacyhealth.pojo.Tips;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    SharedPreferences sharedpreferences;
    private RecyclerView recyclerView;
    private RecyclerView rViewFeedback;
    private FeelingAdapter mAdapter;
    private FeelingFeedbackAdapter fAdapter;
    private FeelingFeedback ff;
    private ArrayList<MetaData> md;
    private ArrayList<FeelingFeedback> ffList;
    private ArrayList<LatLng> coordinatesFeeling;
    private Tips tip;
    private int curSymptomPosition;
    private GoogleMap mMap;
    private String tabPressed;
    private ArrayList<FeelingsResponse> allFeelingsData;
    private ArrayList<CoordinatesFeelings> coordinatesData = new ArrayList<>();
    private double latitude;
    private TextView infoBox;
    private ImageButton crsBtn;
    private double longitude;
    private boolean dialogBoxOk=false;
    private boolean hasDialogBoxOpened=false;
    private boolean isLocationSupported=true;
    private APIInterface apiInterface;
    private boolean isFirstFragment=true;
    private TwoFragment secondFragment;
    private ArrayList<Feeling> fl;
    Feeling f;
    public static final String MyPREFERENCES = "MyPrefs" ;
    PresetRadioGroup mSetDurationPresetRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title= (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        coordinatesFeeling=new ArrayList<>();
        curSymptomPosition=2;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String rText;
        if(isLocationSupported) {
            rText = sharedpreferences.getString("neighborhood", "");
        }
        else
            rText="Stumptown";
        latitude=Float.parseFloat(sharedpreferences.getString("latitude",""));
        longitude=Float.parseFloat(sharedpreferences.getString("longitude",""));
        title.setText(rText);

        infoBox=findViewById(R.id.info_box);
        crsBtn=findViewById(R.id.cross_btn);
        infoBox.setVisibility(View.GONE);
        crsBtn.setVisibility(View.GONE);
        crsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                infoBox.setVisibility(View.GONE);
                crsBtn.setVisibility(View.GONE);

            }
        });
        Intent myIntent = getIntent(); // gets the previously created intent
        if(!myIntent.getStringExtra("isLocationSupported").equalsIgnoreCase("false")){
            isLocationSupported=false;
            hasDialogBoxOpened=true;
            new AlertDialog.Builder(this,R.style.MyDialogTheme)
                    .setMessage("Oh no! You are too far away. Want to check out our neighborhood?")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogBoxOk=true;
                            setTitle("Portland");
                            LatLng home = new LatLng(45.5190,-122.5180);
                            float zoom=11;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
                            getFeelings(3);
                            Log.d("MainActivity", "Clicked okay");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogBoxOk=false;
                            Log.d("MainActivity", "Aborting mission...");
                        }
                    })
                    .show();
        }
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        recyclerView=(RecyclerView)findViewById(R.id.my_recycler_view) ;
        recyclerView.setHasFixedSize(true);
//        recyclerView.setVisibility(View.VISIBLE);

        rViewFeedback=findViewById(R.id.feeling_feedback);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        ffList=new ArrayList<>();
        fl=new ArrayList<>();

        populateFeelingsList();

        mAdapter = new FeelingAdapter(fl);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener((new FeelingAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i("ItemClicked", " Clicked on Item " + position);
                if(curSymptomPosition==position) {
                    mAdapter.changeImage(position);
                    mMap.clear();
                }
                else {
                    curSymptomPosition = position;
                    mAdapter.changeImage(position);
                    mMap.clear();
                    getFeelings(position + 1);
                }
//                getTip(position+1);
               /*infoBox.setVisibility(View.VISIBLE);
               crsBtn.setVisibility(View.VISIBLE);*/

            }
        }));

        fAdapter=new FeelingFeedbackAdapter(ffList);
        RecyclerView.LayoutManager fLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rViewFeedback.setLayoutManager(fLayoutManager);
        rViewFeedback.setAdapter(fAdapter);
        fAdapter.notifyDataSetChanged();
//

        secondFragment = new TwoFragment();


        final SupportMapFragment mapFragment =  SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        if (findViewById(R.id.map) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            // Add the fragment to the 'fragment_container' FrameLayout
           /* getSupportFragmentManager().beginTransaction()
                    .add(R.id.map, firstFragment).commit();*/
        }


        mSetDurationPresetRadioGroup = (PresetRadioGroup) findViewById(R.id.preset_time_radio_group);
        mSetDurationPresetRadioGroup.setOnCheckedChangeListener(new PresetRadioGroup.OnCheckedChangeListener() {      /* Method to change fragments on pressing different tabs */
            @Override
            public void onCheckedChanged(View radioGroup, View radioButton, boolean isChecked, int checkedId) {
                tabPressed=((PresetValueButton) radioButton).getValue();
                if (tabPressed.equalsIgnoreCase("Doing")) {
                    if (isFirstFragment) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
                        transaction.replace(R.id.map,secondFragment);
                        transaction.addToBackStack(null);
// Commit the transaction
                        transaction.commit();
                        isFirstFragment=false;
                    }
                }
                else
                if (tabPressed.equalsIgnoreCase("Feeling")) {
                    if (!isFirstFragment) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
                        transaction.replace(R.id.map, mapFragment);
                        transaction.addToBackStack(null);

// Commit the transaction
                        transaction.commit();
                        isFirstFragment=true;
                    }
                }
            }
        });
    }

    public void populateFeelingsList(){                     /* Method to fetch feeling images and names using the API and populate the feelings arraylist */
        /*
         GET List Resources
         */

        Call<MetaDataResponse> metadataResponse = apiInterface.metadata();
        metadataResponse.enqueue(new Callback<MetaDataResponse>() {
            @Override
            public void onResponse(Call<MetaDataResponse> call, Response<MetaDataResponse> response) {


                MetaDataResponse resource = response.body();
//                TO DO: Add those values to arraylist of Feeling type that have key="feeling"
                md=resource.getResponseData().getMetadata();
                int len=md.size();
                for(int i=0;i<len;i++){
                    if(md.get(i).getKey().equalsIgnoreCase("feeling")) {
                        f = new Feeling(md.get(i).getImgUrl(), md.get(i).getName(), md.get(i).getImgSelUrl());
                        fl.add(f);
                    }
                    else
                        if(md.get(i).getKey().equalsIgnoreCase("symptom")){
                          ff = new FeelingFeedback(md.get(i).getImgUrl(),md.get(i).getName());
                          ffList.add(ff);
                        }
                }
                int flen=fl.size();
            }

            @Override
            public void onFailure(Call<MetaDataResponse> call, Throwable t) {
                call.cancel();
            }
        });
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng home=new LatLng(latitude, longitude);
        float zoom=10;
        if(!hasDialogBoxOpened) {
            getFeelings(3);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
    }

    public void getFeelings(final int position) {

        if (isLocationSupported) {
            if(!coordinatesData.isEmpty())
                coordinatesData.clear();
            if(!coordinatesFeeling.isEmpty())
                coordinatesFeeling.clear();
            Call<FeelingsData> feelingsResponse = apiInterface.feelings(String.valueOf(latitude), String.valueOf(longitude), position);
            feelingsResponse.enqueue(new Callback<FeelingsData>() {
                @Override
                public void onResponse(Call<FeelingsData> call, Response<FeelingsData> response) {
                    FeelingsData resource = response.body();
                    if(resource!=null) {
                        allFeelingsData = resource.getResponseData().getFeelings();
                        if (allFeelingsData != null) {
                            int alLen = allFeelingsData.size();
                                for (int j = 0; j < allFeelingsData.get(position-1).getCoordinates().size(); j++) {
                                    coordinatesData.add(allFeelingsData.get(position-1).getCoordinates().get(j));
                                }




                           for (int k = 0; k < coordinatesData.size(); k++) {
                             double latitude=Float.parseFloat(coordinatesData.get(k).getLat());
                             double longitude=Float.parseFloat(coordinatesData.get(k).getLon());
                             coordinatesFeeling.add(new LatLng(latitude,longitude));
                            }

                            int[] colors = {
                                    Color.argb(160,0,149,164)    /*.rgb(0, 149, 164), // blue*/
                            };

                            float[] startPoints = {
                                    0.009f
                            };

                            Gradient gradient = new Gradient(colors, startPoints);

// Create the tile provider.

                            HeatmapTileProvider provider;
                            provider = new HeatmapTileProvider.Builder().data(coordinatesFeeling).gradient(gradient).build();
                            provider.setRadius(30);
                            // Add a tile overlay to the map, using the heat map tile provider.
                            mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
                        }
                    }
                }

                @Override
                public void onFailure(Call<FeelingsData> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

/*    public void getTip(int feelingId){
        Call<FeelingTips> TipResponse=apiInterface.feelingTips(feelingId);
        TipResponse.enqueue(new Callback<FeelingTips>() {
            @Override
            public void onResponse(Call<FeelingTips> call, Response<FeelingTips> response) {
                FeelingTips resource=response.body();
                tip=resource.getResponseData().getTips().get(0);
            }

            @Override
            public void onFailure(Call<FeelingTips> call, Throwable t) {
                call.cancel();
            }
        });
    }*/
}
