package com.photon.legacyhealth;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.photon.legacyhealth.pojo.ActivityData;
import com.photon.legacyhealth.pojo.ActivityDetails;
import com.photon.legacyhealth.pojo.ActivityGroup;
import com.photon.legacyhealth.pojo.MetaData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class DoingMapFragment extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap doingMap;
    private double latitude;
    private double longitude;
    private APIInterface apiInterface;
    private LatLng home;
    private ArrayList<ActivityDetails> climbings, gyms, pilates, yogas, parks, markets;
    private ArrayList<Marker> marketMarkers, climbingMarkers, pilatesMarkers, yogaMarkers, runningMarkers, walkingMarkers, parkMarkers, gymMarkers;
    private ArrayList<MetaData> annotations;
    private boolean isMarkerSelected = false;
    private Marker selectedMarker;
    private RecyclerView rViewFeedback, recyclerView;
    private ArrayList<Feeling> Outdoor, Indoor;
    private ArrayList<FeelingFeedback> dfList;
    private ArrayList<Feeling> doingList;
    private boolean isIndoorExpanded, isOutdoorExpanded;
    private ArrayList<DoingMarkerDetails> activityDetails;
    private ArrayList<ActivityGroup> activityGroups;
    private Marker prevMarker;
    private ArrayList<LatLng> runningLocations, walkingLocations;
    private ArrayList<String> runningTags, walkingTags;
    private DoingAdapter dAdapter;
    private int curActivityPosition;
    private ArrayList<String> annotationsList;
    private RelativeLayout rl,rl2;
    private ArrayList<String> walkingDates, runningDates;
    private int[] doingBadgeValues = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private AutoCompleteTextView searchBar;
    private AutoCompleteActivitiesAdapter mSearchableAdapter;

    public static DoingMapFragment newInstance(Double latitude, Double longitude, ArrayList<Feeling> doingList, ArrayList<Feeling> Indoor, ArrayList<Feeling> Outdoor, ArrayList<FeelingFeedback> dfList, ArrayList<MetaData> annotations) {
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        bundle.putParcelableArrayList("activity_list",doingList);
        bundle.putParcelableArrayList("indoor_activities_list", Indoor);
        bundle.putParcelableArrayList("outdoor_activities_list", Outdoor);
        bundle.putParcelableArrayList("doing_feedback_list", dfList);
        bundle.putParcelableArrayList("activity_annotations", annotations);
        DoingMapFragment fragment = new DoingMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            latitude = bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");
            doingList = bundle.getParcelableArrayList("activity_list");
            Indoor = bundle.getParcelableArrayList("indoor_activities_list");
            Outdoor = bundle.getParcelableArrayList("outdoor_activities_list");
            dfList = bundle.getParcelableArrayList("doing_feedback_list");
            annotations = bundle.getParcelableArrayList("activity_annotations");

            home = new LatLng(latitude, longitude);
            curActivityPosition = -1;
            climbingMarkers = new ArrayList<>();
            parkMarkers = new ArrayList<>();
            yogaMarkers = new ArrayList<>();
            marketMarkers = new ArrayList<>();
            gymMarkers = new ArrayList<>();
            pilatesMarkers = new ArrayList<>();
            runningMarkers = new ArrayList<>();
            walkingMarkers = new ArrayList<>();
            selectedMarker = null;

            walkingDates = new ArrayList<>();
            runningDates = new ArrayList<>();

            apiInterface = APIClient.getClient().create(APIInterface.class);

            activityDetails = new ArrayList<>();                                               //ArrayList to hold details of every activity such as marker image, locations, tags required

            walkingLocations = new ArrayList<>();
            walkingTags = new ArrayList<>();
            runningLocations = new ArrayList<>();
            runningTags = new ArrayList<>();

            isIndoorExpanded = false;
            isOutdoorExpanded = false;

            getActivityDetails();

            annotationsList = new ArrayList<>();
            for(int i=0; i<annotations.size();i++)
                annotationsList.add(annotations.get(i).getName());
            mSearchableAdapter = new AutoCompleteActivitiesAdapter(getContext(), annotationsList);

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
        View view = inflater.inflate(R.layout.doing_map_fragment, container, false);
        rl2 = view.findViewById(R.id.event_details);
        rl2.setVisibility(View.INVISIBLE);

        rl = view.findViewById(R.id.doing_feedback_lyt);

        searchBar = view.findViewById(R.id.zipcode);
        searchBar.setAdapter(mSearchableAdapter);

        MapView mMapView =  view.findViewById(R.id.doing_map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                doingMap = mMap;

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
                doingMap.setMyLocationEnabled(true);
                float zoom = 10;
                doingMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
                doingMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if(selectedMarker!=null && isMarkerSelected){
                            addCustomActivityMarkerToMap(selectedMarker.getPosition(),selectedMarker.getTitle(),String.valueOf(selectedMarker.getTag()),false, removeMarkerFromArrayList(selectedMarker),selectedMarker.getSnippet());
                            selectedMarker = null;
                            isMarkerSelected = false;
                            rl2.setVisibility(View.INVISIBLE);
                            rl.setVisibility(View.VISIBLE);
                        }
                    }
                });
                doingMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override public boolean onMarkerClick(Marker marker) {
                        TextView feedbackQuestion = getView().findViewById(R.id.event_name);
                        TextView address = getView().findViewById(R.id.date_or_address);
                        //  Take some action here
                        if (prevMarker != null) {
                            //Set prevMarker back to default color
                            addCustomActivityMarkerToMap(prevMarker.getPosition(),prevMarker.getTitle(),String.valueOf(prevMarker.getTag()),false, removeMarkerFromArrayList(prevMarker),prevMarker.getSnippet());
                            selectedMarker = null;
                            isMarkerSelected = false;
                            rl2.setVisibility(View.INVISIBLE);
                            rl.setVisibility(View.VISIBLE);
                        }

                        //leave Marker default color if re-click current Marker
                        if(prevMarker==null) {
                            addCustomActivityMarkerToMap(marker.getPosition(), marker.getTitle(), String.valueOf(marker.getTag()), true, removeMarkerFromArrayList(marker),marker.getSnippet());
                            selectedMarker = marker;
                            isMarkerSelected = true;
                            feedbackQuestion.setText(marker.getTitle());
                            address.setText(marker.getSnippet());
                            if(marker.getSnippet().length()<15){
                                ImageView img = getView().findViewById(R.id.event_icon);
                                TextView tView = getView().findViewById(R.id.detail_name);
                                img.setImageResource(R.drawable.calendar_white);
                                tView.setText(R.string.event_details_date);
                            }
                            else{
                                ImageView img = getView().findViewById(R.id.event_icon);
                                TextView tView = getView().findViewById(R.id.detail_name);
                                img.setImageResource(R.drawable.address_pin);
                                tView.setText(R.string.address);
                            }
                            rl.setVisibility(View.INVISIBLE);
                            rl2.setVisibility(View.VISIBLE);
                        }
                        else
                        if (!marker.equals(prevMarker)) {
                            addCustomActivityMarkerToMap(marker.getPosition(),marker.getTitle(),String.valueOf(marker.getTag()),true, removeMarkerFromArrayList(marker), marker.getSnippet());
                            selectedMarker = marker;
                            isMarkerSelected = true;
                            feedbackQuestion.setText(marker.getTitle());
                            address.setText(marker.getSnippet());
                            if(marker.getSnippet().length()<15){
                                ImageView img = getView().findViewById(R.id.event_icon);
                                TextView tView = getView().findViewById(R.id.detail_name);
                                img.setImageResource(R.drawable.calendar_white);
                                tView.setText("DATE");
                            }
                            else{
                                ImageView img = getView().findViewById(R.id.event_icon);
                                TextView tView = getView().findViewById(R.id.detail_name);
                                img.setImageResource(R.drawable.address_pin);
                                tView.setText("ADDRESS");
                            }
                            rl.setVisibility(View.INVISIBLE);
                            rl2.setVisibility(View.VISIBLE);
                        }
                        prevMarker = marker;
                        return true;
                    }

                });
            }
        });

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

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedPos = annotationsList.indexOf((((TextView)view.findViewById(R.id.activity_name)).getText()).toString());
                String searchText = annotationsList.get(selectedPos);
                searchBar.setText(searchText);
                performSearch();
            }
        });

        // Setup any handles to view objects here

        recyclerView = view.findViewById(R.id.my_recycler_view_doing);

        rViewFeedback = view.findViewById(R.id.footer_feedback_doing);


        return view;
    }

    private void performSearch() {
        searchBar.clearFocus();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        String searchText = searchBar.getText().toString();
        if(isNumber(searchText))
            getActivitesByZipcode(searchText);
        else
        {
            if(searchText.equalsIgnoreCase("running"))
                searchText = "races";
                        else
                            if (searchText.equalsIgnoreCase("farmer's market"))
                                searchText = "market";
                        checkCategory(searchText);
        }
    }

    private void checkCategory(String searchText) {
        boolean found = false;

        for(int i=0; i<Indoor.size();i++){
            Indoor.get(i).setImageChanged(false);
        }
        for(int j=0; j<Outdoor.size();j++){
            Outdoor.get(j).setImageChanged(false);
        }
        for(int i=0; i<doingList.size(); i++){
            doingList.get(i).setImageChanged(false);
        }
        if (isOutdoorExpanded) {
            dAdapter.removeItemsOnUnselect(Outdoor, dAdapter.getPosition("outdoor") - 1);
            isOutdoorExpanded = false;
        }
        else
        if (isIndoorExpanded) {
            dAdapter.removeItemsOnUnselect(Indoor, dAdapter.getPosition("indoor") - 1);
            isIndoorExpanded = false;
        }

        for(int k = 0; k < Indoor.size() ; k++) {
            if (Indoor.get(k).getSymptom_name().equalsIgnoreCase(searchText)) {
                doingMap.clear();
                Indoor.get(k).setImageChanged(true);
                dAdapter.addItemsOnFilter(Indoor,dAdapter.getPosition("indoor"));
                isIndoorExpanded = true;
                for(int i=0;i<activityDetails.size();i++){
                    if(activityDetails.get(i).getActivityName().equalsIgnoreCase(searchText)){
                        for(int j=0;j<activityDetails.get(i).getActivityLocations().size();j++){
                            addCustomActivityMarkerToMap(activityDetails.get(i).getActivityLocations().get(j),activityDetails.get(i).getTags().get(j) ,activityDetails.get(i).getMarkerImgUrl(),false, getMarkerListForActivity(searchText),activityDetails.get(i).getActivityAddresses().get(j));
                        }
                        break;
                    }
                }
                found = true;
                break;
            }
        }
            if(!found){
                for(int k = 0; k < Outdoor.size() ; k++) {
                    if (Outdoor.get(k).getSymptom_name().equalsIgnoreCase(searchText)) {
                        Outdoor.get(k).setImageChanged(true);
                        doingMap.clear();
                        dAdapter.addItemsOnFilter(Outdoor, dAdapter.getPosition("outdoor"));
                        isOutdoorExpanded = true;
                        if(searchText.equalsIgnoreCase("races"))
                            searchText = "running";
                        else if(searchText.equalsIgnoreCase("trails"))
                            searchText = "walking";
                        for(int i=0;i<activityDetails.size();i++){
                            if(activityDetails.get(i).getActivityName().equalsIgnoreCase(searchText)){
                                for(int j=0;j<activityDetails.get(i).getActivityLocations().size();j++){
                                    addCustomActivityMarkerToMap(activityDetails.get(i).getActivityLocations().get(j),activityDetails.get(i).getTags().get(j) ,activityDetails.get(i).getMarkerImgUrl(),false, getMarkerListForActivity(searchText),activityDetails.get(i).getActivityAddresses().get(j));
                                }
                                break;
                            }
                        }
                        found = true;
                        break;
                    }
                }
                if(!found){
                    for(int k=0; k<doingList.size();k++){
                        if(doingList.get(k).getSymptom_name().equalsIgnoreCase("market")) {
                            doingList.get(k).setImageChanged(true);
                            doingMap.clear();
                            for(int i=0;i<activityDetails.size();i++){
                                if(activityDetails.get(i).getActivityName().equalsIgnoreCase(dAdapter.getActivityName(k))){
                                    for(int j=0;j<activityDetails.get(i).getActivityLocations().size();j++){
                                        addCustomActivityMarkerToMap(activityDetails.get(i).getActivityLocations().get(j),activityDetails.get(i).getTags().get(j) ,activityDetails.get(i).getMarkerImgUrl(),false, getMarkerListForActivity(dAdapter.getActivityName(k)),activityDetails.get(i).getActivityAddresses().get(j));
                                    }
                                    break;
                                }
                            }
                        }

                    }
                }

            }
            dAdapter.notifyDataSetChanged();
    }

    private void getActivitesByZipcode(String searchText) {
        Call<ActivityData> activityDataCall = apiInterface.zipcode_activities(searchText,"doing");
        activityDataCall.enqueue(new Callback<ActivityData>() {
            @Override
            public void onResponse(Call<ActivityData> call, Response<ActivityData> response) {
                ActivityData resource = response.body();
                if(resource!=null) {
                    if(resource.getResponseData().isZipCodeSupported()) {
                        doingMap.clear();
                        for(int i=0; i<Indoor.size();i++){
                            Indoor.get(i).setImageChanged(false);
                        }
                        for(int j=0; j<Outdoor.size();j++){
                            Indoor.get(j).setImageChanged(false);
                        }
                        if (isOutdoorExpanded) {
                            dAdapter.removeItemsOnUnselect(Outdoor, dAdapter.getPosition("outdoor") - 1);
                            isOutdoorExpanded = false;
                            dAdapter.unselectImage(dAdapter.getPosition("outdoor"));
                        }
                        else
                        if (isIndoorExpanded) {
                            dAdapter.removeItemsOnUnselect(Indoor, dAdapter.getPosition("indoor") - 1);
                            isIndoorExpanded = false;
                            dAdapter.unselectImage(dAdapter.getPosition("indoor"));
                        }
                        curActivityPosition = -1;

                        activityGroups = resource.getResponseData().getActivityGroups();
                        activityDetails.clear();
                        populateActivitiesList(activityGroups);
                        latitude = Double.parseDouble(resource.getResponseData().getLatitude());
                        longitude = Double.parseDouble(resource.getResponseData().getLongitude());
                        doingMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
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
            public void onFailure(Call<ActivityData> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dAdapter = new DoingAdapter(doingList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(dAdapter);
        dAdapter.notifyDataSetChanged();

        dAdapter.setOnItemClickListener((new DoingAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                dAdapter.changeImage(position);
                if (curActivityPosition == position) {
                    if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_indoor)) || dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_outdoor))) {
                        if (dAdapter.getItemCount() > 3) {
                            if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_indoor))) {
                                dAdapter.removeItemsOnUnselect(Indoor, dAdapter.getPosition("indoor") - 1);
                                isIndoorExpanded = false;
                            } else if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_outdoor))) {
                                dAdapter.removeItemsOnUnselect(Outdoor, dAdapter.getPosition("outdoor") - 1);
                                isOutdoorExpanded = false;
                            }
                        } else {
                            if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_indoor))) {
                                dAdapter.addItemsOnFilter(Indoor, dAdapter.getPosition("indoor"));
                                isIndoorExpanded = true;
                            } else if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_outdoor))) {
                                dAdapter.addItemsOnFilter(Outdoor, dAdapter.getPosition("outdoor"));
                                isOutdoorExpanded = true;
                            }
                        }
                    }
                    else
                    if(dAdapter.isFilterSelected(position)){
                        for(int i=0;i<activityDetails.size();i++){
                            if(activityDetails.get(i).getActivityName().equalsIgnoreCase(dAdapter.getActivityName(position))){
                                for(int j=0;j<activityDetails.get(i).getActivityLocations().size();j++){
                                    addCustomActivityMarkerToMap(activityDetails.get(i).getActivityLocations().get(j),activityDetails.get(i).getTags().get(j) ,activityDetails.get(i).getMarkerImgUrl(),false, getMarkerListForActivity(dAdapter.getActivityName(position)),activityDetails.get(i).getActivityAddresses().get(j));
                                }
                                break;
                            }
                        }
                    }
                    else
                    if(!dAdapter.isFilterSelected(position)){
                        if(!getMarkerListForActivity(dAdapter.getActivityName(position)).isEmpty()){
                            for(int i=0; i<getMarkerListForActivity(dAdapter.getActivityName(position)).size();i++){
                                getMarkerListForActivity(dAdapter.getActivityName(position)).get(i).remove();
                                rl2.setVisibility(View.INVISIBLE);
                                rl.setVisibility(View.VISIBLE);
                            }
                            getMarkerListForActivity(dAdapter.getActivityName(position)).clear();
                        }
                    }
                } else {
                    if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_indoor)) || dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_outdoor))) {
                        if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_indoor))) {
                            if (!isIndoorExpanded) {
                                dAdapter.addItemsOnFilter(Indoor, dAdapter.getPosition("indoor"));
                                isIndoorExpanded = true;
                            } else {
                                dAdapter.removeItemsOnUnselect(Indoor, dAdapter.getPosition("indoor") - 1);
                                isIndoorExpanded = false;
                            }
                            if (isOutdoorExpanded) {
                                dAdapter.removeItemsOnUnselect(Outdoor, dAdapter.getPosition("outdoor") - 1);
                                isOutdoorExpanded = false;
                                dAdapter.unselectImage(dAdapter.getPosition("outdoor"));
                            }
                            position = dAdapter.getPosition(getString(R.string.activity_group_indoor));
                        } else if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_outdoor))) {
                            if (!isOutdoorExpanded) {
                                dAdapter.addItemsOnFilter(Outdoor, dAdapter.getPosition("outdoor"));
                                isOutdoorExpanded = true;
                            } else {
                                dAdapter.removeItemsOnUnselect(Outdoor, dAdapter.getPosition("outdoor") - 1);
                                isOutdoorExpanded = false;
                            }
                            if (isIndoorExpanded) {
                                dAdapter.removeItemsOnUnselect(Indoor, dAdapter.getPosition("indoor") - 1);
                                isIndoorExpanded = false;
                                dAdapter.unselectImage(dAdapter.getPosition("indoor"));
                            }
                            position = dAdapter.getPosition(getString(R.string.activity_group_outdoor));
                        }
                    }
                    else
                    if(dAdapter.isFilterSelected(position)){
                        for(int i=0;i<activityDetails.size();i++){
                            if(activityDetails.get(i).getActivityName().equalsIgnoreCase(dAdapter.getActivityName(position))){
                                for(int j=0;j<activityDetails.get(i).getActivityLocations().size();j++){
                                    addCustomActivityMarkerToMap(activityDetails.get(i).getActivityLocations().get(j),activityDetails.get(i).getTags().get(j),activityDetails.get(i).getMarkerImgUrl(),false, getMarkerListForActivity(dAdapter.getActivityName(position)), activityDetails.get(i).getActivityAddresses().get(j));
                                }
                                break;
                            }
                        }
                    }
                    else
                    if(!dAdapter.isFilterSelected(position)){
                        if(!getMarkerListForActivity(dAdapter.getActivityName(position)).isEmpty()){
                            for(int i=0; i<getMarkerListForActivity(dAdapter.getActivityName(position)).size();i++){
                                getMarkerListForActivity(dAdapter.getActivityName(position)).get(i).remove();
                                rl2.setVisibility(View.INVISIBLE);
                                rl.setVisibility(View.VISIBLE);
                            }
                            getMarkerListForActivity(dAdapter.getActivityName(position)).clear();
                        }
                    }
                    if (dAdapter.getActivityName(position).equalsIgnoreCase(getString(R.string.activity_group_market))) {
                        if (isOutdoorExpanded && dAdapter.getItemCount() > 3) {
                            dAdapter.removeItemsOnUnselect(Outdoor, dAdapter.getPosition("outdoor") - 1);
                            dAdapter.unselectImage(dAdapter.getPosition("outdoor"));
                            isOutdoorExpanded = false;
                        } else if (isIndoorExpanded && dAdapter.getItemCount() > 3) {
                            dAdapter.removeItemsOnUnselect(Indoor, dAdapter.getPosition("indoor") - 1);
                            dAdapter.unselectImage(dAdapter.getPosition("indoor"));
                            isIndoorExpanded = false;
                        }
                    }
                    curActivityPosition = position;
                }
            }
        }));
        final DoingFeedbackAdapter dFbackAdapter = new DoingFeedbackAdapter(dfList);

        RecyclerView.LayoutManager fLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rViewFeedback.setLayoutManager(fLayoutManager);
        rViewFeedback.setAdapter(dFbackAdapter);
        dFbackAdapter.notifyDataSetChanged();

        dFbackAdapter.setOnItemClickListener(new DoingFeedbackAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                dFbackAdapter.increaseBadgeCount(position, doingBadgeValues[position]);
                doingBadgeValues[position]++;
            }
        });

        recyclerView.setAdapter(dAdapter);
        dAdapter.notifyDataSetChanged();


        rViewFeedback.setAdapter(dFbackAdapter);
        dFbackAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }



    public void addMarkerDetailObjects(ArrayList<ActivityDetails> a, String nameOfACtivity){
        ArrayList<LatLng> activityLocations = new ArrayList<>();
        ArrayList<String> activityTags = new ArrayList<>();
        ArrayList<String> activityAdresses = new ArrayList<>();

        for(int i=0;i<a.size();i++){
            activityLocations.add(new LatLng(Double.parseDouble(a.get(i).getLocation().getLat()),Double.parseDouble(a.get(i).getLocation().getLon())));
            activityTags.add(a.get(i).getName());
            activityAdresses.add(a.get(i).getAddress());
        }

        if(nameOfACtivity.equalsIgnoreCase("market"))
            nameOfACtivity = "farmer's market";

        for(int i=0;i<annotations.size();i++){
            if(annotations.get(i).getName().equalsIgnoreCase(nameOfACtivity)) {
                if(nameOfACtivity.equalsIgnoreCase("farmer's market"))
                    activityDetails.add(new DoingMarkerDetails(annotations.get(i).getImgUrl(), "market", activityTags, activityLocations, activityAdresses));
                else
                    activityDetails.add(new DoingMarkerDetails(annotations.get(i).getImgUrl(), annotations.get(i).getName(), activityTags, activityLocations, activityAdresses));
                break;
            }
        }
    }

    /**
     * Add activity custom markers on map - need to load marker icon dynamically from url
     *
     * @param location activity feedback submitted position
     * @param tag tag for marker
     * @param iconUrl marker url
     * @param isSelected whether marker to be in selected state
     * @param activityMarkers list of markers for activity
     */
    private void addCustomActivityMarkerToMap(final LatLng location, final String tag, final String iconUrl, boolean isSelected, final ArrayList<Marker> activityMarkers, final String activityAddress){
        try{
            final View customMarkerView = LayoutInflater.from(getContext()).inflate(R.layout.doing_map_marker, null);
            final ImageView markerImageView =  customMarkerView.findViewById(R.id.doing_pin_image);
            final ImageView mapMarker = (ImageView) customMarkerView.findViewById(R.id.map_marker);
            if(isSelected)
                mapMarker.setImageResource(R.drawable.selected_marker);
            else
                mapMarker.setImageResource(R.drawable.unselected_marker);
            Glide.with(customMarkerView).load(iconUrl)
                    .listener(new RequestListener() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                            Log.e(TAG, "Load failed", e);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                            try{
                                if(resource!=null){
                                    DrawableCompat.setTint((Drawable) resource, ContextCompat.getColor(markerImageView.getContext(), R.color.white));
                                    markerImageView.setImageDrawable((Drawable)resource);
                                    if(doingMap!=null){
                                        Marker marker=doingMap.addMarker(new MarkerOptions()
                                                .position(location)
                                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(customMarkerView))));
                                        marker.setTitle(tag);
                                        marker.setTag(iconUrl);
                                        marker.setSnippet(activityAddress);
//                                        if(activityMarkers!=null)
                                            activityMarkers.add(marker);
                                    }
                                }
                            }
                            catch (Exception e){
                                Log.e("Error","Marker not loaded");
                            }
                            return true;
                        }
                    })
                    .into(markerImageView);
        }
        catch (NullPointerException n){
            Log.e("NullPointer","Null ptr exception");
        }
        catch (Exception e){
            Log.e("Error","Marker not added!");
        }
    }

    private Bitmap getMarkerBitmapFromView(View customMarkerView) {
        Bitmap returnedBitmap = null;
        try {
            customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
            customMarkerView.buildDrawingCache();
            returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
            Drawable drawable = customMarkerView.getBackground();
            if (drawable != null) {
                drawable.draw(canvas);
            }
            customMarkerView.draw(canvas);
        } catch (Exception e) {
            Log.e("Error","Bitmap error");
        }
        return returnedBitmap;
    }

    public ArrayList<Marker> getMarkerListForActivity(String activityName){
        if(activityName.equalsIgnoreCase("climbing"))
            return climbingMarkers;
        else
        if(activityName.equalsIgnoreCase("pilates"))
            return pilatesMarkers;
        else
        if(activityName.equalsIgnoreCase("yoga"))
            return yogaMarkers;
        else
        if(activityName.equalsIgnoreCase("gyms"))
            return gymMarkers;
        else
        if(activityName.equalsIgnoreCase("parks"))
            return parkMarkers;
        else
        if(activityName.equalsIgnoreCase("market"))
            return marketMarkers;
        else
        if(activityName.equalsIgnoreCase("running"))
            return runningMarkers;
        else
        if(activityName.equalsIgnoreCase("walking"))
            return walkingMarkers;
        else
            return null;
    }

    public void getActivityDetails(){
        Call<ActivityData> activityDataCall=apiInterface.activities(String.valueOf(latitude),String.valueOf(longitude));
        activityDataCall.enqueue(new Callback<ActivityData>() {
            @Override
            public void onResponse(Call<ActivityData> call, Response<ActivityData> response) {
                ActivityData resource = response.body();
                if(resource!=null) {
                    activityGroups = resource.getResponseData().getActivityGroups();
                    populateActivitiesList(activityGroups);
                }
            }

            @Override
            public void onFailure(Call<ActivityData> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public ArrayList<Marker> removeMarkerFromArrayList(Marker marker){
        if(climbingMarkers.contains(marker)){
            return climbingMarkers;
        }
        else
            if(gymMarkers.contains(marker)){
                return gymMarkers;
            }
            else
                if(yogaMarkers.contains(marker)){
                    return yogaMarkers;
                }
                else
                    if(pilatesMarkers.contains(marker)){
                        return pilatesMarkers;
                    }
                    else
                        if(runningMarkers.contains(marker)) {
                            return runningMarkers;
                        }
                        else
                            if(walkingMarkers.contains(marker)){
                                return walkingMarkers;
                            }
                            else
                                if(parkMarkers.contains(marker)){
                                    return parkMarkers;
                                }
                                else
                                    if(marketMarkers.contains(marker)){
                                        return marketMarkers;
                                    }
        else
            return null;
    }

    public void populateActivitiesList(ArrayList<ActivityGroup> activityGroups){
        runningDates.clear();
        runningLocations.clear();
        runningTags.clear();
        walkingDates.clear();
        walkingLocations.clear();
        walkingTags.clear();
        for(int i=0;i<activityGroups.size();i++){
            for (int j=0;j<activityGroups.get(i).getActivities().size();j++){
                if (activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("climbing")) {
                    climbings = activityGroups.get(i).getActivities().get(j).getClimbings();
                }
                else
                if(activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("gyms"))
                    gyms = activityGroups.get(i).getActivities().get(j).getGyms();
                else
                if(activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("pilates"))
                    pilates = activityGroups.get(i).getActivities().get(j).getPilates();
                else
                if(activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("yoga"))
                    yogas = activityGroups.get(i).getActivities().get(j).getYogas();
                else
                if(activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("parks"))
                    parks = activityGroups.get(i).getActivities().get(j).getParks();
                else
                if(activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("market"))
                    markets = activityGroups.get(i).getActivities().get(j).getMarkets();
                else
                if(activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("running")) {
                    runningLocations.add(new LatLng(Double.parseDouble(activityGroups.get(i).getActivities().get(j).getLocation().getLat()), Double.parseDouble(activityGroups.get(i).getActivities().get(j).getLocation().getLon())));
                    runningTags.add(activityGroups.get(i).getActivities().get(j).getEventTitle());
                    runningDates.add(activityGroups.get(i).getActivities().get(j).getDetailedDateTime());
                }
                else
                if(activityGroups.get(i).getActivities().get(j).getActivityName().equalsIgnoreCase("walking")) {
                    walkingLocations.add(new LatLng(Double.parseDouble(activityGroups.get(i).getActivities().get(j).getLocation().getLat()), Double.parseDouble(activityGroups.get(i).getActivities().get(j).getLocation().getLon())));
                    walkingTags.add(activityGroups.get(i).getActivities().get(j).getEventTitle());
                    walkingDates.add(activityGroups.get(i).getActivities().get(j).getDetailedDateTime());
                }
            }
        }

        addMarkerDetailObjects(climbings,"climbing");
        addMarkerDetailObjects(gyms,"gyms");
        addMarkerDetailObjects(pilates,"pilates");
        addMarkerDetailObjects(yogas,"yoga");
        addMarkerDetailObjects(parks,"parks");
        addMarkerDetailObjects(markets,"market");

        for(int i=0;i<annotations.size();i++) {
            if (annotations.get(i).getName().equalsIgnoreCase("running")) {
                activityDetails.add(new DoingMarkerDetails(annotations.get(i).getImgUrl(), annotations.get(i).getName(), runningTags, runningLocations, runningDates));
                break;
            }
        }

        for(int i=0;i<annotations.size();i++) {
            if (annotations.get(i).getName().equalsIgnoreCase("walking")) {
                activityDetails.add(new DoingMarkerDetails(annotations.get(i).getDoingFeedbackUrl(), annotations.get(i).getName(), walkingTags, walkingLocations, walkingDates));
                break;
            }
        }
    }

    public boolean isNumber(String s)
    {
        for (int i = 0; i < s.length(); i++)
            if (!Character.isDigit(s.charAt(i)))
                return false;

        return true;
    }
}
