package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class NeighborhoodListData {
    private String status;
    private String statusCode;
    private ArrayList<NeighborhoodListDataResponseData> responseData;

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

    public ArrayList<NeighborhoodListDataResponseData> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<NeighborhoodListDataResponseData> responseData) {
        this.responseData = responseData;
    }


}
