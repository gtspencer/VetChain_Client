package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "DOCTOR")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Appointment> schedule;

    public int getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Set<Appointment> getSchedule() {
        return schedule;
    }
    public void setSchedule(Set<Appointment> schedule) {
        this.schedule = schedule;
    }
}
