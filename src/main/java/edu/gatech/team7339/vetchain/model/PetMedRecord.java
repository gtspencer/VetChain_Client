package edu.gatech.team7339.vetchain.model;

import javax.persistence.*;
import java.util.Date;

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
    @Column(name = "EXAMINER")
    private String examiner;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VISIT_DATE")
    private Date timeVisit;
    @Column(name = "TREATMENT")
    private String treatment;
    @Column(name = "CLINIC")
    private String clinic;
    @Column(name = "TEST")
    private String test;
    @Column(name = "MEDICATION")
    private String medication;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NEXT_VISIT")
    private Date nextVisitDate;
    @Column(name = "SYMPTOM")
    private String symptom;


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
