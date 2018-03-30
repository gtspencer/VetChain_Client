package edu.gatech.team7339.vetchain.bindingObject;

import java.util.Date;

public class AppointmentInfo {
    private int petId;
    private Date date;
    private Date time;
    private String reason;

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getPetId() {
        return petId;
    }
}
