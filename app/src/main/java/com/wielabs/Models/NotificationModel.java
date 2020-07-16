package com.wielabs.Models;

public class NotificationModel {

    String title, message;

    public NotificationModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
