package com.photon.legacyhealth.pojo;

public class MetaData {
    private int id;
    private String name;
    private String key;
    private boolean isHighlited  = false;
    private String imgUrl;
    private String imgSelUrl;
    private String annotationUrl;
    private int order;
    private boolean hasSymptomMessage;
    private boolean hasCriticalMessage;
    private boolean hasCriteria;
    private String description = "";
    private int parentId;
    private String doingFeedbackUrl;
    private String listUrl;

    public String getDoingFeedbackUrl() {
        return doingFeedbackUrl;
    }

    public void setDoingFeedbackUrl(String doingFeedbackUrl) {
        this.doingFeedbackUrl = doingFeedbackUrl;
    }

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

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

    public String getAnnotationUrl() {
        return annotationUrl;
    }

    public void setAnnotationUrl(String annotationUrl) {
        this.annotationUrl = annotationUrl;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
