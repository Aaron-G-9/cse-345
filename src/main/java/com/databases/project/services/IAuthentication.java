package com.databases.project.services;
import com.databases.project.models.CreditCard;
import com.databases.project.models.Customer;

import java.util.List;

public interface IAuthentication{

  public String getSecurityQuestion(String username);

  public boolean userExists(String username);

  public boolean isValidLogin(String username, String password, String email, String answer);

  public String generateToken(String username);

  public boolean createUser(Customer customer);
}