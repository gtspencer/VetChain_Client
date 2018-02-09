package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;

@Entity
@Table(name = "PET_MED_RECORD")
public class PetMedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MED_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "PET_ID")
    private Pet pet;

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
