package com.photon.legacyhealth;

public class Feeling {
    private String imgUrl;
    private String symptom_name;
    private String imgSelUrl;
    public boolean isImageChanged;

    public Feeling(String imgUrl, String symptom_name, String imgSelUrl) {
        this.imgUrl = imgUrl;
        this.symptom_name = symptom_name;
        this.imgSelUrl = imgSelUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSymptom_name() {
        return symptom_name;
    }

    public void setSymptom_name(String symptom_name) {
        this.symptom_name = symptom_name;
    }

    public String getImgSelUrl() {
        return imgSelUrl;
    }

    public void setImgSelUrl(String imgSelUrl) {
        this.imgSelUrl = imgSelUrl;
    }
    public boolean isImageChanged() {
        return isImageChanged;
    }
    public void setImageChanged(boolean imageChanged) {
        isImageChanged = imageChanged;
    }
}
