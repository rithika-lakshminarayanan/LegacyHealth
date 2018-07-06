package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class ActivityGroupsData {
    private String latitude;
    private String longitude;
    private boolean zipCodeSupported;
    ArrayList<ActivityGroup> activityGroups;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isZipCodeSupported() {
        return zipCodeSupported;
    }

    public void setZipCodeSupported(boolean zipCodeSupported) {
        this.zipCodeSupported = zipCodeSupported;
    }

    public ArrayList<ActivityGroup> getActivityGroups() {
        return activityGroups;
    }

    public void setActivityGroups(ArrayList<ActivityGroup> activityGroups) {
        this.activityGroups = activityGroups;
    }
}
