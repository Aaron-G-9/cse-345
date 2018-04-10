package com.databases.project.services;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component
class ScheduledTasks {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Scheduled(fixedRate = 2628000)
  public static void updateInterest(){
    //Update interest
  }
}