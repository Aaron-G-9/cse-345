package com.databases.project.services;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
class ScheduledTasks {
  @Scheduled(fixedRate = 2628000)
  public static void updateInterest(){
    //Update interest
  }
}