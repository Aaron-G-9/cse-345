package com.databases.project.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;

import com.databases.project.services.IAuthentication;
import com.databases.project.models.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Set;

import io.jsonwebtoken.impl.crypto.MacProvider;
import java.lang.Object;
import java.util.Date ;
import java.security.Key;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/")
public class Api {

    @Autowired IAuthentication authentication;

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
      return authentication.getCreditTypes();
    }

    @RequestMapping("/getJWT")
    public String test(@RequestParam(value = "i", required = false, defaultValue = "5") int i) {

      String compactJws = Jwts.builder()
        .setSubject("Joe")
        .signWith(SignatureAlgorithm.HS512, "cse3450")
        .compact();

      return (compactJws);
    }

    @RequestMapping("/authenticate")
    public String authenticate(@RequestParam(value = "username", required = true) String username, 
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "email", required = true) String email,
                               @RequestParam(value = "answer", required = true) String answer){


      if (authentication.isValidLogin(username, password, email.toLowerCase(), answer.toLowerCase())){
        Key key = MacProvider.generateKey();

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 12000000;
        Long warnMillis = expMillis - 300000;

        Date exp = new Date(expMillis);
        Date warn = new Date(warnMillis);

        String compactJws = Jwts.builder()
        .claim("username", username)
        .claim("warning-time", warn)
        .setExpiration(exp)
        .signWith(SignatureAlgorithm.HS512, key)
        .compact();


        return compactJws;

      }else{
        System.out.println("ERROR! INCORRECT LOGIN ATTEMPT");
        return "Invalid";
      }


    }

}