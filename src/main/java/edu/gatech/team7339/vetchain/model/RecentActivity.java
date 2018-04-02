package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RecentActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date date;
    @Column(name = "ACTIVITY")
    private String activity;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
