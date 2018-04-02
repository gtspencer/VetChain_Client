package edu.gatech.team7339.vetchain.controller;

import edu.gatech.team7339.vetchain.bindingObject.AppointmentInfo;
import edu.gatech.team7339.vetchain.bindingObject.PetInfo;
import edu.gatech.team7339.vetchain.model.Appointment;
import edu.gatech.team7339.vetchain.model.Pet;
import edu.gatech.team7339.vetchain.model.PetXrayUrl;
import edu.gatech.team7339.vetchain.model.User;
import edu.gatech.team7339.vetchain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private UserRepo userRepo;

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
    public String showDoctorPets(   @PathVariable("userId") String userId,
                                    ModelMap model){
        if(user != null){
            for(Pet pet : user.getPets()) {
                pet.setMedRecordUrls(petMedRecordRepo.findPetMedRecordsByPetId(pet.getId()));
                pet.setXrayUrls(petXrayUrlRepo.findPetXrayUrlsByPetId(pet.getId()));
            }
            model.addAttribute("clients", userRepo.findAllByType("client"));
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
            ArrayList<ArrayList<String>> next7Days = new ArrayList<>();
            ArrayList<String> day = new ArrayList<>();
            day.add("#");
            day.add("");
            next7Days.add(day);
            long milisPerDay =  86400000;
            long time = begin.getTime();
            Calendar calendar = Calendar.getInstance();
            for(int i = 0; i < 7; i++) {
                calendar.setTime(new Date(time + i * milisPerDay));
                day = new ArrayList<>();
                day.add(calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.getDefault()));
                day.add((calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
                next7Days.add(day);
            }
            if(appointmentRepo.existsByDoctorIdAndDateBetween(user.getDoctor().getId(),begin,end)) {
                Set<Appointment> appointmentSet = appointmentRepo.findAllByDoctorIdAndDateBetweenOrderByTimeAsc(user.getDoctor().getId(), begin, end);
                ArrayList<ArrayList<ArrayList<String>>> schedule = new ArrayList<>();
                ArrayList<ArrayList<String>> appointments = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    appointments.add(new ArrayList<>());
                }
                String meetingTime = "NaN";
                for (Appointment p : appointmentSet) {
                    if (meetingTime.equals("NaN")) {
                        appointments.get(0).add(0, p.getTime().toString());
                        meetingTime = p.getTime().toString();
                    } else if (!meetingTime.equalsIgnoreCase(p.getTime().toString())) {
                        schedule.add(appointments);
                        appointments = new ArrayList<>();
                        for (int i = 0; i < 9; i++) {
                            appointments.add(new ArrayList<>());
                        }
                        appointments.get(0).add(0, p.getTime().toString());
                    }
                    int index = 0;
                    for (ArrayList<String> d : next7Days) {
                        if (d.contains(p.getDayOfWeek())) {
                            break;
                        }
                        index++;
                    }
                    ArrayList<String> appointmentDetails = new ArrayList<>();
                    appointmentDetails.add("Pet: " + p.getPet().getName());
                    appointmentDetails.add("Reason: " + p.getReason());
                    appointments.add(index, appointmentDetails);
                }
                schedule.add(appointments);
                model.addAttribute("schedule",schedule);
            }
            model.addAttribute("daysOfWeek",next7Days);
            model.addAttribute("userInfo",user);
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
            pet.getUsers().add(userRepo.findUserById(petInfo.getOwnerId()));
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
    @RequestMapping(value = "doctor/{docId}/schedule/addAppointment",method = RequestMethod.POST)
    public String addAppointment(@PathVariable("docId") int docId,
                                 @ModelAttribute("AppointmentInfo") AppointmentInfo newAppointment,
                                 ModelMap model) {
        if(user != null) {
            try {
                Appointment appointment = new Appointment();
                DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat time = new SimpleDateFormat("hh:mm");
                appointment.setDate(date.parse(newAppointment.getDate()));
                appointment.setTime(time.parse(newAppointment.getTime()));
                appointment.setDoctor(user.getDoctor());
                appointment.setPet(petRepo.findPetById(Integer.parseInt(newAppointment.getPetId())));
                appointment.setReason(newAppointment.getReason());
                appointmentRepo.save(appointment);
                return "redirect:/doctor/" + docId + "/schedule";
            }catch (ParseException e){
                System.out.println("Parse failed!");
                return "error_page";
            }
        } else {
            return "error_page";
        }
    }
}
