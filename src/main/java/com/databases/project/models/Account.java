package com.databases.project.models;

import lombok.Data;

@Data
public class Account {
  private int accountId;
  private String accountName;
  private AccountType accountType;
  private double earlyWithdrawFee;
  private double maxWithdraw;
  private int minAge;
  private int maxAge;
  private double interest;
  private long processingDelay;
  private double minimumBalance;
  private double minimumDeposit;

  public Account(){}

  public Account(String accountName, AccountType accountType, double earlyWithdrawFee, double maxWithdraw,
                  int minAge, int maxAge, double interest, long processingDelay, double minimumBalance, double minimumDeposit){

    this.accountName = accountName;
    this.accountType = accountType;
    this.earlyWithdrawFee = earlyWithdrawFee;
    this.maxWithdraw = maxWithdraw;
    this.minAge = minAge;
    this.maxAge = maxAge;
    this.interest = interest;
    this.processingDelay = processingDelay;
    this.minimumBalance = minimumBalance;
    this.minimumDeposit = minimumDeposit;

  }

  public Account(int accountId, String accountName, AccountType accountType, double earlyWithdrawFee, double maxWithdraw,
                  int minAge, int maxAge, double interest, long processingDelay, double minimumBalance, double minimumDeposit){

    this.accountId = accountId;
    this.accountName = accountName;
    this.accountType = accountType;
    this.earlyWithdrawFee = earlyWithdrawFee;
    this.maxWithdraw = maxWithdraw;
    this.minAge = minAge;
    this.maxAge = maxAge;
    this.interest = interest;
    this.processingDelay = processingDelay;
    this.minimumBalance = minimumBalance;
    this.minimumDeposit = minimumDeposit;

  }
  

}