package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Integer id;
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
    @Column(nullable = false, name = "DATE_CREATED")
    private String dateCreated;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> pets;


    public User(){
        this.type = "client";
    }
    public User(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.type = "client";
        pets = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        date.getTime();
        dateCreated = dateFormat.format(date).toString();
    }

    public String getCreatedDate() {
        return dateCreated;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public int getTotalPets() {
        return pets.size();
    }

    public void setPets(List<Pet> pets) {
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
