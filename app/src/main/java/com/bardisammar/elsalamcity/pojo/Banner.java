package com.bardisammar.elsalamcity.pojo;

public class Banner {
    private String Image ,Name,id;

    public Banner() {
    }

    public Banner(String image, String name, String id) {
        Image = image;
        Name = name;
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
