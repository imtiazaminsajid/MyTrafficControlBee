package com.example.ambit.mytrafficcontrolbee;

public class AlertMessage {
    private String timeAndDate;
    private String location;
    private String message;
    private String imageUrl;

    public AlertMessage() {
    }

    public AlertMessage(String timeAndDate, String location, String message) {
        this.timeAndDate = timeAndDate;
        this.location = location;
        this.message = message;
    }

    public AlertMessage(String timeAndDate, String location, String message, String imageUrl) {
        this.timeAndDate = timeAndDate;
        this.location = location;
        this.message = message;
        this.imageUrl = imageUrl;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
