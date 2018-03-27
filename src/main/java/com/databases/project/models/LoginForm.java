package com.databases.project.models;

import java.util.HashMap;
import lombok.Data;

@Data
public class LoginForm {
  
  private HashMap<Integer, String> questions;
  private HashMap<Integer, String> answers;
  private HashMap<Integer, String> pictures;

  public LoginForm(){}

}