package com.wielabs.Models;

public class Leader {

    int noOfBids;
    String name, id;

    public String getId() {
        return id;
    }

    public Leader(String id, int noOfBids, String name) {
        this.noOfBids = noOfBids;
        this.name = name;
        this.id = id;
    }

    public int getNoOfBids() {
        return noOfBids;
    }

    public void setNoOfBids(int noOfBids) {
        this.noOfBids = noOfBids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
