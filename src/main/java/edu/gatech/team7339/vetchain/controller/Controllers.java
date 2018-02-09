package edu.gatech.team7339.vetchain.controller;

import edu.gatech.team7339.vetchain.bindingObject.Login;
import edu.gatech.team7339.vetchain.bindingObject.Register;
import edu.gatech.team7339.vetchain.model.Pet;
import edu.gatech.team7339.vetchain.model.User;
import edu.gatech.team7339.vetchain.repository.PetMedRecordRepo;
import edu.gatech.team7339.vetchain.repository.PetRepo;
import edu.gatech.team7339.vetchain.repository.PetXrayUrlRepo;
import edu.gatech.team7339.vetchain.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    /**
     * Login and Register Page
     * @param model
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndex(ModelMap model) {
        model.addAttribute("loginInfo",new Login());
        model.addAttribute("regInfo",new Register());
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
    public String processLogin(@ModelAttribute Login login,
                               ModelMap model,
                               BindingResult result,
                               RedirectAttributes redirect) {
        user = userRepo.findUserByUsernameAndPassword(login.getUsername(),login.getPassword());
        if(user != null){
            user.setPets(petRepo.findPetsByUserId(user.getId()));
            for (Pet pet: user.getPets()) {
                pet.setUser(user);
                pet.setXrayUrls(petXrayUrlRepo.findPetXrayUrlsByPetId(pet.getId()));
            }
            redirect.addFlashAttribute("userInfo", user);
            return "redirect:/" + user.getType() + "/" + user.getId();
        }
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
            userRepo.save(new User(reg.getUsername(),reg.getPassword(),reg.getEmail(),reg.getPhone()));
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
     * @param user
     * @return
     */
    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public String showHomePage(@PathVariable("type") String type,
                               @PathVariable("id") int id,
                               ModelMap model,
                               @ModelAttribute("userInfo") User user) {
        if (user != null) {
            if (type.equalsIgnoreCase("client")) {
                model.addAttribute("userInfo", user);
                return "client_home";
            } else {
                return "redirect:/"; // Fix this for doctor
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
    @RequestMapping(value = "/{type}/{id}/pets/{petId}/upload",method = RequestMethod.POST)
    public String showPets(@PathVariable("type") String type,
                           @PathVariable("id") Integer id,
                           @PathVariable("petId") Integer petId,
                           @RequestParam("fileupload")MultipartFile file,
                           ModelMap model) {
        if (!file.isEmpty()) {
            String name = file.getOriginalFilename();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/DD/YYYY");
                formatter.format(new Date());
                byte[] bytes = file.getBytes();
                Path path = Paths.get("./",id.toString(),petId.toString(),"1/1/2018",name);
                Files.write(path, bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/"+type+"/"+id+"/pets";
    }

    @RequestMapping(value = "/{type}/{id}/pets/addPet", method = RequestMethod.POST)
    public String addPet(@PathVariable("type") String type,
                         @PathVariable("id") int userId,
                         @RequestParam("name") String name,
                         @RequestParam("age") int age,
                         @RequestParam("species") String species,
                         @RequestParam("avatar") MultipartFile avatar,
                         @RequestParam("sex") String sex,
                         ModelMap model) {
        Pet pet = new Pet();
        pet.setUser(user);
        pet.setName(name);
        pet.setSpecies(species);
        pet.setAge(age);
        pet.setSex(sex);
        petRepo.save(pet);
        pet = petRepo.findPetByNameAndAge(name,age);
        System.out.print("New Pet: " +name+" "+species+" "+age+" "+sex+" "+avatar.getOriginalFilename());
        if(!avatar.isEmpty()) {
            try {
                Date date = new Date();
                byte[] bytes = avatar.getBytes();
                Path path = Paths.get(".\\"+userId+"\\"+pet.getId()+"\\"+date.getTime()+"\\"+name);
                Files.write(path, bytes);
                pet.setAvatarUrl(path.toString());
            } catch (IOException e) {
                pet.setAvatarUrl("/static/images/avatar.png");
            }
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
            return "doctorview";
        }
        return "redirect:/";
    }
}
