package com.databases.project.models;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Timesheet {
  private Timestamp timestamp;
  private String punchType;

  public Timesheet(){}

  public Timesheet(Timestamp timestamp, String punchType){
    this.timestamp = timestamp;
    this.punchType = punchType;
  }
}