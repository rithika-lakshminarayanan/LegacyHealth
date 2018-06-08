package com.photon.legacyhealth.pojo;

public class MetaDataResponse {
    String status;
    String statusCode;
    MetaDataResponseData responseData;

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

    public MetaDataResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(MetaDataResponseData responseData) {
        this.responseData = responseData;
    }
}
