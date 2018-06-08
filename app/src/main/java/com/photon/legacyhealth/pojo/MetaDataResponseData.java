package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class MetaDataResponseData {
    ArrayList<MetaData> metadata;
    String version;
    String url;
    String sms;
    boolean normalUpdate;
    String longitude;
    String latitude;
    String emailSubject;
    String emailBody;

    public ArrayList<MetaData> getMetadata() {
        return metadata;
    }

    public void setMetadata(ArrayList<MetaData> metadata) {
        this.metadata = metadata;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public boolean isNormalUpdate() {
        return normalUpdate;
    }

    public void setNormalUpdate(boolean normalUpdate) {
        this.normalUpdate = normalUpdate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }
}
