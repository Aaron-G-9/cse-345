package com.databases.project.services;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.databases.project.models.CreditCard;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;


@Service
public class BankService implements IBankService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

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