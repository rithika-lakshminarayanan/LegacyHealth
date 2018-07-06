package com.photon.legacyhealth;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.photon.legacyhealth.pojo.CoordinatesFeelings;
import com.photon.legacyhealth.pojo.FeelingTips;
import com.photon.legacyhealth.pojo.FeelingsData;
import com.photon.legacyhealth.pojo.FeelingsResponse;
import com.photon.legacyhealth.pojo.NeighborhoodData;
import com.photon.legacyhealth.pojo.NeighborhoodListDataResponseData;
import com.photon.legacyhealth.pojo.Tips;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeelingMapFragment extends Fragment {
    private GoogleMap feelingMap;
    private double latitude;
    private double longitude;
    private FeelingAdapter mAdapter;
    private ArrayList<NeighborhoodListDataResponseData> allNeighborhoodDetails;
    private APIInterface apiInterface;
    private LatLng home;
    private RecyclerView recyclerView, rViewFeedback;
    private boolean isLocationSupported;
    private FeelingFeedbackAdapter fAdapter;
    private TextView tipBody;
    private ArrayList<FeelingFeedback> ffList;
    private ArrayList<LatLng> coordinatesFeeling;
    private ArrayList<Tips> tip;
    private TextView tipSymptom;
    private ImageView tipImage;
    private Circle selectedCircle;
    private boolean isCircleSelected;
    private AutoCompletePlacesAdapter mSearchableAdapter;
    private int curSymptomPosition;
    private ArrayList<FeelingsResponse> allFeelingsData;
    private ArrayList<CoordinatesFeelings> coordinatesData = new ArrayList<>();
    private boolean isTipFound;
    private ArrayList<String> placesList;
    private RelativeLayout infoBox;
    private boolean dialogBoxOk = false;
    private boolean isNhoodSupported;
    private String nhood;
    private AutoCompleteTextView searchBar;
    private TextView tipHeading;
    private int[] badgeValues = {0, 0, 0};
    private ArrayList<String> tipSymName;
    private ArrayList<String> tipImgList;
    private ArrayList<Integer> noOfCases;
    private boolean isFirstTime = true;
    private ArrayList<Feeling> fl;
    private MapView mMapView;

    public static FeelingMapFragment newInstance(double latitude, double longitude, boolean isLocationSupported, boolean dialogBoxOk, ArrayList<Feeling> fl, ArrayList<FeelingFeedback> ffList, ArrayList<String> tipImgList, ArrayList<String> tipSymName, ArrayList<NeighborhoodListDataResponseData> allNeighborhoodDetails) {
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        bundle.putBoolean("isLocationSupported", isLocationSupported);
        bundle.putBoolean("dialogBoxOk", dialogBoxOk);
        bundle.putParcelableArrayList("feeling_list",fl);
        bundle.putParcelableArrayList("feeling_feedback_list",ffList);
        bundle.putStringArrayList("tip_img_list",tipImgList);
        bundle.putStringArrayList("tip_symptom_list",tipSymName);
        bundle.putParcelableArrayList("all_neighborhood_details", allNeighborhoodDetails);
        FeelingMapFragment fragment = new FeelingMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            latitude = bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");
            isLocationSupported = bundle.getBoolean("isLocationSupported");
            dialogBoxOk = bundle.getBoolean("dialogBoxOk");
            fl = bundle.getParcelableArrayList("feeling_list");
            ffList = bundle.getParcelableArrayList("feeling_feedback_list");
            tipImgList = bundle.getStringArrayList("tip_img_list");
            tipSymName = bundle.getStringArrayList("tip_symptom_list");
            allNeighborhoodDetails = bundle.getParcelableArrayList("all_neighborhood_details");

            home = new LatLng(latitude, longitude);
            isTipFound = false;

            coordinatesFeeling = new ArrayList<>();
            placesList = new ArrayList<>();

            tip = new ArrayList<>();

            selectedCircle = null;
            isCircleSelected = false;

            curSymptomPosition = 2;

            apiInterface = APIClient.getClient().create(APIInterface.class);

            noOfCases = new ArrayList<>();

            getTips();

            mAdapter = new FeelingAdapter(fl);
            mSearchableAdapter = new AutoCompletePlacesAdapter(getContext(), allNeighborhoodDetails);
            for(int i=0; i<allNeighborhoodDetails.size();i++)
                placesList.add(allNeighborhoodDetails.get(i).getNeighborhoodName());
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        readBundle(getArguments());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeling_map_fragment, container, false);
        infoBox = view.findViewById(R.id.info_container_feeling);
        tipBody = view.findViewById(R.id.tip_body);
        tipHeading = view.findViewById(R.id.tip_heading);
        tipImage = view.findViewById(R.id.info_sym_pic);
        tipSymptom = view.findViewById(R.id.tip_sym_name);
        ImageButton tipCrsBtn = view.findViewById(R.id.crsbtn_feeling);
        searchBar = view.findViewById(R.id.zipcode);

        tipCrsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoBox.getVisibility() == View.VISIBLE) {
                    infoBox.setVisibility(View.INVISIBLE);
                }
            }
        });

        searchBar.setAdapter(mSearchableAdapter);

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchableAdapter.getFilter().filter(s.toString());
            }
        });

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof NeighborhoodListDataResponseData) {
                    NeighborhoodListDataResponseData selectedItem = (NeighborhoodListDataResponseData) item;
//                    int selectedPos = placesList.indexOf((((TextView)view.findViewById(R.id.place)).getText()).toString());
                    searchBar.setText(selectedItem.getNeighborhoodName());
//                    ((MainActivity)getActivity()).setNeighborhood(placesList.get(selectedPos));
                    latitude = Double.parseDouble(selectedItem.getLatitude());
                    longitude = Double.parseDouble(selectedItem.getLongitude());
                    performSearch();
                }
            }
        });


        infoBox.setVisibility(View.INVISIBLE);

        recyclerView = view.findViewById(R.id.my_recycler_view_feeling);

        rViewFeedback = view.findViewById(R.id.footer_feedback_feeling);

        mMapView = view.findViewById(R.id.feeling_map); /* Initialising adapter for feeling and doing filters */

        return view;
    }

    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        mMapView.onCreate(savedInstance);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                feelingMap = mMap;
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                feelingMap.setMyLocationEnabled(true);

                feelingMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if(selectedCircle!=null && isCircleSelected){
                            isCircleSelected = false;
                            selectedCircle = null;
                            infoBox.setVisibility(View.INVISIBLE);
                        }
                    }
                });


                float zoom=10;
                if(isLocationSupported) {
                    getFeelings(3);
                    /*String tHead=String.valueOf(noOfCases.get(2)+" "+getString(R.string.tip_heading_const));
                    if(tHead!=null)
                    tipHeading.setText(tHead);*/
//                    infoBox.setVisibility(View.VISIBLE);
                }
                feelingMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
            }
        });
    }

    /* Function to get text from search bar once search button is pressed and call the required API*/
    private void performSearch() {
        searchBar.clearFocus();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        String searchText = searchBar.getText().toString();
        if(curSymptomPosition!=2) {
            mAdapter.changeImage(curSymptomPosition);
            mAdapter.changeImage(2);
        }
        Log.d("from_search_bar", searchText);
        if(isNumber(searchText))
            getFeelingsByZipcode(searchText,3);
            else
                getFeelings(3);

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener((new FeelingAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                GlideApp.with(infoBox).load(tipImgList.get(position)).fitCenter().into(tipImage);
                tipImage.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                tipSymptom.setText(tipSymName.get(position));
                String tHead = String.valueOf(noOfCases.get(position)) + " " + getString(R.string.tip_heading_const);
                tipHeading.setText(tHead);
                if (tip != null) {
                    for (int i = 0; i < tip.size(); i++) {
                        if (tip.get(i).getFeelings().get(0) == position + 1) {
                            tipBody.setText(tip.get(i).getMessage());
                            isTipFound = true;
                            break;
                        }
                    }
                }
                if (!isTipFound)
                    tipBody.setText(" ");
                if (curSymptomPosition == position) {
                    mAdapter.changeImage(position);
                    feelingMap.clear();
                    if (!fl.get(position).isImageHighlighted()) {
                        addFeelingMarkersToMap(position + 1);
                        fl.get(position).setImageHighlighted(true);
                        infoBox.setVisibility(View.VISIBLE);
                    } else {
                        infoBox.setVisibility(View.INVISIBLE);
                        fl.get(position).setImageHighlighted(false);
                    }
                } else {
                    curSymptomPosition = position;
                    mAdapter.changeImage(position);
                    addFeelingMarkersToMap(position + 1);
                    fl.get(position).setImageHighlighted(true);
                    infoBox.setVisibility(View.VISIBLE);
                }
                isTipFound = false;
            }
        }));

        fAdapter = new FeelingFeedbackAdapter(ffList);

        RecyclerView.LayoutManager fLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rViewFeedback.setLayoutManager(fLayoutManager);
        rViewFeedback.setAdapter(fAdapter);
        fAdapter.notifyDataSetChanged();

        fAdapter.setOnItemClickListener(new FeelingFeedbackAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                fAdapter.increaseBadgeCount(position, badgeValues[position]);
                badgeValues[position]++;
            }
        });

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        rViewFeedback.setAdapter(fAdapter);
        fAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    public void getFeelingsByZipcode(String zipCode, final int feelingId){
        isFirstTime = true;
        Call<FeelingsData> zipCodeResponse = apiInterface.zipcode_feelings(zipCode,"feeling");
        zipCodeResponse.enqueue(new Callback<FeelingsData>() {
            @Override
            public void onResponse(Call<FeelingsData> call, Response<FeelingsData> response) {
                FeelingsData resource = response.body();
                if(resource!=null){
                    if(resource.getResponseData().isZipCodeSupported()) {
                        noOfCases.clear();
                        feelingMap.clear();
                        tip.clear();
                        if (!coordinatesData.isEmpty())
                            coordinatesData.clear();
                        if (!coordinatesFeeling.isEmpty())
                            coordinatesFeeling.clear();
                        allFeelingsData = resource.getResponseData().getFeelings();
                        addFeelingMarkersToMap(feelingId);
                        latitude = Double.parseDouble(resource.getResponseData().getLatitude());
                        longitude = Double.parseDouble(resource.getResponseData().getLongitude());
                        feelingMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
                        getNeighborhood();
                        ((MainActivity) getActivity()).setNeighborhood(nhood);
                        isFirstTime = false;
                    }
                    else {
                        new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                                .setMessage(R.string.out_of_service_area)                          /* If user is not in supported neighbourhood, give them option to check the supported neighbourhood */
                                .setPositiveButton(R.string.okay_dialog_box, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FeelingsData> call, Throwable t) {
                call.cancel();
            }
        });
    }


    public void getFeelings(final int position) {

        if (isLocationSupported || dialogBoxOk) {
            Call<FeelingsData> feelingsResponse = apiInterface.feelings(String.valueOf(latitude), String.valueOf(longitude), position);
            feelingsResponse.enqueue(new Callback<FeelingsData>() {
                @Override
                public void onResponse(Call<FeelingsData> call, Response<FeelingsData> response) {
                    FeelingsData resource = response.body();
                    if(resource!=null) {
                        if(!coordinatesData.isEmpty())
                            coordinatesData.clear();
                        if(!coordinatesFeeling.isEmpty())
                            coordinatesFeeling.clear();
                        allFeelingsData = resource.getResponseData().getFeelings();
                        feelingMap.clear();
                        addFeelingMarkersToMap(position);
                        isFirstTime = false;
                    }
                }

                @Override
                public void onFailure(Call<FeelingsData> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    public void getTips(){
        Call<FeelingTips> TipResponse=apiInterface.feelingTips();
        TipResponse.enqueue(new Callback<FeelingTips>() {
            @Override
            public void onResponse(Call<FeelingTips> call, Response<FeelingTips> response) {                                                   /* Method to retrieve tips from API using Retrofit 2 for REST framework*/
                FeelingTips resource=response.body();
                tip=resource.getResponseData().getTips();
            }

            @Override
            public void onFailure(Call<FeelingTips> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void addFeelingMarkersToMap(int position){
        if(!coordinatesData.isEmpty())
            coordinatesData.clear();
        if(!coordinatesFeeling.isEmpty())
            coordinatesFeeling.clear();
        if (allFeelingsData != null) {
            for (int j = 0; j < allFeelingsData.get(position-1).getCoordinates().size(); j++) {
                coordinatesData.addAll(allFeelingsData.get(position-1).getCoordinates());
            }

            if(isFirstTime) {
                for (int i = 0; i < allFeelingsData.size(); i++) {
                    noOfCases.add(allFeelingsData.get(i).getCoordinates().size());
                }
            }

            for (int k = 0; k < coordinatesData.size(); k++) {
                double latitude=Float.parseFloat(coordinatesData.get(k).getLat());
                double longitude=Float.parseFloat(coordinatesData.get(k).getLon());
                coordinatesFeeling.add(new LatLng(latitude,longitude));
            }
            for(int i=0;i<coordinatesFeeling.size();i++) {
                feelingMap.addCircle(new CircleOptions()
                        .center(coordinatesFeeling.get(i))
                        .radius(100)
                        .strokeWidth(10)
                        .strokeColor(Color.TRANSPARENT)
                        .fillColor(Color.TRANSPARENT)
                        .clickable(true));
                feelingMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

                    @Override
                    public void onCircleClick(Circle circle) {
                        if(infoBox.getVisibility()== View.INVISIBLE)
                            infoBox.setVisibility(View.VISIBLE);
                        selectedCircle = circle;
                        isCircleSelected = true;
                    }
                });
            }

            int[] colors = {
                    Color.argb(160,0,149,164)
            };

            float[] startPoints = {
                    0.009f
            };

            Gradient gradient = new Gradient(colors, startPoints);

// Create the tile provider.

            HeatmapTileProvider provider;
            if(!coordinatesFeeling.isEmpty()) {
                provider = new HeatmapTileProvider.Builder().data(coordinatesFeeling).gradient(gradient).build();
                provider.setRadius(30);
                // Add a tile overlay to the map, using the heat map tile provider.
                feelingMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
            }
        }

    }

    public void getNeighborhood()
    {
            Call<NeighborhoodData> metadataResponse = apiInterface.neighborhood(String.valueOf(latitude),String.valueOf(longitude));
            metadataResponse.enqueue(new Callback<NeighborhoodData>() {
                @Override
                public void onResponse(Call<NeighborhoodData> call, Response<NeighborhoodData> response) {
                    NeighborhoodData resource = response.body();
                    if(resource.getResponseData()!=null) {
                        if (resource.getResponseData().isLocationSupported())
                            isNhoodSupported = true;
                        if (isNhoodSupported) {
                            nhood = resource.getResponseData().getNeighborhood();
                        }
                    }
                }

                @Override
                public void onFailure(Call<NeighborhoodData> call, Throwable t) {
                    call.cancel();
                }
            });
    }

    public boolean isNumber(String s)
    {
        for (int i = 0; i < s.length(); i++)
            if (!Character.isDigit(s.charAt(i)))
                return false;

        return true;
    }


}
