package com.wielabs.Models;

public class NotificationModel {

    String title, message, time;

    public NotificationModel(String title, String message, String time) {
        this.title = title;
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }
}
