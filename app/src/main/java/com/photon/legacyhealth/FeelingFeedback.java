package com.photon.legacyhealth;

import android.os.Parcel;
import android.os.Parcelable;

public class FeelingFeedback implements Parcelable{
    private String fImgUrl;
    private String fText;
    public boolean isBadgeVisible;

    public FeelingFeedback(String fImgUrl, String fText) {

        this.fImgUrl = fImgUrl;
        this.fText = fText;
        this.isBadgeVisible=false;
    }

    protected FeelingFeedback(Parcel in) {
        fImgUrl = in.readString();
        fText = in.readString();
        isBadgeVisible = in.readByte() != 0;
    }

    public static final Creator<FeelingFeedback> CREATOR = new Creator<FeelingFeedback>() {
        @Override
        public FeelingFeedback createFromParcel(Parcel in) {
            return new FeelingFeedback(in);
        }

        @Override
        public FeelingFeedback[] newArray(int size) {
            return new FeelingFeedback[size];
        }
    };

    public String getfImgUrl() {
        return fImgUrl;
    }

    public void setfImgUrl(String fImgUrl) {
        this.fImgUrl = fImgUrl;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }

    public void setBadgeVisible(boolean badgeVisible) {
        this.isBadgeVisible = badgeVisible;
    }
    public boolean isBadgeVisible() {
        return isBadgeVisible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fImgUrl);
        dest.writeString(fText);
        dest.writeByte((byte) (isBadgeVisible ? 1 : 0));
    }
}
