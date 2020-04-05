package com.wielabs.Models;

import java.io.Serializable;

public class WonItem implements Serializable {

    private String id;
    private String image_url;
    private String title;
    private String end_date;
    private String mrp;
    private String sp;
    private String description;
    private String bidamount;
    private String status;
    private String orderplaced;

    public WonItem(String id, String image_url, String title, String end_date, String mrp, String sp, String description, String bidamount, String status, String orderplaced){
        this.end_date = end_date;
        this.title = title;
        this.image_url = image_url;
        this.id = id;
        this.mrp = mrp;
        this.description = description;
        this.sp = sp;
        this.bidamount = bidamount;
        this.status = status;
        this.orderplaced = orderplaced;
    }

    public String getDescription() {
        return description;
    }

    public String getBidamount() {
        return bidamount;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderplaced() {
        return orderplaced;
    }

    public String getMrp() {
        return mrp;
    }

    public String getSp() {
        return sp;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }
}
