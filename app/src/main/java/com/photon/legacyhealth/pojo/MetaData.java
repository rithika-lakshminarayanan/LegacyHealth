package com.photon.legacyhealth.pojo;

public class MetaData {
    int id;
    String name;
    String key;
    boolean isHighlited;
    String imgUrl;
    String imgSelUrl;
    boolean hasSymptomMessage;
    boolean hasCriticalMessage;
    boolean hasCriteria;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isHighlited() {
        return isHighlited;
    }

    public void setHighlited(boolean highlited) {
        isHighlited = highlited;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgSelUrl() {
        return imgSelUrl;
    }

    public void setImgSelUrl(String imgSelUrl) {
        this.imgSelUrl = imgSelUrl;
    }

    public boolean isHasSymptomMessage() {
        return hasSymptomMessage;
    }

    public void setHasSymptomMessage(boolean hasSymptomMessage) {
        this.hasSymptomMessage = hasSymptomMessage;
    }

    public boolean isHasCriticalMessage() {
        return hasCriticalMessage;
    }

    public void setHasCriticalMessage(boolean hasCriticalMessage) {
        this.hasCriticalMessage = hasCriticalMessage;
    }

    public boolean isHasCriteria() {
        return hasCriteria;
    }

    public void setHasCriteria(boolean hasCriteria) {
        this.hasCriteria = hasCriteria;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
