package edu.gatech.team7339.vetchain.validator;

import edu.gatech.team7339.vetchain.bindingObject.Register;
import edu.gatech.team7339.vetchain.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterValidator implements Validator {

    @Autowired
    UserRepo userRepo;
    @Override
    public boolean supports(Class<?> aClass) {
        return Register.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Register reg = (Register) o;
        if(!reg.getPassword().equals(reg.getConfirmPassword())) {
            errors.rejectValue("password","password.NotMatch",new Object[]{"password"},"Password and Confirm Password are not match");
        }
        if(userRepo.existsUserByUsername(reg.getUsername())){
            errors.rejectValue("username","username.exists",new Object[]{"username"},"Username is already in use");
        }
    }
}
