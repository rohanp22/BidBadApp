package com.wielabs.Models;

import java.io.Serializable;

public class NotificationModel implements Serializable {

    String message, header;

    NotificationModel(String message, String header){
        this.message = message;
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }
}
