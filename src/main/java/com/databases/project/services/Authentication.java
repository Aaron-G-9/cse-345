package com.databases.project.services;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.List;

import com.databases.project.models.LoginForm;

@Service
public class Authentication implements IAuthentication {


  @Autowired
  private JdbcTemplate jdbcTemplate;

  public String getSecurityQuestion(String username){
    return jdbcTemplate.queryForObject("SELECT security_question from customer where username =?",
     new Object[] { username }, String.class);
  }

  public boolean userExists(String username){
    try {
      return jdbcTemplate.queryForObject("select exists(select 1 from customer where username = ?)",
          new Object[] { username }, Boolean.class);
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }

  public boolean isValidLogin(String username, String email, String password, String answer){
    try {
      System.out.println("\n");
      System.out.println(username + " " + email + " "+ password);
      return jdbcTemplate.queryForObject(
        "select exists(select 1 from customer" + 
        "  where username = ? and email=? and c_password=? )",
          new Object[] { username, email, password }, Boolean.class);
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }

}