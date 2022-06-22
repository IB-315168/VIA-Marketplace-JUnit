package com.sep2zg4.viamarket.server.dao;

import com.sep2zg4.viamarket.model.User;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class LoginHandler
{

  private static LoginHandler loginHandler;
  private Connection connection;
  private final UserDAO userDAO = (UserDAO) DAOManager.getInstance().getDao(DAOManager.Table.User);

  private LoginHandler() throws SQLException, RemoteException
  {

  }

  public static LoginHandler getInstance() throws SQLException, RemoteException
  {
    if(loginHandler == null){
      loginHandler = new LoginHandler();
    }

    return loginHandler;
  }

  public void setConnection(Connection connection){
    this.connection = connection;
  }

  public User attemptLogin(int studentNumber, String password)
      throws SQLException, RemoteException
  {
    String query = "SELECT * FROM person WHERE studentNumber = ? AND password = ?";
    PreparedStatement selectStatement = connection.prepareStatement(query);
    selectStatement.setInt(1, studentNumber);
    selectStatement.setString(2, password);
    ResultSet res = selectStatement.executeQuery();
    if(res.next()){
      return userDAO.getById(Integer.parseInt(res.getString(1)));
    }
    return null;
  }
}
