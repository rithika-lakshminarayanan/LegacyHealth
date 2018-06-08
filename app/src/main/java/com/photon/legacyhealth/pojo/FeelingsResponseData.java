package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class FeelingsResponseData {
    ArrayList<FeelingsResponse> feelings;
    String latitude;
    String longitude;
    boolean zipCodeSupported;

    public ArrayList<FeelingsResponse> getFeelings() {
        return feelings;
    }

    public void setFeelings(ArrayList<FeelingsResponse> feelings) {
        this.feelings = feelings;
    }

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
}
