package com.sep2zg4.viamarket.server.dao;

import com.sep2zg4.viamarket.model.User;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements Dao<User>
{
  private final DAOManager manager = DAOManager.getInstance();
  private final Connection connection;

  public UserDAO(Connection connection) throws SQLException
  {
    this.connection = connection;
  }

  @Override public User getById(int id) throws SQLException, RemoteException
  {
    String query = "SELECT * FROM person WHERE studentNumber = ?";
    PreparedStatement selectStatemenet = connection.prepareStatement(query);
    selectStatemenet.setInt(1, id);
    ResultSet res = selectStatemenet.executeQuery();
    res.next();
    return new User(res.getInt("studentNumber"), res.getString("fullName"),
        res.getString("phoneNumber"), res.getString("email"),
        res.getBoolean("isModerator"));
  }

  @Override public List<User> getAll() throws SQLException, RemoteException
  {
    List<User> listOfUser = new ArrayList<>();
    String query = "SELECT * FROM person";
    PreparedStatement selectStatement = connection.prepareStatement(query);
    ResultSet res = selectStatement.executeQuery();
    while (res.next())
    {
      listOfUser.add(
          new User(res.getInt("studentNumber"), res.getString("fullName"),
              res.getString("phoneNumber"), res.getString("email"),
              res.getBoolean("isModerator")));
    }
    return listOfUser;
  }

  @Override public void create(User user) throws SQLException
  {
    String query = "INSERT INTO person (studentNumber, fullName, phoneNumber, email, isModerator) VALUES ?,?,?,?,?";
    PreparedStatement insertStatement = connection.prepareStatement(query);
    insertStatement.setInt(1, user.getId());
    insertStatement.setString(2, user.getFullName());
    insertStatement.setString(3, user.getPhoneNumber());
    insertStatement.setString(4, user.getEmail());
    insertStatement.setBoolean(5, user.isModerator());
    insertStatement.executeUpdate();
  }

  @Override public void update(User user) throws SQLException
  {
    String query = "UPDATE person SET studentNumber=?,fullName=?,phoneNumber=?,email=?,isModerator=? WHERE studentNumber=? ;";
    PreparedStatement updateStatement = connection.prepareStatement(query);
    updateStatement.setInt(1, user.getId());
    updateStatement.setString(2, user.getFullName());
    updateStatement.setString(3, user.getPhoneNumber());
    updateStatement.setString(4, user.getEmail());
    updateStatement.setBoolean(5, user.isModerator());
    updateStatement.setInt(6, user.getId());
    updateStatement.executeUpdate();
  }

  @Override public void delete(User user) throws SQLException, RemoteException
  {
    String query = "DELETE FROM person WHERE studentNumber=?";
    PreparedStatement deleteStatement = connection.prepareStatement(query);
    deleteStatement.setInt(1, user.getId());
    deleteStatement.executeUpdate();
  }
}
