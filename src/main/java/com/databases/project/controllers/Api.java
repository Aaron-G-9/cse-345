package com.databases.project.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

import com.databases.project.services.IBankService;
import com.databases.project.services.IAuthentication;
import com.databases.project.models.*;

import java.util.Collections;
import java.util.Set;

import java.lang.Object;
import java.util.Date ;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/")
public class Api {

    @Autowired IAuthentication authentication;
    @Autowired IBankService bankservice;

    @RequestMapping("/getUserLogin")
    public Boolean getUserLogin(@RequestParam(value = "username", required = true) String username) {
      return authentication.userExists(username);
    }

    @RequestMapping("/getSecurityQuestion")
    public Set<String> getSecurityQuestion(@RequestParam(value = "username", required = true) String username) {
      return Collections.singleton(authentication.getSecurityQuestion(username));
    }

    @RequestMapping("/getCreditCards")
    public List<CreditCard> getCreditCards() {
      return bankservice.getCreditTypes();
    }

    @RequestMapping("/getAccountTypes")
    public List<Account> getAccountTypes() {
      return bankservice.getAccountTypes();
    }

    //@RequestMapping("/getUserAccounts")
    //public Map<Account, List<Transaction>> getUserAccounts(){
    //  
    //}

    @RequestMapping("/getUserAccounts")
    public Map<String, List<Transaction>> getUserAccounts(){
      return bankservice.getUserOverview("amgoodfellow");
      
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute Customer customer){
      String token = "";
      if (authentication.createUser(customer)){
        token = authentication.generateToken(customer.getUsername());
      }
      return token;
    }

    @RequestMapping("/authenticate")
    public String authenticate(@RequestParam(value = "username", required = true) String username, 
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "email", required = true) String email,
                               @RequestParam(value = "answer", required = true) String answer){


      if (authentication.isValidLogin(username, password, email.toLowerCase(), answer.toLowerCase())){
        String yourToken =  authentication.generateToken(username);
        System.out.println(yourToken);
        return yourToken;
      }else{
        System.out.println("ERROR! INCORRECT LOGIN ATTEMPT");
        return "Invalid";
      }


    }

}