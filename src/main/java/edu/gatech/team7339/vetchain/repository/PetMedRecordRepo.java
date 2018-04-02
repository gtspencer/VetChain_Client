package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.PetMedRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PetMedRecordRepo extends CrudRepository<PetMedRecord, Long> {
    public Set<PetMedRecord> findPetMedRecordsByPetId(int petId);
}
