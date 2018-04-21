package edu.gatech.team7339.vetchain.controller;

import edu.gatech.team7339.vetchain.bindingObject.SharePetInfo;
import edu.gatech.team7339.vetchain.model.Appointment;
import edu.gatech.team7339.vetchain.model.Pet;
import edu.gatech.team7339.vetchain.model.RecentActivity;
import edu.gatech.team7339.vetchain.model.User;
import edu.gatech.team7339.vetchain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Controller
public class ClientControllers {
    private User user;
    @Autowired
    private PetXrayUrlRepo petXrayUrlRepo;
    @Autowired
    private PetMedRecordRepo petMedRecordRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private RecentActivityRepo recentActivityRepo;
    @RequestMapping(value = "/client/{userId}",method = RequestMethod.GET)
    public String showClientPage(ModelMap model, @PathVariable("userId") String userId){
        if(model.containsAttribute("userInfo")){
            user = (User) model.get("userInfo");
        } else if (user != null) {
            model.addAttribute("userInfo",user);
        } else {
            return "error_page";
        }
        Date begin = new Date();
        long miliPerWeek = 604800000;
        Date end = new Date(begin.getTime()+miliPerWeek);
        Set<Appointment> upcomingAppointments = new HashSet<>();
        for (Pet pet : user.getPets()) {
            Set<Appointment> petAppointments = appointmentRepo.findAllByPetIdAndDateBetweenOrderByTimeAsc(pet.getId(), begin, end);
            if(!petAppointments.isEmpty()){
                Iterator<Appointment> iterator = petAppointments.iterator();
                if(iterator.hasNext()) {
                    upcomingAppointments.add(iterator.next());
                }
            }
        }
        if(!upcomingAppointments.isEmpty()){
            model.addAttribute("upComingAppointments",upcomingAppointments);
        }
        return "client_home";
    }
    /**
     * Client's pet view
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/client/{id}/pets", method = RequestMethod.GET)
    public String showPets(@PathVariable("id") int id,
                           ModelMap model) {
        if(user != null) {
            //Fetch Pet's Xray and Medical record for viewing
            for (Pet pet: user.getPets()){
                pet.setXrayUrls(petXrayUrlRepo.findPetXrayUrlsByPetId(pet.getId()));
                pet.setMedRecordUrls(petMedRecordRepo.findPetMedRecordsByPetId(pet.getId()));
            }
            model.addAttribute("userInfo", user);
            return "client_pets";
        }
        return "error_page";
    }

    /**
     * Client's doctor view
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/client/{id}/doctors",method = RequestMethod.GET)
    public String showDoctorView(@PathVariable("id") int id,
                                 ModelMap model) {
        if(user !=null){
            for(Pet pet : user.getPets()){
                pet.setUsers(userRepo.findAllByPets(pet));
            }
            model.addAttribute("userInfo",user);
            model.addAttribute("doctors", userRepo.findAllByType("doctor"));
            model.addAttribute("sharePetInfo",new SharePetInfo(user.getTotalPets()));
            return "client_doctors";
        }
        return "error_page";
    }

    @RequestMapping(value = "/client/{userId}/doctors/{docId}/share",method = RequestMethod.POST)
    public String sharePet(@PathVariable("userId") int userId,
                           @PathVariable("docId") int docId,
                           @ModelAttribute("sharePetInfo") SharePetInfo sharePetInfo,
                           ModelMap model){
        boolean fshare = true;
        if(user != null) {
            User doctor = userRepo.findUserById(docId);
            for (Pet pet : user.getPets()) {
                for (String id : sharePetInfo.getPetIds()) {
                    if (id != null && pet.getId() == Integer.parseInt(id)) {
                        for(User u : pet.getUsers()){
                            if(u.getId() == docId) {
                                fshare = false;
                                break;
                            }
                        }
                        if(fshare) {
                            pet.getUsers().add(doctor);
                            doctor.getPets().add(pet);
                        }
                    }
                }
            }
            userRepo.save(doctor);
            RecentActivity newActivity = new RecentActivity(user, new Date(),"Share pets with doctor:"+doctor.getFullname());
            recentActivityRepo.save(newActivity);
            return "redirect:/client/"+userId+"/doctors";
        }
        return "error_page";
    }
    @RequestMapping(value = "/client/{id}/history", method = RequestMethod.GET)
    public String showHistory(@PathVariable("id") int userid,
                       ModelMap model) {
        Set<RecentActivity> activities = recentActivityRepo.findAllByUserId(userid);
        if(activities != null){
            user.setActivities(activities);
        } else {
            user.setActivities(new HashSet<>());
        }
        model.addAttribute("userInfo",user);
        return "client_history";
    }
}
