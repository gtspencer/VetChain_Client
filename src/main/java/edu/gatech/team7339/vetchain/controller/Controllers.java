package edu.gatech.team7339.vetchain.controller;

import edu.gatech.team7339.vetchain.bindingObject.Login;
import edu.gatech.team7339.vetchain.bindingObject.PetInfo;
import edu.gatech.team7339.vetchain.bindingObject.Register;
import edu.gatech.team7339.vetchain.blockchain.Block;
import edu.gatech.team7339.vetchain.blockchain.BlockChain;
import edu.gatech.team7339.vetchain.blockchain.BlockUtil;
import edu.gatech.team7339.vetchain.model.*;
import edu.gatech.team7339.vetchain.repository.*;
import edu.gatech.team7339.vetchain.validator.LoginValidator;
import edu.gatech.team7339.vetchain.validator.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

@Controller
public class Controllers {
    public BlockUtil blockUtil = new BlockUtil();
    private static LinkedList<Block> blockchain = new LinkedList<>();


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
    @Autowired
    RecentActivityRepo activityRepo;
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

        // Load blockchain from the file

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
    public String processLogin(@Valid @ModelAttribute("loginInfo") Login login,
                               ModelMap model,
                               BindingResult result,
                               RedirectAttributes redirect) {


        // If block ID in chain then set login values to username and pass


        String userHash = blockUtil.calculateHash(login.getUsername() + login.getPassword());
        Boolean hashValid = false;

        // TODO: Pull and read the blockchain file

        for (Block aBlockchain : blockchain) {
            if (userHash.equals(aBlockchain.hash)) {
                hashValid = true;
                System.out.printf("Username and password in blockchain: %s\n", userHash);
            }
        }
        if (hashValid) {
            loginValidator.validate(login,result);
            if(!result.hasErrors()) {

                user = userRepo.findUserByUsernameAndPassword(login.getUsername(), login.getPassword());
                user.setPets(petRepo.findAllByUsers(user));
                if (user.getLastLogin() == null) {
                    user.setLastLogin(new Date());
                }
                redirect.addFlashAttribute("userInfo", user);
                return "redirect:/" + user.getType() + "/" + user.getId();
            }
        }

        System.out.println("Username and password not in blockchain\n");
        redirect.addFlashAttribute("org.springframework.validation.BindingResult.loginInfo",result);
        redirect.addFlashAttribute("loginInfo",login);
        return "redirect:/";
    }

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String showAdminPage(ModelMap model){
        if(user != null) {
            model.addAttribute("userInfo", user);
            model.addAttribute("doctorsInfo", userRepo.findAllByType("doctor"));
            model.addAttribute("clientsInfo", userRepo.findAllByType("client"));
            model.addAttribute("petsInfo", petRepo.findAll());
            model.addAttribute("activities", activityRepo.findAll());
            return "admin_home";
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
            registerValidator.validate(reg,result);
            if(result.hasErrors()) {
                redirect.addFlashAttribute("org.springframework.BindingResult.regInfo",result);
                redirect.addFlashAttribute("regInfo",reg);
            } else {

                if (blockchain.size() == 0) {
                    blockchain.add(new Block("root", ""));
                }
                // TODO: Load the blockchain
                blockchain.add(new Block((reg.getUsername() + reg.getPassword()), blockchain.get(blockchain.size()-1).hash));
                // TODO: Save the blockchain to a file

                userRepo.save(new User(reg.getFirstname()+" "+ reg.getLastname(),reg.getUsername(), reg.getPassword(), reg.getEmail(), reg.getPhone(),reg.getType()));

                backupBlockchain(blockchain);
            }
        }
        return "redirect:/";
    }
    @RequestMapping(value = "/register/validator/username", method = RequestMethod.POST)
    @ResponseBody
    public boolean usernameValidation(@RequestParam("username") String username) {
        return userRepo.existsUserByUsername(username);
    }
    @RequestMapping(value = "/register/vallidator/password", method = RequestMethod.POST)
    @ResponseBody
    public boolean passwordValidator(@RequestParam("password") String password,
                                     @RequestParam("confirmPassword") String confirmPassword){
        return password.equals(confirmPassword);
    }
    /**
     * Logout
     * @param model
     * @return
     */
    @RequestMapping("/logout")
    public String logout(ModelMap model) {
        userRepo.save(user);
        user= null;
        return "redirect:/";
    }

    public void backupBlockchain(LinkedList<Block> backupChain) {
        BlockChain chainObject = new BlockChain();
        chainObject.setChain(backupChain);
        try {
            FileOutputStream fos = new FileOutputStream("keep.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(chainObject);
            fos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
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




}
