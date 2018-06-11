package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class FeelingsTipsResponseData {
    String alerts;
    String push;

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

    ArrayList<Tips> tips;
}
