package com.databases.project.models;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Transaction {
 private Customer customer;
 private int customerId;
 private double creationFee;
 private CreditCard creditCard;
 private int creditCardId;
 private Loan loan;
 private int loanId;
 private double oldBalance;
 private double newBalance;
 private double delta;
 private Timestamp transactionTime;

 public Transaction(){}

 public Transaction(Customer customer, double creationFee, CreditCard creditCard, Loan loan, double oldBalance, double delta){
   this.customer = customer;
   this.creationFee = creationFee;
   this.creditCard = creditCard;
   this.loan = loan;
   this.oldBalance = oldBalance;
   this.delta = delta;
 }

 public Transaction(int customerId, double creationFee, int creditCardId, int loanId, double oldBalance, double delta, 
                    double newBalance, Timestamp transactionTime){
   this.customerId = customerId;
   this.creationFee = creationFee;
   this.creditCardId = creditCardId;
   this.loanId = loanId;
   this.oldBalance = oldBalance;
   this.delta = delta;
   this.newBalance = newBalance;
   this.transactionTime = transactionTime;
 }

}