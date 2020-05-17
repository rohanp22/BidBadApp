package com.wielabs.Models;

public class ScratchCardModel {

    private String id, type, text, color;

    public ScratchCardModel(String id, String type, String text, String color) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }
}
