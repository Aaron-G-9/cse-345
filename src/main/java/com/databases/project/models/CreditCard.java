package com.databases.project.models;

import java.util.HashMap;

import lombok.Data;

@Data
public class CreditCard {
  private int id;
  private String name;
  private double annualFee;
  private double introApr;
  private int monthsOfIntro;
  private double regularAprMin;
  private double regularAprMax;
  private double rewardRateMin;
  private double rewardRateMax;
  private double rewardBonus;
  private double lateFee;
  private String creditHistory;

  public CreditCard(){}

  public CreditCard(int id, String name, double annualFee, double introApr, int monthsOfIntro,
    double regularAprMin, double regularAprMax, double rewardRateMin, double rewardRateMax, 
    double rewardBonus, double lateFee, String creditHistory){

      this.id = id;
      this.name = name;
      this.annualFee = annualFee;
      this.introApr = introApr;
      this.monthsOfIntro = monthsOfIntro;
      this.regularAprMin = regularAprMin;
      this.regularAprMax = regularAprMax;
      this.rewardRateMin = rewardRateMin;
      this.rewardRateMax = rewardRateMax;
      this.rewardBonus = rewardBonus;
      this.lateFee = lateFee;
      this.creditHistory = creditHistory;
  }

}