package com.databases.project.models;

import lombok.Data;

@Data
public class Loan {
  private int id;
  private String loanName;
  private double monthlyPayment;
  private String creditHistory;

  public Loan(){}

  public Loan(int id, String loanName, double monthlyPayment, String creditHistory){
    this.id = id;
    this.loanName = loanName;
    this.monthlyPayment = monthlyPayment;
    this.creditHistory = creditHistory;
  }
}