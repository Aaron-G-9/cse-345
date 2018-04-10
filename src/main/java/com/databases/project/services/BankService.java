package com.databases.project.services;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.RowMapper;

import com.databases.project.models.*;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
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

  public List<Loan> getUserLoans(String username){
    RowMapper<Loan> rowMapper = (rs, rowNum) -> {
      return new Loan(rs.getInt("loan_id"), rs.getString("loan_name"), rs.getDouble("monthly_payment_minimum"), rs.getString("necessary_credit") ); 
    };

    int customerId = jdbcTemplate.queryForObject(
                        "select customer_id from customer where username = ?", new Object[] { username }, Integer.class);

    String sql = "select * from has_loan inner join loans on has_loan.loan_id = loans.loan_id where has_loan.customer_id = ?;";
    List<Loan> creditList = jdbcTemplate.query(sql, rowMapper, customerId);

    return creditList;
  }

  public Map<String, List<Transaction>> getUserOverview(String username){
    List<Account> accountList = getUserAccounts(username);
    List<CreditCard> creditList = getUserCredit(username);
    List<Loan> loanList = getUserLoans(username);

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

    String loanTransactionSql = "select * from transactions where customer_id = ? and loan_id = ? order by transaction_time desc limit 5;";

    for (Loan loan : loanList){
      transactionList.add(jdbcTemplate.query(loanTransactionSql, transactionMapper, customerId, loan.getId()));
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

    for(int i = 0; i < loanList.size(); i++){
      endResult.put(loanList.get(i).getLoanName(), transactionList.get(j));
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

  public List<Transaction> getTransactionHistory(String username, String type, int id){
    int customerId = getCustomerID(username);

    String accountSql = "";
    RowMapper<Transaction> rowMapper;
    rowMapper = (rs, rowNum) -> {
    return new Transaction(rs.getInt("customer_id"), rs.getDouble("creation_fee"), rs.getInt("credit_id"),
                            rs.getInt("loan_id"), rs.getDouble("old_balance"), rs.getDouble("delta"), 
                            rs.getDouble("new_balance"), rs.getTimestamp("transaction_time"));
    };

    if (type.equals("account")){
      accountSql = "select * from transactions where customer_id = ? and account_id = ? order by transaction_time desc limit 25";
    }else if (type.equals("credit")){
      accountSql = "select * from transactions where customer_id = ? and credit_id = ? order by transaction_time desc limit 25";
      rowMapper = (rs, rowNum) -> {
      return new Transaction(rs.getInt("customer_id"), rs.getDouble("creation_fee"), 
                              rs.getInt("loan_id"), rs.getDouble("old_balance"), rs.getDouble("delta"), 
                              rs.getDouble("new_balance"), rs.getTimestamp("transaction_time"), rs.getInt("account_id"));
      };
    }else if (type.equals("loan")){
      accountSql = "select * from transactions where customer_id = ? and loan_id = ? order by transaction_time desc limit 25";
      rowMapper = (rs, rowNum) -> {
      return new Transaction(rs.getInt("loan_id"), rs.getInt("customer_id"), rs.getDouble("creation_fee"), 
                              rs.getDouble("old_balance"), rs.getDouble("delta"), 
                              rs.getDouble("new_balance"), rs.getTimestamp("transaction_time"));
      };

    }

    List<Transaction> transactions = jdbcTemplate.query(accountSql, rowMapper, customerId, id);



    return transactions;
  }

  public String punch(String username, String type){


    return("Successful punch!");
  }


  public Map<Integer, String> getEmployees(){
    return jdbcTemplate.query("select employee_id, concat(first_name, ' ', last_name) as name from employee", 
    (ResultSet rs) -> {
      HashMap<Integer, String> results = new HashMap<>();
      while (rs.next()) {
          results.put(rs.getInt("employee_id"), (rs.getString("name")));
      }
      return results;
    });
  }

  public String punch(String type, int id){
    if (!(type.equals("clock-in") || type.equals("clock-out"))){
      return "Incorrect type";
    }

    String sql = "insert into timestamps (employee_id, punch_type) VALUES (?, ?)";
    jdbcTemplate.update(sql, new Object[] {id, type} );

    return "Your punch has been submitted";

  }

  public Employee getEmployeeInfo(int id){
    String sql = "select * from employee where employee_id = ?";
    RowMapper<Employee> rowMapper;
    rowMapper = (rs, rowNum) -> {
    return new Employee(rs.getString("first_name"), rs.getString("last_name"), rs.getDate("date_of_birth"),
                        rs.getString("street"), rs.getString("city"), rs.getString("state"), rs.getString("zipcode"),
                        rs.getString("phone"), rs.getString("title"), rs.getDouble("salary"), 
                        rs.getString("office_location"), rs.getDate("hire_date"));
    };
    List<Employee> employees = jdbcTemplate.query(sql, rowMapper, id);

    return employees.get(0);

  }

  public List<Timesheet> getPunchInfo(int id){
    String sql = "select * from timestamps where employee_id = ? order by time desc limit 20";
    RowMapper<Timesheet> rowMapper;
    rowMapper = (rs, rowNum) -> {
    return new Timesheet(rs.getTimestamp("time"), rs.getString("punch_type"));
    };
    List<Timesheet> timesheet = jdbcTemplate.query(sql, rowMapper, id);

    return timesheet;
  }

  public List<String> addAccountOptions(){
    String sql = "select account_name from accounts union select concat(card_name, ' Card')  from credit_cards union select loan_name from loans";
    RowMapper<String> rowMapper;
    rowMapper = (rs, rowNum) -> {
    return rs.getString("account_name");
    };
    return jdbcTemplate.query(sql, rowMapper);
  }



  private boolean goodEnoughCredit(String user, String needed){
    int userScore, neededScore;
    if (user.equals("excellent")){
      userScore = 3;
    }else if(user.equals("good")){
      userScore = 2;
    }else{
      userScore = 1;
    }

    if (needed.equals("excellent")){
      neededScore = 3;
    }else if(needed.equals("good")){
      neededScore = 2;
    }else{
      neededScore = 1;
    }

    if (userScore >= neededScore){
      return true;
    }

    return false;
  }



  public String addAccount(String username, String name){
    String sql;

    int customerId = getCustomerID(username);

    RowMapper<Customer> customerMapper = (rs, rowNum) -> {
      return new Customer(rs.getString("first_name"), rs.getString("last_name"), rs.getDate("date_of_birth"), rs.getString("street"),
                          rs.getString("city"), rs.getString("state"), rs.getString("country"), rs.getString("email"), rs.getString("gender"), rs.getString("phone"),
                          rs.getString("credit_status"), rs.getString("username"), rs.getString("c_password"), rs.getString("security_question"),
                          rs.getString("security_answer"), rs.getDouble("annual_income"), rs.getString("zipcode"));
    };

    List<Customer> customers = jdbcTemplate.query("select * from customer where username=?", customerMapper, username);
    Customer customer = customers.get(0);

    if (name.contains("Card")){
      sql = "select * from credit_cards where card_name = ?";
      RowMapper<CreditCard> rowMapper;
      rowMapper = (rs, rowNum) -> {
        return new CreditCard(rs.getInt("card_id"), rs.getString("card_name"), rs.getDouble("annual_fees"), 
          rs.getDouble("intro_apr"), rs.getInt("months_of_intro_apr"), rs.getDouble("regular_apr_min"), 
          rs.getDouble("regular_apr_max"), rs.getDouble("reward_rate_min"), rs.getDouble("reward_rate_max"), 
          rs.getDouble("reward_bonus"), rs.getDouble("late_fee"), CreditHistory.valueOf(rs.getString("credit_history")));
      };
      List<CreditCard> cards = jdbcTemplate.query("select * from credit_cards where card_name = ?", rowMapper, name);
      CreditCard card = cards.get(0);
      if (!goodEnoughCredit(customer.getCreditHistory(), card.getCreditHistory().toString())){
        return "You do not have sufficient credit";
      }

      jdbcTemplate.update("insert into has_credit_card (customer_id, card_id) values (?, ?)", customerId, card.getId());
      jdbcTemplate.update(
        "insert into transactions (credit_id, customer_id, old_balance, delta) values (?, ?, 0, 2000)",
         card.getId(), customerId 
      );

      return "Success!";

    }else if (name.contains("loan")){
      sql = "select * from loans where loan_name = ?";
      RowMapper<Loan> rowMapper;
      rowMapper = (rs, rowNum) -> {
        return new Loan(rs.getInt("loan_id"), rs.getString("loan_name"), rs.getDouble("monthly_payment_minimum"), rs.getString("necessary_credit") ); 
      };
      List<Loan> loans = jdbcTemplate.query(sql, rowMapper, name);
      Loan loan = loans.get(0);

      if (!goodEnoughCredit(customer.getCreditHistory(), loan.getCreditHistory())){
        return "You do not have sufficient credit";
      }

      jdbcTemplate.update("insert into has_loan (customer_id, loan_id) values (?, ?)", customerId, loan.getId());
      jdbcTemplate.update(
        "insert into transactions (loan_id, customer_id, old_balance, delta) values (?, ?, 0, -2000)",
         loan.getId(), customerId 
      );


    }else{
      sql= "select * from accounts where account_name = ?";
      RowMapper<Account> rowMapper;
      rowMapper = (rs, rowNum) -> {
        return new Account(rs.getInt("account_id"), rs.getString("account_name"),  AccountType.valueOf(rs.getString("account_type")), 
          rs.getDouble("early_withdraw_fee"), rs.getDouble("max_withdraw"), rs.getInt("min_age"), rs.getInt("max_age"), 
          rs.getDouble("interest"), rs.getLong("processing_delay"), rs.getDouble("minimum_balance"), 
          rs.getDouble("minimum_deposit"));
      };
      List<Account> accounts = jdbcTemplate.query("select * from accounts where account_name = ?", rowMapper, name);
      Account account = accounts.get(0);

      java.sql.Date dob = customer.getDateOfBirth();
      LocalDate dateOfBirth = dob.toLocalDate();
      Period period = Period.between(dateOfBirth, LocalDate.now());
      
      if (period.getYears() < account.getMinAge()){
        return "You are too young";
      }else if (period.getYears() > account.getMaxAge()){
        return "You are too old";
      }
      

      jdbcTemplate.update("insert into has_account (customer_id, account_id) values (?, ?)", customerId, account.getAccountId());
      jdbcTemplate.update(
        "insert into transactions (account_id, customer_id, old_balance, delta) values (?, ?, 0, 10)",
         account.getAccountId(), customerId 
      );

    }



    return "Success";
  }
}