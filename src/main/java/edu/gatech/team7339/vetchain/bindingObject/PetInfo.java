package edu.gatech.team7339.vetchain.bindingObject;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class PetInfo {
    private int ownerId;
    private String name;
    private String dob;
    private String weight;
    private String license;
    private String insNum;
    private String insCarrier;
    private String microchip;
    private String breed;
    private MultipartFile avatarUrl;
    private String gender;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
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

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAvatarUrl(MultipartFile avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public MultipartFile getAvatarUrl() {
        return avatarUrl;
    }

    public String getInsCarrier() {
        return insCarrier;
    }

    public String getInsNum() {
        return insNum;
    }

    public String getMicrochip() {
        return microchip;
    }

    public void setInsCarrier(String insCarrier) {
        this.insCarrier = insCarrier;
    }

    public void setInsNum(String insNum) {
        this.insNum = insNum;
    }

    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }
}
