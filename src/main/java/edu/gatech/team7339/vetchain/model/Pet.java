package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PET")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PET_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(nullable = false,name = "PET_NAME")
    private String name;
    @Column(nullable = false, name = "SPECIES")
    private String species;
    @Column(nullable = false, name = "SEX")
    private String sex;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<PetXrayUrl> xrayUrls;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<PetMedRecord> medRecordUrls;

    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    @Column(name ="DOB")
    public String dob;

    public Pet(){
        xrayUrls= new ArrayList<>();
        medRecordUrls=new ArrayList<>();
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        int year = Integer.parseInt(dob.substring(0,dob.indexOf("-")));
        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }

    public List<PetMedRecord> getMedRecordUrls() {
        return medRecordUrls;
    }

    public List<PetXrayUrl> getXrayUrls() {
        return xrayUrls;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setMedRecordUrls(List<PetMedRecord> medRecordUrls) {
        this.medRecordUrls = medRecordUrls;
    }

    public void setXrayUrls(List<PetXrayUrl> xrayUrls) {
        this.xrayUrls = xrayUrls;
    }
}
