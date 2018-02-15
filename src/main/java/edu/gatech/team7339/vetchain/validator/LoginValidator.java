package edu.gatech.team7339.vetchain.validator;

import edu.gatech.team7339.vetchain.bindingObject.Login;
import edu.gatech.team7339.vetchain.model.User;
import edu.gatech.team7339.vetchain.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator{

    @Autowired
    UserRepo userRepo;
    @Override
    public boolean supports(Class<?> aClass) {
        return Login.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Login login = (Login) o;
        User user = userRepo.findUserByUsernameAndPassword(login.getUsername(),login.getPassword());
        if (user == null){
            errors.rejectValue("username","username.exists",new Object[]{"username"},"Wrong username or password");
        }
    }
}
