package com.databases.project.models;

import lombok.Data;

@Data
public class Transaction {
 private Customer customer;
 private double creationFee;
 private CreditCard creditCard;
 private Loan loan;
 private double oldBalance;
 private double delta;

 public Transaction(){}

 public Transaction(Customer customer, double creationFee, CreditCard creditCard, Loan loan, double oldBalance, double delta){
   this.customer = customer;
   this.creationFee = creationFee;
   this.creditCard = creditCard;
   this.loan = loan;
   this.oldBalance = oldBalance;
   this.delta = delta;
 }

}