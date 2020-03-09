package com.wielabs.Models;

import java.io.Serializable;

public class Current_Product implements Serializable {

    private String id;
    private String image_url, image_url2, image_url3;
    private String title;
    private String end_date;
    private String mrp;
    private String sp;
    private String description;


    public Current_Product(String id, String image_url, String title, String end_date, String mrp, String sp, String description, String image_url2, String image_url3){
        this.end_date = end_date;
        this.title = title;
        this.image_url = image_url;
        this.id = id;
        this.mrp = mrp;
        this.description = description;
        this.sp = sp;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url2() {
        return image_url2;
    }

    public String getImage_url3() {
        return image_url3;
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
