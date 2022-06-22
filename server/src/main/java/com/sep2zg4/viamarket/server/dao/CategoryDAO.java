package com.sep2zg4.viamarket.server.dao;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements Dao<String>
{
  private final Connection connection;

  public CategoryDAO(Connection connection)
  {
    this.connection = connection;
  }

  @Override public String getById(int id) throws SQLException
  {
    String query = "SELECT * FROM category WHERE id = ?";
    PreparedStatement selectStatement = connection.prepareStatement(query);
    selectStatement.setInt(1, id);
    ResultSet res = selectStatement.executeQuery();
    res.next();
    return res.getString("name");
  }

  @Override public List<String> getAll() throws SQLException
  {
    ArrayList<String> categories = new ArrayList<>();
    String query = "SELECT * FROM category";
    PreparedStatement selectStatemenet = connection.prepareStatement(query);
    ResultSet res = selectStatemenet.executeQuery();
    while (res.next())
    {
      categories.add(res.getString("name"));
    }
    return categories;
  }

  @Override public void create(String s) throws SQLException
  {
    String query = "INSERT INTO category (name) VALUES (?)";
    PreparedStatement insertStatement = connection.prepareStatement(query);
    insertStatement.setString(1, s);
    insertStatement.executeUpdate();
  }

  @Override public void update(String s) throws SQLException
  {
    String query = "UPDATE category SET name=? WHERE name=? ;";
    PreparedStatement updateStatement = connection.prepareStatement(query);
    updateStatement.setString(1, s);
    updateStatement.setString(2, s);
    updateStatement.executeUpdate();
  }

  @Override public void delete(String s) throws SQLException, RemoteException
  {
    String query = "DELETE FROM category WHERE name=?";
    PreparedStatement deleteStatement = connection.prepareStatement(query);
    deleteStatement.setString(1, s);
    deleteStatement.executeUpdate();
  }
}
