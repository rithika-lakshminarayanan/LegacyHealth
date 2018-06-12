package com.photon.legacyhealth;

public class FeelingFeedback {
    private String fImgUrl;
    private String fText;

    public FeelingFeedback(String fImgUrl, String fText) {

        this.fImgUrl = fImgUrl;
        this.fText = fText;
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
}
