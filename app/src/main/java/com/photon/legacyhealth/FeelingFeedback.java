package com.photon.legacyhealth;

public class FeelingFeedback {
    private String fImgUrl;
    private String fText;
    public boolean isBadgeVisible;

    public FeelingFeedback(String fImgUrl, String fText) {

        this.fImgUrl = fImgUrl;
        this.fText = fText;
        this.isBadgeVisible=false;
    }

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
}
