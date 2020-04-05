package com.wielabs.Models;

public class Order {

    private String name, bidamount, date, image_url, mrp, sp, oid, address;

    public Order(String name, String bidamount, String date, String image_url, String mrp, String sp, String oid, String address) {
        this.name = name;
        this.bidamount = bidamount;
        this.date = date;
        this.image_url = image_url;
        this.mrp = mrp;
        this.sp = sp;
        this.oid = oid;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getBidamount() {
        return bidamount;
    }

    public String getDate() {
        return date;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getMrp() {
        return mrp;
    }

    public String getSp() {
        return sp;
    }

    public String getOid() {
        return oid;
    }

    public String getAddress() {
        return address;
    }
}
