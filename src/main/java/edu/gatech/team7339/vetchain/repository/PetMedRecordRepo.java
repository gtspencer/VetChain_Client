package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.PetMedRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetMedRecordRepo extends CrudRepository<PetMedRecord, Long> {
    public List<PetMedRecord> findPetMedRecordsByPetId(int petId);
}
