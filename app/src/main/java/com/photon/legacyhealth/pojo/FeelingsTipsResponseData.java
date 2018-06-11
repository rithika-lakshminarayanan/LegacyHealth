package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class FeelingsTipsResponseData {
    private String alerts;
    private String push;
    private ArrayList<Tips> tips;

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public ArrayList<Tips> getTips() {
        return tips;
    }

    public void setTips(ArrayList<Tips> tips) {
        this.tips = tips;
    }


}
