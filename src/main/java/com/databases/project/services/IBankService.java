package com.databases.project.services;
import com.databases.project.models.*;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

public interface IBankService{

  public List<CreditCard> getCreditTypes();
  
  public List<Account> getAccountTypes();

  public Map<String, List<Transaction>> getUserOverview(String username);

  public List<Account> getUserAccounts(String username);

  public List<CreditCard> getUserCredit(String username);

  public int setComplaint(int customerId, String employeeName, String subject, Timestamp fileDate);

  public int getCustomerID(String username);

  public Map<String, List<String>> getAllUserAccountTypes(String username);

  public Map<String, Double> getCustomerBalances(String username);

  public String addTransaction(String username, String type, int id, double amount);

  public List<Transaction> getTransactionHistory(String username, String type, int id);

  public Map<Integer, String> getEmployees();

  public String punch(String type, int id);

  public Employee getEmployeeInfo(int id);

  public List<Timesheet> getPunchInfo(int id);

  public List<String> addAccountOptions();

  public String addAccount(String username, String name);

  public List<Loan> getUserLoans(String username);

}