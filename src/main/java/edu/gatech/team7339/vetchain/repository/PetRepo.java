package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepo extends CrudRepository<Pet,Long> {
    public List<Pet> findPetsByUserId(int userId);
    public Pet findPetByUserIdAndId(int userId, int petId);
    public Pet findPetById(int petId);
    public Pet findPetByNameAndDob(String name, String dob);
    public Pet findPetByName(String name);
}
