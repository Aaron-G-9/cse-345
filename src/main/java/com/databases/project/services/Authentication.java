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
import java.util.Date;
import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.databases.project.models.LoginForm;
import com.databases.project.models.CreditCard;
import com.databases.project.models.Customer;

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

  public boolean isValidLogin(String username, String password, String email, String answer){
    try {
      return jdbcTemplate.queryForObject(
        "select exists(select 1 from customer" + 
        "  where username = ? and email=? and c_password=? and security_answer = ? )",
          new Object[] { username, email, password, answer }, Boolean.class);
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }

  public String generateToken(String username){
    //The JWT signature algorithm we will be using to sign the token
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    
    //We will sign our JWT with our ApiKey secret
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("regal");
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    long nowMillis = System.currentTimeMillis();
    long expMillis = nowMillis + 12000000;
    Long warnMillis = expMillis - 300000;

    Date exp = new Date(expMillis);
    Date warn = new Date(warnMillis);
    boolean isAdmin = false;
    if (username.equals("amgoodfellow") || username.equals("calee")){
      isAdmin = true;
    }

    String compactJws = Jwts.builder()
    .claim("username", username)
    .claim("warning-time", warn)
    .claim("is-admin", isAdmin)
    .setExpiration(exp)
    .signWith(signatureAlgorithm, signingKey)
    .compact();

    return compactJws;
  }

  public boolean createUser(Customer customer){
    try{
      jdbcTemplate.update(
        "INSERT INTO customer (first_name, last_name, date_of_birth, street, city, state, country, " +
          "email, gender, phone, credit_status, username, c_password, security_question, security_answer, " +
          "annual_income, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        customer.getFirstName(), customer.getLastName(), customer.getDateOfBirth(), customer.getStreetAddress(),
        customer.getCity(), customer.getState(), customer.getCountry(), customer.getEmail(), 
        customer.getGender(), customer.getPhone(), customer.getCreditHistory(), customer.getUsername(), customer.getPassword(), 
        customer.getSecurityQuestion(), customer.getSecurityAnswer(), customer.getAnnualIncome(), customer.getZipcode()
      );

      int customerId = jdbcTemplate.queryForObject(
          "select customer_id from customer where username = ?",
          new Object[] {customer.getUsername()}, Integer.class);

      jdbcTemplate.update("insert into has_account (customer_id, account_id) values (?, ?)", customerId, 1);
      jdbcTemplate.update(
        "insert into transactions (account_id, customer_id, old_balance, delta) values (1, ?, 0, 5)",
        customerId
      );
      return true;
    }catch(Error e){
      System.out.println(e);
      return false;
    }
  }
}