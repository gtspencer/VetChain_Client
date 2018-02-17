package edu.gatech.team7339.vetchain.controller;

import edu.gatech.team7339.vetchain.bindingObject.Login;
import edu.gatech.team7339.vetchain.bindingObject.PetInfo;
import edu.gatech.team7339.vetchain.bindingObject.Register;
import edu.gatech.team7339.vetchain.model.Pet;
import edu.gatech.team7339.vetchain.model.PetMedRecord;
import edu.gatech.team7339.vetchain.model.PetXrayUrl;
import edu.gatech.team7339.vetchain.model.User;
import edu.gatech.team7339.vetchain.repository.PetMedRecordRepo;
import edu.gatech.team7339.vetchain.repository.PetRepo;
import edu.gatech.team7339.vetchain.repository.PetXrayUrlRepo;
import edu.gatech.team7339.vetchain.repository.UserRepo;
import edu.gatech.team7339.vetchain.validator.LoginValidator;
import edu.gatech.team7339.vetchain.validator.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class Controllers {
    private User user;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PetRepo petRepo;
    @Autowired
    PetXrayUrlRepo petXrayUrlRepo;
    @Autowired
    PetMedRecordRepo petMedRecordRepo;
    @Autowired
    LoginValidator loginValidator;
    @Autowired
    RegisterValidator registerValidator;
    /**
     * Login and Register Page
     * @param model
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndex(ModelMap model) {
        if (!model.containsAttribute("loginInfo")) {
            model.addAttribute("loginInfo", new Login());
        }
        if(!model.containsAttribute("regInfo")) {
            model.addAttribute("regInfo", new Register());
        }
        return "index";
    }

    /**
     * Login Authentication
     * @param login
     * @param model
     * @param result
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin(@Valid @ModelAttribute Login login,
                               ModelMap model,
                               BindingResult result,
                               RedirectAttributes redirect) {
        loginValidator.validate(login,result);
        if(!result.hasErrors()){
            user = userRepo.findUserByUsernameAndPassword(login.getUsername(),login.getPassword());
            user.setPets(petRepo.findPetsByUserId(user.getId()));
            for (Pet pet: user.getPets()) {
                pet.setUser(user);
                pet.setXrayUrls(petXrayUrlRepo.findPetXrayUrlsByPetId(pet.getId()));
                for(PetXrayUrl xrayUrl:pet.getXrayUrls()) {
                    xrayUrl.setPet(pet);
                }
                pet.setMedRecordUrls(petMedRecordRepo.findPetMedRecordsByPetId(pet.getId()));
                for(PetMedRecord medRecord: pet.getMedRecordUrls()){
                    medRecord.setPet(pet);
                }
            }
            redirect.addFlashAttribute("userInfo", user);
            return "redirect:/" + "client" + "/" + user.getId();
        }
        redirect.addFlashAttribute("org.springframework.validation.BindingResult.loginInfo",result);
        redirect.addFlashAttribute("loginInfo",login);
        return "redirect:/";
    }

    /**
     * Register user
     * @param reg
     * @param model
     * @param result
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String processRegister(@ModelAttribute Register reg,
                                  ModelMap model,
                                  BindingResult result,
                                  RedirectAttributes redirect) {
        if(user != null) {
            return "redirect:/";//Fix this later
        } else {
            registerValidator.validate(reg,result);
            if(result.hasErrors()) {
                redirect.addFlashAttribute("org.springframework.BindingResult.regInfo",result);
                redirect.addFlashAttribute("regInfo",reg);
            } else {
                userRepo.save(new User(reg.getUsername(), reg.getPassword(), reg.getEmail(), reg.getPhone()));
            }
        }
        return "redirect:/";
    }

    /**
     * Logout
     * @param model
     * @return
     */
    @RequestMapping("/logout")
    public String logout(ModelMap model) {
        user = null;
        return "redirect:/";
    }

    /**
     * User's homepage
     * @param type
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public String showHomePage(@PathVariable("type") String type,
                               @PathVariable("id") int id,
                               ModelMap model) {
        if (user != null) {
            if (type.equalsIgnoreCase("client")) {
                model.addAttribute("userInfo", user);
                model.addAttribute("petInfo",new PetInfo());
                return "client_home";
            } else {
                return "redirect:/"; // Fix this for doctor
            }
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/{type}/{id}/search", method = RequestMethod.GET)
    public String showSearch(@PathVariable("type") String type,
                             @PathVariable("id") int id,
                             ModelMap model) {
        if (user != null) {
            if (type.equalsIgnoreCase("client")) {
                model.addAttribute("userInfo", user);
                model.addAttribute("petInfo",new PetInfo());
                return "client_search";
            }
        }
        return "redirect:/";
    }

    /**
     * Client's pet view
     * @param type
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/{type}/{id}/pets")
    public String showPets(@PathVariable("type") String type,
                           @PathVariable("id") int id,
                           ModelMap model) {
        if(user != null) {
            model.addAttribute("userInfo", user);
            model.addAttribute("petInfo",new PetInfo());
            return "petview";
        }
        return "redirect:/";
    }

    /**
     * Upload Xray images
     * @param type
     * @param id
     * @param petId
     * @param file
     * @param model
     * @return
     */
    @RequestMapping(value = "/{type}/{id}/pets/{petId}/addXray",method = RequestMethod.POST)
    public String showPets(@PathVariable("type") String type,
                           @PathVariable("id") Integer id,
                           @PathVariable("petId") Integer petId,
                           @RequestParam("xrayFile")MultipartFile file,
                           ModelMap model) {
        Pet pet = null;
        for (Pet p: user.getPets()) {
            if(p.getId() == petId) {
                pet = p;
                break;
            }
        }
        if (!file.isEmpty() && pet != null) {
            String name = file.getOriginalFilename();
            try {
                Date date = new Date();
                byte[] bytes = file.getBytes();
                Path path = Paths.get("/files",id.toString(),petId.toString(),Long.toString(date.getTime()),name);
                Files.createDirectories(path.getParent());
                Files.write(path, bytes);
                PetXrayUrl xray = new PetXrayUrl();
                xray.setPet(pet);
                xray.setDate(date);
                xray.setUrl("/images/"+id.toString()+"/"+petId.toString()+"/"+Long.toString(date.getTime())+"/"+name);
                pet.getXrayUrls().add(xray);
                petXrayUrlRepo.save(xray);
                petRepo.save(pet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/"+type+"/"+id+"/pets";
    }

    @RequestMapping(value = "/{type}/{id}/pets/addPet", method = RequestMethod.POST)
    public String addPet(@PathVariable("type") String type,
                         @PathVariable("id") String userId,
                         @ModelAttribute("petInfo") PetInfo petInfo,
                         @RequestParam("gender") String gender,
                         @RequestParam("unit") String unit,
                         ModelMap model) {

        Pet pet = new Pet(user);
        pet.setName(petInfo.getName());
        pet.setBreed(petInfo.getBreed());
        pet.setGender(gender);
        pet.setInsuranceCarrier(petInfo.getInsCarrier().isEmpty()? "Unknown":petInfo.getInsCarrier());
        pet.setLicense(petInfo.getLicense().isEmpty()? "Unknown" : petInfo.getLicense());
        pet.setInsuranceNum(petInfo.getInsNum().isEmpty()? "Unknown" : petInfo.getInsNum());
        pet.setDob(petInfo.getDob());
        pet.setWeight(petInfo.getWeight() + " " + unit);
        pet.setMicrochipNum(petInfo.getMicrochip().isEmpty()? "Unknown" : petInfo.getMicrochip());
        petRepo.save(pet);
        pet = petRepo.findPetByNameAndDob(pet.getName(), pet.getDob());
        if (!petInfo.getAvatarUrl().isEmpty()) {
            String avatarFileName = petInfo.getAvatarUrl().getOriginalFilename();
            try {
                byte[] bytes = petInfo.getAvatarUrl().getBytes();
                Path path = Paths.get("/files", userId, Integer.toString(pet.getId()), "avatar", avatarFileName);
                Files.createDirectories(path.getParent());
                Files.write(path, bytes);
                pet.setAvatarUrl("/images/" + userId + "/" + pet.getId() + "/avatar/" + avatarFileName);
            } catch (IOException e) {
                pet.setAvatarUrl("/static/images/avatar.png");
            }
            petRepo.save(pet);
            user.getPets().add(pet);
        }
        return "redirect:/"+type+"/"+userId+"/pets";
    }
    /**
     * Client's doctor view
     * @param type
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/{type}/{id}/doctors",method = RequestMethod.GET)
    public String showDoctorView(@PathVariable("type") String type,
                                 @PathVariable("id") int id,
                                 ModelMap model) {
        if(user !=null){
            model.addAttribute("userInfo",user);
            model.addAttribute("doctorList", userRepo.findUserByType("doctor"));
            return "doctorview";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/{type}/{id}/pets/sharePet", method = RequestMethod.POST)
    public String sharePet(@PathVariable("type") String type,
                         @PathVariable("userId") int userId,
                         @PathVariable("doctorId") int doctorId,
                         @ModelAttribute("petInfo") PetInfo petInfo,
                         ModelMap model) {

        User doctor = userRepo.findUserById(doctorId);

        return "redirect:/";
    }
}
