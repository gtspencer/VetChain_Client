package edu.gatech.team7339.vetchain.repository;

import edu.gatech.team7339.vetchain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    boolean existsUserByUsernameAndPassword(String username, String password);
    boolean existsUserByUsername(String username);
    User findUserByUsernameAndPassword(String username, String password);
    User findUserByUsername(String username);
    User findUserById(int id);
}
