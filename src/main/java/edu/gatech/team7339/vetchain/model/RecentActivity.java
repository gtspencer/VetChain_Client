package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ACTIVITY")
public class RecentActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "ACTIVITY")
    private String activity;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public RecentActivity(){
        date = new Date();
    }
    public RecentActivity(User user, Date date, String activity){
        this.user = user;
        this.date = date;
        this.activity = activity;
    }

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
