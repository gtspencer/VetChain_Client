package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.PetXrayUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PetXrayUrlRepo extends CrudRepository<PetXrayUrl,Long>{
    public Set<PetXrayUrl> findPetXrayUrlsByPetId(int petId);

}
