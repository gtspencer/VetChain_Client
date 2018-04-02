package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "PET")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PET_ID")
    private int id;

    @ManyToMany
    @JoinTable(name = "USER_PET",
                joinColumns = @JoinColumn(name = "PET_ID",referencedColumnName = "PET_ID"),
                inverseJoinColumns = @JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID")
    )
    private Set<User> users;

    @Column(nullable = false,name = "PET_NAME")
    private String name;
    @Column(nullable = false, name = "BREED")
    private String breed;
    @Column(nullable = false, name = "GENDER")
    private String gender;
    @Column(nullable = false, name = "WEIGHT")
    private String weight;
    @Column(name = "INSURANCE_NUM")
    private String insuranceNum;
    @Column(name = "MICROCHIP_NUM")
    private String microchipNum;
    @Column(name = "INSRANCE_CARRIER")
    private String insuranceCarrier;
    @Column(name = "LICENSE")
    private String license;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private Set<PetXrayUrl> xrayUrls;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private Set<PetMedRecord> medRecordUrls;

    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    @Column(name ="DOB")
    private String dob;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private Set<Appointment> schedule;

    public Pet(Set<User> users){
        this.users = users;
        xrayUrls = new HashSet<>();
        medRecordUrls = new HashSet<>();
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setMicrochipNum(String microchipNum) {
        this.microchipNum = microchipNum;
    }

    public void setInsuranceNum(String insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public void setInsuranceCarrier(String insuranceCarrier) {
        this.insuranceCarrier = insuranceCarrier;
    }

    public String getLicense() {
        return license;
    }

    public String getMicrochipNum() {
        return microchipNum;
    }

    public String getInsuranceNum() {
        return insuranceNum;
    }

    public String getInsuranceCarrier() {
        return insuranceCarrier;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBreed() {
        return breed;
    }

    public String getWeight() {
        return weight;
    }

    public Pet(){
        xrayUrls= new HashSet<>();
        medRecordUrls=new HashSet<>();
        users = new HashSet<>();
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

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        int year = Integer.parseInt(dob.substring(0,dob.indexOf("-")));
        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }

    public Set<PetMedRecord> getMedRecordUrls() {
        return medRecordUrls;
    }

    public Set<PetXrayUrl> getXrayUrls() {
        return xrayUrls;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setMedRecordUrls(Set<PetMedRecord> medRecordUrls) {
        this.medRecordUrls = medRecordUrls;
    }

    public void setXrayUrls(Set<PetXrayUrl> xrayUrls) {
        this.xrayUrls = xrayUrls;
    }
}
