package com.wielabs.Models;

import java.io.Serializable;

public class CartItems implements Serializable {

    public String image_url,mybid,status,titleCart,id;

    public CartItems(String image_url, String mybid, String status, String titleCart, String id){
        this.image_url = image_url;
        this.mybid = mybid;
        this.status = status;
        this.titleCart = titleCart;
        this.id = id;
    }

    public String getId() {
        return id;
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
}

