package com.databases.project.services;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
class ScheduledTasks {
  @Scheduled(fixedRate = 2000)
  public void scheduleTaskWithFixedRate() {
      System.out.println("Fixed Rate Task");
      System.out.println("WOAH");
  }
}