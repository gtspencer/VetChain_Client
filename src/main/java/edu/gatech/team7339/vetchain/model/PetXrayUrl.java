package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PET_XRAY")
public class PetXrayUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "XRAY_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "PET_ID")
    private Pet pet;

    @Column(name = "URL")
    private String url;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE")
    private Date date;


    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
