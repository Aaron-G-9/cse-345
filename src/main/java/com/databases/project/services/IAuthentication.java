package com.databases.project.services;

import java.util.HashMap;

public interface IAuthentication{

  public String getSecurityQuestion(String username);

  public boolean userExists(String username);

  public boolean isValidLogin(String username, String email, String password, String answer);
}