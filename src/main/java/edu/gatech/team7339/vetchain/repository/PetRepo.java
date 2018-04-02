package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.Pet;
import edu.gatech.team7339.vetchain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PetRepo extends CrudRepository<Pet,Long> {
    public Pet findPetById(int petId);
    public Pet findPetByNameAndDob(String name, String dob);
    public Set<Pet> findAll();
    public Set<Pet> findAllByUsers(User user);
}
