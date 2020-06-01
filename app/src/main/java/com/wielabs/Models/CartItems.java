package com.wielabs.Models;

import java.io.Serializable;

public class CartItems implements Serializable {

    public String image_url, mybid, status, titleCart, id, start_date, bidentry, mrp;

    public CartItems(String image_url, String mybid, String status, String titleCart, String id, String start_date, String bidentry, String mrp) {
        this.image_url = image_url;
        this.mybid = mybid;
        this.status = status;
        this.titleCart = titleCart;
        this.id = id;
        this.start_date = start_date;
        this.bidentry = bidentry;
        this.mrp = mrp;
    }

    public String getId() {
        return id;
    }

    public String getMrp() {
        return mrp;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getMybid() {
        return mybid;
    }

    public String getStatus() {
        return status;
    }

    public String getTitleCart() {
        return titleCart;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getBidentry() {
        return bidentry;
    }
}

