package edu.gatech.team7339.vetchain.bindingObject;

import java.util.ArrayList;
import java.util.List;

public class SharePetInfo {
    List<String> petIds;

    public List<String> getPetIds(){
        return petIds;
    }

    public void setPetIds(List<String> petIds) {
        this.petIds = petIds;
    }

    public SharePetInfo(int size){
        petIds = new ArrayList<>(size);
    }

    public SharePetInfo(){
        petIds = new ArrayList<>();
    }
}
