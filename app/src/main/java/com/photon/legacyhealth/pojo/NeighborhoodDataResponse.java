package com.photon.legacyhealth.pojo;

public class NeighborhoodDataResponse {
    String neighborhood;
    boolean isLocationSupported;

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public boolean isLocationSupported() {
        return isLocationSupported;
    }

    public void setLocationSupported(boolean locationSupported) {
        isLocationSupported = locationSupported;
    }
}
