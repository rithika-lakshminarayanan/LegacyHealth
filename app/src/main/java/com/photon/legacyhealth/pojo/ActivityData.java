package com.photon.legacyhealth.pojo;

import java.io.Serializable;

public class ActivityData implements Serializable {
    private String statusCode;
    private String status;
    ActivityGroupsData responseData;

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

    public ActivityGroupsData getResponseData() {
        return responseData;
    }

    public void setResponseData(ActivityGroupsData responseData) {
        this.responseData = responseData;
    }
}
