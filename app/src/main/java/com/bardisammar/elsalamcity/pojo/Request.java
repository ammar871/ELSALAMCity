package com.bardisammar.elsalamcity.pojo;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String addresse;
    private String total;
    private String comment;
    private List<Pro_Of_Product> pro_cato;

    public Request(String phone, String name, String addresse, String total,  String comment, List<Pro_Of_Product> foods) {
        this.phone = phone;
        this.name = name;
        this.addresse = addresse;
        this.total = total;

        this.comment = comment;
        this.pro_cato = foods;
    }

    public Request() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Pro_Of_Product> getFoods() {
        return pro_cato;
    }

    public void setFoods(List<Pro_Of_Product> foods) {
        this.pro_cato = foods;
    }
}
