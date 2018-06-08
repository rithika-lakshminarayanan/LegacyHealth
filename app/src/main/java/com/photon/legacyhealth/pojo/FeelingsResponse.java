package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class FeelingsResponse {
    int feelingId;
    ArrayList<CoordinatesFeelings>  coordinates;

    public int getFeelingId() {
        return feelingId;
    }

    public void setFeelingId(int feelingId) {
        this.feelingId = feelingId;
    }

    public ArrayList<CoordinatesFeelings> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<CoordinatesFeelings> coordinates) {
        this.coordinates = coordinates;
    }

    public int getListSize(){ return  coordinates.size(); }
}
