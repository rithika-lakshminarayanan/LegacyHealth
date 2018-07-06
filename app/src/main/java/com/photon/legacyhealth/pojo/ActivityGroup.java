package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class ActivityGroup {
    private int order;
    private String activityGroupName;
    private int activityGroupId;
    private boolean activityGroup;
    private ArrayList<ActivityResponseData> activities;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getActivityGroupName() {
        return activityGroupName;
    }

    public void setActivityGroupName(String activityGroupName) {
        this.activityGroupName = activityGroupName;
    }

    public int getActivityGroupId() {
        return activityGroupId;
    }

    public void setActivityGroupId(int activityGroupId) {
        this.activityGroupId = activityGroupId;
    }

    public boolean isActivityGroup() {
        return activityGroup;
    }

    public void setActivityGroup(boolean activityGroup) {
        this.activityGroup = activityGroup;
    }

    public ArrayList<ActivityResponseData> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivityResponseData> activities) {
        this.activities = activities;
    }
}
