package com.bidbad.Models;

public class User {

    private int id;
    private String email, firstname, lastname, address, pincode, mobile;

    public User(int id, String email, String firstname, String lastname, String address, String pincode, String mobile) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.pincode = pincode;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPincode() {
        return pincode;
    }
}