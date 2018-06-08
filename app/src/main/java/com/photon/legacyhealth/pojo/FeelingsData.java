package com.photon.legacyhealth.pojo;

public class FeelingsData {
    String statusCode;
    String  status;
    FeelingsResponseData responseData;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FeelingsResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(FeelingsResponseData responseData) {
        this.responseData = responseData;
    }
}
