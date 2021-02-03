package com.bardisammar.elsalamcity.pojo;

public class ModelLike {

    private String prouductKey;
    private String phoneNumber;

    boolean like;

    public ModelLike() {
    }

    public ModelLike(String prouductKey, String phoneNumber, boolean like) {
        this.prouductKey = prouductKey;
        this.phoneNumber = phoneNumber;
        this.like = like;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getProuductKey() {
        return prouductKey;
    }

    public void setProuductKey(String prouductKey) {
        this.prouductKey = prouductKey;
    }
}
