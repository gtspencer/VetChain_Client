package edu.gatech.team7339.vetchain.controller;

import edu.gatech.team7339.vetchain.bindingObject.AppointmentInfo;
import edu.gatech.team7339.vetchain.bindingObject.PetInfo;
import edu.gatech.team7339.vetchain.model.Appointment;
import edu.gatech.team7339.vetchain.model.Pet;
import edu.gatech.team7339.vetchain.model.PetXrayUrl;
import edu.gatech.team7339.vetchain.model.User;
import edu.gatech.team7339.vetchain.repository.AppointmentRepo;
import edu.gatech.team7339.vetchain.repository.PetMedRecordRepo;
import edu.gatech.team7339.vetchain.repository.PetRepo;
import edu.gatech.team7339.vetchain.repository.PetXrayUrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class DoctorControllers {
    private User user;
    @Autowired
    private PetMedRecordRepo petMedRecordRepo;
    @Autowired
    private PetXrayUrlRepo petXrayUrlRepo;
    @Autowired
    private PetRepo petRepo;
    @Autowired
    private AppointmentRepo appointmentRepo;

    @RequestMapping(value = "/doctor/{userId}", method = RequestMethod.GET)
    public String showDoctorHome(@PathVariable("userId") String id,
                                 ModelMap model){
        if(model.containsAttribute("userInfo")) {
            user = (User) model.get("userInfo");
            return "doctor_home";
        } else if (user != null){
            model.addAttribute("userInfo",user);
            return "doctor_home";
        }
        return "error_page";
    }
    @RequestMapping(value = "/doctor/{userId}/pets", method = RequestMethod.GET)
    public String showDoctorPets(ModelMap model){
        if(user != null){
            for(Pet pet : user.getPets()) {
                pet.setMedRecordUrls(petMedRecordRepo.findPetMedRecordsByPetId(pet.getId()));
                pet.setXrayUrls(petXrayUrlRepo.findPetXrayUrlsByPetId(pet.getId()));
            }
            model.addAttribute("userInfo",user);
            model.addAttribute("petInfo",new PetInfo());
            return "doctor_pets";
        }
        return "error_page";
    }
    @RequestMapping(value = "/doctor/{userId}/schedule", method = RequestMethod.GET)
    public String showDoctorSchedule(   @PathVariable("userId") int id,
                                        ModelMap model){
        if(user != null){
            Date begin = new Date();
            Date end = new Date(begin.getTime() + 604800000);
            Set<Appointment> appointmentSet = appointmentRepo.findAllByDoctorIdAndDateBetweenOrderByTimeAsc(user.getDoctor().getId(),begin,end);
            ArrayList<String> next7Days = new ArrayList<>();
            next7Days.add("#");
            long milisPerDay =  86400000;
            long time = begin.getTime();
            Calendar calendar = Calendar.getInstance();
            for(int i = 0; i < 7; i++) {
                calendar.setTime(new Date(time+ i * milisPerDay));
                next7Days.add(calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.getDefault()));
            }
            ArrayList<ArrayList<ArrayList<String>>> schedule = new ArrayList<>();
            ArrayList<ArrayList<String>> appointments = new ArrayList<>();
            for (int i = 0 ; i < 9; i++){
                appointments.add(new ArrayList<>());
            }
            String meetingTime="NaN";
            for(Appointment p : appointmentSet){
                if(meetingTime.equals("NaN")){
                    appointments.get(0).add(0, p.getTime().toString());
                    meetingTime = p.getTime().toString();
                } else if(!meetingTime.equalsIgnoreCase(p.getTime().toString())) {
                    schedule.add(appointments);
                    appointments = new ArrayList<>();
                    for(int i = 0; i < 9; i++){
                        appointments.add(new ArrayList<>());
                    }
                    appointments.get(0).add(0, p.getTime().toString());
                }
                    int index = next7Days.indexOf(p.getDayOfWeek());
                    ArrayList<String> appointmentDetails = new ArrayList<>();
                    appointmentDetails.add("Pet: " + p.getPet().getName());
                    appointmentDetails.add("Reason: " +p.getReason());
                    appointments.add(index, appointmentDetails);
            }
            schedule.add(appointments);
            model.addAttribute("daysOfWeek",next7Days);
            model.addAttribute("userInfo",user);
            model.addAttribute("schedule",schedule);
            model.addAttribute("AppointmentInfo",new AppointmentInfo());
            return "doctor_schedule";
        }
        return "error_page";
    }

    @RequestMapping(value = "/doctor/{userId}/pets/addPet", method = RequestMethod.POST)
    public String addPet(@PathVariable("userId") String userId,
                         @ModelAttribute("petInfo") PetInfo petInfo,
                         @RequestParam("unit") String unit,
                         ModelMap model) {
        if(user != null) {
            Pet pet = new Pet();
            pet.getUsers().add(user);
            pet.setName(petInfo.getName());
            pet.setBreed(petInfo.getBreed());
            pet.setGender(petInfo.getGender());
            pet.setInsuranceCarrier(petInfo.getInsCarrier().isEmpty() ? "Unknown" : petInfo.getInsCarrier());
            pet.setLicense(petInfo.getLicense().isEmpty() ? "Unknown" : petInfo.getLicense());
            pet.setInsuranceNum(petInfo.getInsNum().isEmpty() ? "Unknown" : petInfo.getInsNum());
            pet.setDob(petInfo.getDob());
            pet.setWeight(petInfo.getWeight() + " " + unit);
            pet.setMicrochipNum(petInfo.getMicrochip().isEmpty() ? "Unknown" : petInfo.getMicrochip());
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
            return "redirect:/doctor/" + userId + "/pets";
        }
        return "error_page";
    }
    /**
     * Upload Xray images
     * @param userId
     * @param petId
     * @param file
     * @param model
     * @return
     */
    @RequestMapping(value = "/doctor/{userId}/pets/{petId}/addXray",method = RequestMethod.POST)
    public String showPets(@PathVariable("userId") Integer userId,
                           @PathVariable("petId") Integer petId,
                           @RequestParam("xrayFile")MultipartFile file,
                           ModelMap model) {
        if(user != null) {
            Pet pet = null;
            for (Pet p : user.getPets()) {
                if (p.getId() == petId) {
                    pet = p;
                    break;
                }
            }
            if (!file.isEmpty() && pet != null) {
                String name = file.getOriginalFilename();
                try {
                    Date date = new Date();
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get("/files", userId.toString(), petId.toString(), Long.toString(date.getTime()), name);
                    Files.createDirectories(path.getParent());
                    Files.write(path, bytes);
                    PetXrayUrl xray = new PetXrayUrl();
                    xray.setPet(pet);
                    xray.setDate(date);
                    xray.setUrl("/images/" + userId.toString() + "/" + petId.toString() + "/" + Long.toString(date.getTime()) + "/" + name);
                    pet.getXrayUrls().add(xray);
                    petXrayUrlRepo.save(xray);
                    petRepo.save(pet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "redirect:/doctor/" + userId + "/pets";
        }
        return "error_page";
    }
}
