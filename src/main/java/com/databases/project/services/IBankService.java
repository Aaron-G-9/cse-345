package com.databases.project.services;
import com.databases.project.models.*;

import java.util.List;
import java.util.Map;

public interface IBankService{

  public List<CreditCard> getCreditTypes();
  
  public List<Account> getAccountTypes();

  public Map<String, List<Transaction>> getUserOverview(String username);

  public List<Account> getUserAccounts(String username);
}