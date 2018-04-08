package com.databases.project.services;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.RowMapper;

import com.databases.project.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;;


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

  public List<Account> getAccountTypes(){
    RowMapper<Account> rowMapper = (rs, rowNum) -> {
      return new Account(rs.getString("account_name"),  AccountType.valueOf(rs.getString("account_type")), 
        rs.getDouble("early_withdraw_fee"), rs.getDouble("max_withdraw"), rs.getInt("min_age"), rs.getInt("max_age"), 
        rs.getDouble("interest"), rs.getLong("processing_delay"), rs.getDouble("minimum_balance"), 
        rs.getDouble("minimum_deposit"));
    };

    String sql = "SELECT * FROM accounts;";
    List<Account> list = jdbcTemplate.query(sql, rowMapper);
    return list;

  }

  public List<Account> getUserAccounts(String username){
    RowMapper<Account> rowMapper = (rs, rowNum) -> {
      return new Account(rs.getInt("account_id"), rs.getString("account_name"),  AccountType.valueOf(rs.getString("account_type")), 
        rs.getDouble("early_withdraw_fee"), rs.getDouble("max_withdraw"), rs.getInt("min_age"), rs.getInt("max_age"), 
        rs.getDouble("interest"), rs.getLong("processing_delay"), rs.getDouble("minimum_balance"), 
        rs.getDouble("minimum_deposit"));
    };

    int customerId = jdbcTemplate.queryForObject(
                        "select customer_id from customer where username = ?", new Object[] { username }, Integer.class);

    String sql = "select * from has_account inner join accounts on has_account.account_id = accounts.account_id where has_account.customer_id = ?;";
    List<Account> accountList = jdbcTemplate.query(sql, rowMapper, customerId);

    return accountList;
  }

  public Map<String, List<Transaction>> getUserOverview(String username){
    List<Account> accountList = getUserAccounts(username);

    RowMapper<Transaction> transactionMapper = (rs, rowNum) -> {
      System.out.println(rs.getDouble("delta"));
      return new Transaction(rs.getInt("account_id"), rs.getDouble("creation_fee"), rs.getInt("credit_id"),
                             rs.getInt("loan_id"), rs.getDouble("old_balance"), rs.getDouble("delta"), 
                             rs.getDouble("new_balance"), rs.getTimestamp("transaction_time"));
    }; 

    String transactionSql = "select * from transactions where customer_id = ? and account_id = ?  order by transaction_time limit 5";

    List<List<Transaction>> transactionList = new ArrayList<List<Transaction>>();
    for (Account account : accountList){
      transactionList.add(jdbcTemplate.query(transactionSql, transactionMapper, customerId, account.getAccountId()));
    }

    Map<String, List<Transaction>> endResult = new HashMap<>();

    for(int i = 0; i < accountList.size(); i++){
      endResult.put(accountList.get(i).getAccountName(), transactionList.get(i));
    }

    return endResult;
  }
}