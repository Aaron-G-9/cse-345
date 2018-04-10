package com.databases.project.models;

import java.util.Date;

import lombok.Data;

@Data
public class Employee {
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private String street;
  private String city;
  private String state;
  private String zipcode;
  private String phone;
  private String title;
  private double salary;
  private String officeLocation;
  private Date hireDate;

  public Employee(){}

  public Employee(String firstName, String lastName, Date dateOfBirth, String street, String city,
                  String state, String zipcode, String phone, String title, double salary,
                  String officeLocation, Date hireDate){
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zipcode = zipcode;
    this.phone = phone;
    this.title = title;
    this.salary = salary;
    this.officeLocation = officeLocation;
    this.hireDate = hireDate;

  }
}