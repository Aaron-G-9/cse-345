package com.databases.project.models;

import lombok.Data;
import java.sql.Date;

@Data
public class Customer {
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private String streetAddress;
  private String city;
  private String state;
  private String country;
  private String email;
  private String gender;
  private String phone;
  private String creditHistory;
  private String username;
  private String password;
  private String securityQuestion;
  private String securityAnswer;
  private double annualIncome;
  private String zipcode;

  public Customer(){}

  public Customer(String firstName, String lastName, Date dateOfBirth, String streetAddress, String city, 
                  String state, String country, String email, String phone, String username, String password, 
                  String securityQuestion, String securityAnswer, double annualIncome, String zipcode){
    
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.streetAddress = streetAddress;
    this.city = city;
    this.country = country;
    this.email = email;
    this.phone = phone;
    this.username = username;
    this.password = password;
    this.securityQuestion = securityQuestion;
    this.securityAnswer = securityAnswer;
    this.annualIncome = annualIncome;
    this.zipcode = zipcode;
    this.state = "MI";
  }

  public Customer(String firstName, String lastName, Date dateOfBirth, String streetAddress, String city, 
                  String state, String country, String email, String gender, String phone, String creditHistory, String username, String password, 
                  String securityQuestion, String securityAnswer, double annualIncome, String zipcode){
    
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.streetAddress = streetAddress;
    this.city = city;
    this.country = country;
    this.email = email;
    this.gender = gender;
    this.phone = phone;
    this.creditHistory = creditHistory;
    this.username = username;
    this.password = password;
    this.securityQuestion = securityQuestion;
    this.securityAnswer = securityAnswer;
    this.annualIncome = annualIncome;
    this.zipcode = zipcode;
    this.state = "MI";
  }

  public String toString(){
    return(firstName + " " + lastName + "\n" + username);
  }
}