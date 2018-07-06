package com.photon.legacyhealth;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DoingMarkerDetails {
    private String markerImgUrl;
    private String activityName;
    private ArrayList<String> activityAddresses;
    private ArrayList<String> tags;
    private ArrayList<LatLng> activityLocations;

    public DoingMarkerDetails(String markerImgUrl, String activityName, ArrayList<String> tags, ArrayList<LatLng> activityLocations, ArrayList<String> activityAddresses) {
        this.markerImgUrl = markerImgUrl;
        this.activityName = activityName;
        this.tags = tags;
        this.activityLocations = activityLocations;
        this.activityAddresses = activityAddresses;
    }

    public String getMarkerImgUrl() {
        return markerImgUrl;
    }

    public void setMarkerImgUrl(String markerImgUrl) {
        this.markerImgUrl = markerImgUrl;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<LatLng> getActivityLocations() {
        return activityLocations;
    }

    public void setActivityLocations(ArrayList<LatLng> activityLocations) {
        this.activityLocations = activityLocations;
    }
    public ArrayList<String> getActivityAddresses() {
        return activityAddresses;
    }

    public void setActivityAddresses(ArrayList<String> activityAddresses) {
        this.activityAddresses = activityAddresses;
    }
}
