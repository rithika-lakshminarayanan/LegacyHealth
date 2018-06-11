package com.photon.legacyhealth.pojo;

public class FeelingTips {
    String status;
    String statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public FeelingsTipsResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(FeelingsTipsResponseData responseData) {
        this.responseData = responseData;
    }

    FeelingsTipsResponseData responseData;
}
