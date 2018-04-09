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
import java.sql.Timestamp;
import java.util.HashMap;
import java.math.BigDecimal;


@Service
public class BankService implements IBankService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<CreditCard> getCreditTypes(){
    RowMapper<CreditCard> rowMapper = (rs, rowNum) -> {
      return new CreditCard(rs.getInt("card_id"), rs.getString("card_name"), rs.getDouble("annual_fees"), 
        rs.getDouble("intro_apr"), rs.getInt("months_of_intro_apr"), rs.getDouble("regular_apr_min"), 
        rs.getDouble("regular_apr_max"), rs.getDouble("reward_rate_min"), rs.getDouble("reward_rate_max"), 
        rs.getDouble("reward_bonus"), rs.getDouble("late_fee"), CreditHistory.valueOf(rs.getString("credit_history")));
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

  public List<CreditCard> getUserCredit(String username){
    RowMapper<CreditCard> rowMapper = (rs, rowNum) -> {
      return new CreditCard(rs.getInt("card_id"), rs.getString("card_name"), rs.getDouble("annual_fees"), rs.getDouble("intro_apr"), 
                            rs.getInt("months_of_intro_apr"), rs.getDouble("regular_apr_min"), rs.getDouble("regular_apr_max"),
                            rs.getDouble("reward_rate_min"), rs.getDouble("reward_rate_max"), rs.getDouble("reward_bonus"),
                            rs.getDouble("late_fee"), CreditHistory.valueOf(rs.getString("credit_history")));
    };

    int customerId = jdbcTemplate.queryForObject(
                        "select customer_id from customer where username = ?", new Object[] { username }, Integer.class);

    String sql = "select * from has_credit_card inner join credit_cards on has_credit_card.card_id = credit_cards.card_id where has_credit_card.customer_id = ?;";
    List<CreditCard> creditList = jdbcTemplate.query(sql, rowMapper, customerId);

    return creditList;
  }

  public Map<String, List<Transaction>> getUserOverview(String username){
    List<Account> accountList = getUserAccounts(username);
    List<CreditCard> creditList = getUserCredit(username);

    int customerId = jdbcTemplate.queryForObject(
                        "select customer_id from customer where username = ?", new Object[] { username }, Integer.class);

    RowMapper<Transaction> transactionMapper = (rs, rowNum) -> {
      return new Transaction(rs.getInt("account_id"), rs.getDouble("creation_fee"), rs.getInt("credit_id"),
                             rs.getInt("loan_id"), rs.getDouble("old_balance"), rs.getDouble("delta"), 
                             rs.getDouble("new_balance"), rs.getTimestamp("transaction_time"));
    }; 

    String transactionSql = "select * from transactions where customer_id = ? and account_id = ?  order by transaction_time desc limit 5;";

    List<List<Transaction>> transactionList = new ArrayList<List<Transaction>>();
    for (Account account : accountList){
      transactionList.add(jdbcTemplate.query(transactionSql, transactionMapper, customerId, account.getAccountId()));
    }

    String creditTransactionSql = "select * from transactions where customer_id = ? and credit_id = ? order by transaction_time desc limit 5;";

    for (CreditCard card : creditList){
      transactionList.add(jdbcTemplate.query(creditTransactionSql, transactionMapper, customerId, card.getId()));
    }

    Map<String, List<Transaction>> endResult = new HashMap<>();

    for(int i = 0; i < accountList.size(); i++){
      endResult.put(accountList.get(i).getAccountName(), transactionList.get(i));
    }

    int j = accountList.size();
    for(int i = 0; i < creditList.size(); i++){
      endResult.put(creditList.get(i).getName() + " Credit", transactionList.get(j));
      j++;
    }

    return endResult;
  }

  public Map<String, List<String>> getAllUserAccountTypes(String username){
    RowMapper<String> creditMapper = (rs, rowNum) -> {
      return rs.getString("card_name") + " Credit";
    };

    RowMapper<String> accountMapper = (rs, rowNum) -> {
      return rs.getString("account_name");
    };

    int customerId = getCustomerID(username);

    String creditNameSql = "select * from has_credit_card inner join credit_cards on has_credit_card.card_id = credit_cards.card_id where has_credit_card.customer_id = ?;";
    List<String> creditList = jdbcTemplate.query(creditNameSql, creditMapper, customerId);

    String accountNameSql = "select * from has_account inner join accounts on has_account.account_id = accounts.account_id where has_account.customer_id = ?;";
    List<String> accountList = jdbcTemplate.query(accountNameSql, accountMapper, customerId);

    Map<String, List<String>> accountMap = new HashMap<>(3);

    accountMap.put("Regal Accounts", accountList);
    accountMap.put("Regal Credit", creditList);


    return accountMap;
  }


  public int setComplaint(int customerId, String employeeName, String subject, Timestamp fileDate) {
    try {
      int i = jdbcTemplate.update(
      "insert into complaints (customer_id, employee_name, subject, filed_on) values (?, ?, ?, ?)",
      new Object[] {customerId, employeeName, subject, fileDate});
      return i;
    } catch (Exception e) {
      throw e;
    }
  }


  public int getCustomerID(String username) {
    try {
      int customerId = jdbcTemplate.queryForObject(
          "select customer_id from customer where username = ?",
          new Object[] {username}, Integer.class);
      return customerId;
    } catch (Exception e) {
      throw e;
    }
  }

  public Map<String, Double> getCustomerBalances(String username){
    int customerId = getCustomerID(username);

    String accountSQL = "select account_name, new_balance from transactions inner join accounts on transactions.account_id = accounts.account_id " +
                          "where customer_id = ? and transactions.account_id is not null;";

    String creditSQL = "select card_name, new_balance from transactions inner join credit_cards on transactions.credit_id = credit_cards.card_id " +
      "where customer_id = ? and transactions.credit_id is not null;";

    List<Map<String, Object>> accountList = jdbcTemplate.queryForList(accountSQL, customerId);
    List<Map<String, Object>> creditList = jdbcTemplate.queryForList(creditSQL, customerId);

    Map<String, Double> resultMap = new HashMap<>();

    for (Map<String, Object> account : accountList){
      String key = (String) account.get("account_name");
      BigDecimal bigValue = (BigDecimal) account.get("new_balance");
      Double value = bigValue.doubleValue();
      resultMap.put(key, value);
    }

    for (Map<String, Object> card : creditList){
      String key = (String) card.get("card_name");
      BigDecimal bigValue = (BigDecimal) card.get("new_balance");
      Double value = bigValue.doubleValue();
      resultMap.put(key, value);
    }
    
    return resultMap;
  }

  public String addTransaction(String username, String type, int id, double amount){
    int customerId = getCustomerID(username);
    String sql;
    try{
      if (type.equals("account")){
        double oldBalance = jdbcTemplate.queryForObject("select new_balance from transactions where customer_id = ? and account_id = ? order by transaction_time desc limit 1",
          new Object[] {customerId, id}, Double.class );
        sql = "insert into transactions (account_id, customer_id, old_balance, delta) values (?, ?, ?, ?);";
        jdbcTemplate.update(sql, new Object[] {id, customerId, oldBalance, amount} );
      }else if (type.equals("credit")){
        double oldBalance = jdbcTemplate.queryForObject("select new_balance from transactions where customer_id = ? and card_id = ? order by transaction_time desc limit 1",
          new Object[] {customerId, id}, Double.class );
        sql = "insert into transactions (credit_id, customer_id, old_balance, delta) values (?, ?, ?, ?);";
        jdbcTemplate.update(sql, new Object[] {id, customerId, oldBalance, amount} );
      }
    }catch(Error e){
      return e.toString();
    }
    return "success";
  }
}