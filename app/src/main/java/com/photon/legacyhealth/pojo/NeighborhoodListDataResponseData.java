package com.photon.legacyhealth.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class NeighborhoodListDataResponseData implements Parcelable{
    private  String city;
    private String latitude;
    private String longitude;
    private int neighborhoodId;
    private String neighborhoodName;
    private String state;

    protected NeighborhoodListDataResponseData(Parcel in) {
        city = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        neighborhoodId = in.readInt();
        neighborhoodName = in.readString();
        state = in.readString();
    }

    public static final Creator<NeighborhoodListDataResponseData> CREATOR = new Creator<NeighborhoodListDataResponseData>() {
        @Override
        public NeighborhoodListDataResponseData createFromParcel(Parcel in) {
            return new NeighborhoodListDataResponseData(in);
        }

        @Override
        public NeighborhoodListDataResponseData[] newArray(int size) {
            return new NeighborhoodListDataResponseData[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public int getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(int neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public String getNeighborhoodName() {
        return neighborhoodName;
    }

    public void setNeighborhoodName(String neighborhoodName) {
        this.neighborhoodName = neighborhoodName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeInt(neighborhoodId);
        dest.writeString(neighborhoodName);
        dest.writeString(state);
    }
}
