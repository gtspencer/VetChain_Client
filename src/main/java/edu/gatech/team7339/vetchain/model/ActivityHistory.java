package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ActivityHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PET_ID")
    private Pet pet;

    @Column(name = "ACTIVITY")
    private String activity;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    public ActivityHistory(User user, Pet pet, String activity, Date timestamp){
        this.user = user;
        this.pet = pet;
        this.activity = activity;
        timestamp.setTime(timestamp.getTime());
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Pet getPet() {
        return pet;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    public void setId(int id) {
        this.id = id;
    }
}
