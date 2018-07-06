package com.photon.legacyhealth.pojo;

import java.util.ArrayList;

public class ActivityResponseData {
    private String activityName;
    private int activtyId;
    private String ageGroup;
    private String contactInfo;
    private String detailedDateTime;
    private String eventDescription;
    private String eventTitle;
    private int topicId;
    private EventAddress address;
    private EventHost eventHosts;
    private EventTime eventTime;
    private EventLocation location;
    private String assetGuid;
    private ArrayList<ActivityDetails> climbings;
    private ArrayList<ActivityDetails> gyms;
    private ArrayList<ActivityDetails> pilates;
    private ArrayList<ActivityDetails> yogas;
    private ArrayList<ActivityDetails> parks;
    private ArrayList<ActivityDetails> markets;

    public String getAssetGuid() {
        return assetGuid;
    }

    public void setAssetGuid(String assetGuid) {
        this.assetGuid = assetGuid;
    }

    public ArrayList<ActivityDetails> getClimbings() {
        return climbings;
    }

    public void setClimbings(ArrayList<ActivityDetails> climbings) {
        this.climbings = climbings;
    }

    public ArrayList<ActivityDetails> getGyms() {
        return gyms;
    }

    public void setGyms(ArrayList<ActivityDetails> gyms) {
        this.gyms = gyms;
    }

    public ArrayList<ActivityDetails> getPilates() {
        return pilates;
    }

    public void setPilates(ArrayList<ActivityDetails> pilates) {
        this.pilates = pilates;
    }

    public ArrayList<ActivityDetails> getYogas() {
        return yogas;
    }

    public void setYogas(ArrayList<ActivityDetails> yogas) {
        this.yogas = yogas;
    }

    public ArrayList<ActivityDetails> getParks() {
        return parks;
    }

    public void setParks(ArrayList<ActivityDetails> parks) {
        this.parks = parks;
    }

    public ArrayList<ActivityDetails> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<ActivityDetails> markets) {
        this.markets = markets;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivtyId() {
        return activtyId;
    }

    public void setActivtyId(int activtyId) {
        this.activtyId = activtyId;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDetailedDateTime() {
        return detailedDateTime;
    }

    public void setDetailedDateTime(String detailedDateTime) {
        this.detailedDateTime = detailedDateTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public EventAddress getAddress() {
        return address;
    }

    public void setAddress(EventAddress address) {
        this.address = address;
    }

    public EventHost getEventHosts() {
        return eventHosts;
    }

    public void setEventHosts(EventHost eventHosts) {
        this.eventHosts = eventHosts;
    }

    public EventTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(EventTime eventTime) {
        this.eventTime = eventTime;
    }

    public EventLocation getLocation() {
        return location;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }


}
