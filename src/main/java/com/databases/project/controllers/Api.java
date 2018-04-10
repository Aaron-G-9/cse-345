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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import io.jsonwebtoken.impl.crypto.MacProvider;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.HashMap;


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

    @RequestMapping("/getUserAccounts")
    public List<Account> getUserAccounts(HttpServletRequest request){
      String jwt = request.getHeader("Authorization");

      Claims claims = Jwts.parser()         
       .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
       .parseClaimsJws(jwt).getBody();

      return bankservice.getUserAccounts(claims.get("username").toString());
    } 

    @RequestMapping("/getCustomerBalances")
    public Map<String, Double> getCustomerBalances(HttpServletRequest request){
      String jwt = request.getHeader("Authorization");

      Claims claims = Jwts.parser()         
       .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
       .parseClaimsJws(jwt).getBody();

      return bankservice.getCustomerBalances(claims.get("username").toString());
    } 

    @RequestMapping("/addTransaction")
    public String addTransaction(
      @RequestParam(value = "type", required = true) String type, 
      @RequestParam(value = "id", required = true) int id, 
      @RequestParam(value = "amount", required = true) double amount, 
      HttpServletRequest request){

      String jwt = request.getHeader("Authorization");

      Claims claims = Jwts.parser()         
        .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
        .parseClaimsJws(jwt).getBody();
      
      return bankservice.addTransaction(claims.get("username").toString(), type, id, amount);

    }

    @RequestMapping("/getUserCredit")
    public List<CreditCard> getUserCredit(HttpServletRequest request){
      String jwt = request.getHeader("Authorization");

      Claims claims = Jwts.parser()         
       .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
       .parseClaimsJws(jwt).getBody();

      return bankservice.getUserCredit(claims.get("username").toString());
    } 
      
    @RequestMapping("/getAccountSummary")
    public Map<String, List<Transaction>> getAccountSummary(HttpServletRequest request){
      String jwt = request.getHeader("Authorization");

      Claims claims = Jwts.parser()         
       .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
       .parseClaimsJws(jwt).getBody();
      return bankservice.getUserOverview(claims.get("username").toString());
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

  @RequestMapping(value = "/complaint", produces = "application/json")
  public Map<String, String> complaint(
      @RequestParam(value = "message", required = true) String message,
      @RequestParam(value = "customer", required = true) String customer,
      @RequestParam(value = "employee", required = true) String employee) {
    Map<String, String> map = new HashMap<>();
    Claims claims =
        Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
            .parseClaimsJws(customer)
            .getBody();
    int customerId = bankservice.getCustomerID((String) claims.get("username"));
    LocalDateTime now = LocalDateTime.now();
    int i = bankservice.setComplaint(customerId, employee, message, Timestamp.valueOf(now));
    if(i == 0) {
      map.put("status", "failure");
      return map;
    } else {
      map.put("status", "success");
      return map;
    }
  }

  @RequestMapping("/allUserAccounts")
  public Map<String, List<String>> getAllUserAccountTypes(HttpServletRequest request){
    String jwt = request.getHeader("Authorization");

    Claims claims = Jwts.parser()         
      .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
      .parseClaimsJws(jwt).getBody();
    return bankservice.getAllUserAccountTypes(claims.get("username").toString());
  }


  @RequestMapping("/transactionHistory")
  public List<Transaction> transactionHistory(
      @RequestParam(value = "type", required = true) String type,
      @RequestParam(value = "id", required = true) int id,
      HttpServletRequest request){

    String jwt = request.getHeader("Authorization");

    Claims claims = Jwts.parser()         
      .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
      .parseClaimsJws(jwt).getBody();
    return bankservice.getTransactionHistory(claims.get("username").toString(), type, id);
  }

  @RequestMapping("/punch")
  public String punch(
      @RequestParam(value = "type", required = true) String type,
      @RequestParam(value = "id", required = true) int id){
    
    return bankservice.punch(type, id);
  }

  @RequestMapping("/getEmployees")
  public Map<Integer, String> getEmployees(){
    return bankservice.getEmployees();
  }

  @RequestMapping("/getEmployeeInfo")
  public Employee getEmployeeInfo(@RequestParam(value="id", required=true) int id){
    return bankservice.getEmployeeInfo(id);
  }

  @RequestMapping("/getPunchInfo")
  public List<Timesheet> getPunchInfo(@RequestParam(value="id", required=true) int id){
    return bankservice.getPunchInfo(id);
  }

  @RequestMapping("/addAccountOptions")
  public List<String> addAccountOptions(){
    return bankservice.addAccountOptions();
  }

  @RequestMapping("/addAccount")
  public String addAccount(
      @RequestParam(value = "name", required = true) String name,
      HttpServletRequest request){
    String jwt = request.getHeader("Authorization");

    Claims claims = Jwts.parser()         
      .setSigningKey(DatatypeConverter.parseBase64Binary("regal"))
      .parseClaimsJws(jwt).getBody();
    return bankservice.addAccount(claims.get("username").toString(), name);
  }

}