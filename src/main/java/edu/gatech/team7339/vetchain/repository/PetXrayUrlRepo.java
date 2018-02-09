package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.PetXrayUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetXrayUrlRepo extends CrudRepository<PetXrayUrl,Long>{
    public List<PetXrayUrl> findPetXrayUrlsByPetId(int petId);

}
