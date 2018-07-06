package com.photon.legacyhealth.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class MetaData implements Parcelable{
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

    protected MetaData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        key = in.readString();
        isHighlited = in.readByte() != 0;
        imgUrl = in.readString();
        imgSelUrl = in.readString();
        annotationUrl = in.readString();
        order = in.readInt();
        hasSymptomMessage = in.readByte() != 0;
        hasCriticalMessage = in.readByte() != 0;
        hasCriteria = in.readByte() != 0;
        description = in.readString();
        parentId = in.readInt();
        doingFeedbackUrl = in.readString();
        listUrl = in.readString();
    }

    public static final Creator<MetaData> CREATOR = new Creator<MetaData>() {
        @Override
        public MetaData createFromParcel(Parcel in) {
            return new MetaData(in);
        }

        @Override
        public MetaData[] newArray(int size) {
            return new MetaData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeByte((byte) (isHighlited ? 1 : 0));
        dest.writeString(imgUrl);
        dest.writeString(imgSelUrl);
        dest.writeString(annotationUrl);
        dest.writeInt(order);
        dest.writeByte((byte) (hasSymptomMessage ? 1 : 0));
        dest.writeByte((byte) (hasCriticalMessage ? 1 : 0));
        dest.writeByte((byte) (hasCriteria ? 1 : 0));
        dest.writeString(description);
        dest.writeInt(parentId);
        dest.writeString(doingFeedbackUrl);
        dest.writeString(listUrl);
    }
}
