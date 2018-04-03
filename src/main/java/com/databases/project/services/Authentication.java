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
import com.databases.project.models.CreditCard;

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

  public List<CreditCard> getCreditTypes(){
    RowMapper<CreditCard> rowMapper = (rs, rowNum) -> {
      return new CreditCard(rs.getInt("card_id"), rs.getString("card_name"), rs.getDouble("annual_fees"), 
        rs.getDouble("intro_apr"), rs.getInt("months_of_intro_apr"), rs.getDouble("regular_apr_min"), 
        rs.getDouble("regular_apr_max"), rs.getDouble("reward_rate_min"), rs.getDouble("reward_rate_max"), 
        rs.getDouble("reward_bonus"), rs.getDouble("late_fee"), rs.getString("credit_history"));
    };

    String sql = "SELECT * FROM credit_cards;";
    List<CreditCard> list = jdbcTemplate.query(sql, rowMapper);
    return list;
  }
}