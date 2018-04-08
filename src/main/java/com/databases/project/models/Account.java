package com.databases.project.models;

public class Account {
  private String accountName;
  private enum accountType {basic_checking, joint_checking, student_checking, senior_checking, saving, money_market,  ira};
  private double earlyWithdrawFee;
  private double maxWithdraw;
  private int minAge;
  private int maxAge;
  private double interest;
  private long processingDelay;
  private double minimumBalance;
  private double minimumDeposit;

}