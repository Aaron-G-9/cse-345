package com.databases.project.services;
import com.databases.project.models.CreditCard;

import java.util.List;

public interface IBankService{

  public List<CreditCard> getCreditTypes();
}