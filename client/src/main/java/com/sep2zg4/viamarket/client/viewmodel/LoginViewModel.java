package com.sep2zg4.viamarket.client.viewmodel;

import com.sep2zg4.viamarket.client.model.MarketplaceModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * A viewmodel for Login view
 *
 * @author Wojtek Rusinski
 * @version 1.6 - May 2022
 */

public class LoginViewModel
{
  private final StringProperty username;
  private final StringProperty password;
  private final MarketplaceModel model;

  public LoginViewModel(MarketplaceModel model)
  {
    this.model = model;

    this.username = new SimpleStringProperty("");
    this.password = new SimpleStringProperty("");
  }

  public StringProperty getUserName()
  {
    return username;
  }

  public StringProperty getUserPassword()
  {
    return password;
  }

  public boolean login() throws RemoteException, NotBoundException, SQLException
  {
    return model.login(Integer.parseInt(username.get()), password.get());
  }
}
