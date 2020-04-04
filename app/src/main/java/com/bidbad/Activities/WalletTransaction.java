package com.bidbad.Activities;

public class WalletTransaction {

    public String orderid;
    public String value;
    public String description;
    public String date;
    public String image_url;

    public WalletTransaction(String orderid, String value, String date, String description, String image_url){
        this.orderid = orderid;
        this.value = value;
        this.description = description;
        this.date = date;
        this.image_url = image_url;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getValue() {
        return value;
    }

    public String getImage_url() {
        return image_url;
    }
}
