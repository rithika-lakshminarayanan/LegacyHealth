package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class Tips {
    private int id;
    private ArrayList<Integer> feelings;
    private boolean enableTextAsLink;
    private boolean enableGohealthLocationLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getFeelings() {
        return feelings;
    }

    public void setFeelings(ArrayList<Integer> feelings) {
        this.feelings = feelings;
    }

    public boolean isEnableTextAsLink() {
        return enableTextAsLink;
    }

    public void setEnableTextAsLink(boolean enableTextAsLink) {
        this.enableTextAsLink = enableTextAsLink;
    }

    public boolean isEnableGohealthLocationLink() {
        return enableGohealthLocationLink;
    }

    public void setEnableGohealthLocationLink(boolean enableGohealthLocationLink) {
        this.enableGohealthLocationLink = enableGohealthLocationLink;
    }

    private String message;
    private String url;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
