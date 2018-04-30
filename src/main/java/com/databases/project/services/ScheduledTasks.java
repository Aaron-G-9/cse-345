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
    int a, b = 2, c = 3, d = 3, e, f = 3, g, h, i = 0, j, k, l, m, n, o, p, q, r, s, t, u, v;
    
    //For b, c, d, f in-memory
 
    q = a + b;
    r = c + d;
    s = e + f;
    t = s + g;
    u = h + i;
    … = … ;


  }
}