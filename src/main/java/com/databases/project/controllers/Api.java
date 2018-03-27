package com.databases.project.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;

import com.databases.project.services.IAuthentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Set;


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

    @RequestMapping("/getJWT")
    public String test(@RequestParam(value = "i", required = false, defaultValue = "5") int i) {

      String compactJws = Jwts.builder()
        .setSubject("Joe")
        .signWith(SignatureAlgorithm.HS512, "cse3450")
        .compact();

      return (compactJws);
    }

    @RequestMapping("/authenticate")
    public Boolean authenticate(@RequestParam(value = "username", required = true) String username, 
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "email", required = true) String email,
                               @RequestParam(value = "answer", required = true) String answer,
                                HttpServletRequest request){

      return authentication.isValidLogin(username, email, password, answer);

    }

}