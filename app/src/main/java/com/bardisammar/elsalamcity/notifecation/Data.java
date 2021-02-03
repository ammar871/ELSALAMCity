package com.bardisammar.elsalamcity.notifecation;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
    private String body,title,image,click_action;


    public Data() {
    }


    public Data(String body, String title, String image, String click_action) {
        this.body = body;
        this.title = title;
        this.image = image;
        this.click_action = click_action;
    }

    public Data(String body, String title, String click_action) {
        this.body = body;
        this.title = title;
        this.click_action = click_action;
    }

    protected Data(Parcel in) {
        body = in.readString();
        title = in.readString();
        image = in.readString();
        click_action = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(body);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(click_action);
    }
}
