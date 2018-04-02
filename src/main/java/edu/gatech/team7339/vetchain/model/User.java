package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import javax.print.Doc;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Integer id;

    @Column(nullable = false, name = "FULL_NAME")
    private String fullname;

    @Column(nullable = false, unique = true, name = "USER_NAME")
    private String username;

    @Column(nullable = false, name = "PASSWORD")
    private String password;

    @Column(nullable = false, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PHONE")
    private String phone;

    @Column(nullable = false, name = "ACCOUNT_TYPE")
    private String type;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "DATE_CREATED")
    private Date dateCreated;

    @ManyToMany(mappedBy = "users",cascade = {
            CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Pet> pets;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN")
    private Date lastLogin;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Doctor doctor;

    public User(){
        this.type = "client";
    }
    public User(String fullname, String username, String password, String email, String phone, String type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.type = type;
        if(type.equals("doctor")){
            doctor = new Doctor();
            doctor.setUser(this);
        }
        pets = new HashSet<>();
        dateCreated = new Date();
        this.fullname = fullname;
    }

    public Doctor getDoctor() {
        if(type.equals("doctor")){
            return doctor;
        }
        else {
            return null;
        }
    }

    public void setDoctor(Doctor doctor) {
        if(type.equals("doctor")) {
            this.doctor = doctor;
        }
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCreatedDate() {
        return dateCreated;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public int getTotalPets() {
        return pets.size();
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
