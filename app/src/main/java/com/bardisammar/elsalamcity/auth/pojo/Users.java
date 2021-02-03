package com.bardisammar.elsalamcity.auth.pojo;

public class Users {
    private String phoneNumber;

    public Users() {
    }

    public Users( String phoneNumber) {

        this.phoneNumber = phoneNumber;

    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
