package com.photon.legacyhealth;

import android.os.Parcel;
import android.os.Parcelable;

public class Feeling implements Parcelable{
    private String imgUrl;
    private String symptom_name;
    private String imgSelUrl;
    private boolean isImageChanged;
    private boolean isImageHighlighted;

    public Feeling(String imgUrl, String symptom_name, String imgSelUrl) {
        this.imgUrl = imgUrl;
        this.symptom_name = symptom_name;
        this.imgSelUrl = imgSelUrl;
        this.isImageHighlighted = false;
    }

    protected Feeling(Parcel in) {
        imgUrl = in.readString();
        symptom_name = in.readString();
        imgSelUrl = in.readString();
        isImageChanged = in.readByte() != 0;
        isImageHighlighted = in.readByte() != 0;
    }

    public static final Creator<Feeling> CREATOR = new Creator<Feeling>() {
        @Override
        public Feeling createFromParcel(Parcel in) {
            return new Feeling(in);
        }

        @Override
        public Feeling[] newArray(int size) {
            return new Feeling[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSymptom_name() {
        return symptom_name;
    }

    public void setSymptom_name(String symptom_name) {
        this.symptom_name = symptom_name;
    }

    public String getImgSelUrl() {
        return imgSelUrl;
    }

    public void setImgSelUrl(String imgSelUrl) {
        this.imgSelUrl = imgSelUrl;
    }
    public boolean isImageChanged() {
        return isImageChanged;
    }
    public void setImageChanged(boolean imageChanged) {
        isImageChanged = imageChanged;
    }
    public boolean isImageHighlighted() {
        return isImageHighlighted;
    }

    public void setImageHighlighted(boolean imageHighlighted) {
        isImageHighlighted = imageHighlighted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(symptom_name);
        dest.writeString(imgSelUrl);
        dest.writeByte((byte) (isImageChanged ? 1 : 0));
        dest.writeByte((byte) (isImageHighlighted ? 1 : 0));
    }
}
