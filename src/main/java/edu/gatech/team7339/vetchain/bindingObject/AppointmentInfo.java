package edu.gatech.team7339.vetchain.bindingObject;

import java.util.Date;

public class AppointmentInfo {
    private String petId;
    private String date;
    private String time;
    private String reason;

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetId() {
        return petId;
    }
}
