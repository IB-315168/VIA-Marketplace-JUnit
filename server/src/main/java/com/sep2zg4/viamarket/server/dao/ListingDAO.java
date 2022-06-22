package com.sep2zg4.viamarket.server.dao;

import com.sep2zg4.viamarket.model.Listing;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListingDAO implements Dao<Listing>
{
  private final DAOManager manager = DAOManager.getInstance();
  private final Connection connection;
  private final UserDAO userDAO;

  public ListingDAO(Connection connection) throws SQLException, RemoteException
  {
    this.connection = connection;
    this.userDAO = (UserDAO) manager.getDao(DAOManager.Table.User);
  }

  @Override public Listing getById(int id)
      throws SQLException, RemoteException
  {
    String query = "SELECT * FROM listing WHERE id = ?";
    PreparedStatement selectStatement = connection.prepareStatement(query);
    selectStatement.setInt(1, id);
    ResultSet res = selectStatement.executeQuery();
    res.next();
    String category = getCategoryNameFromId(res.getInt("idCategory"));
    return new Listing(res.getInt("id"), category, res.getString("title"),
        res.getString("description"), res.getDouble("price"),
        res.getString("city"), res.getString("condition"),
        userDAO.getById(Integer.parseInt(res.getString("studentNumber"))));
  }

  @Override public List<Listing> getAll() throws SQLException, RemoteException
  {
    List<Listing> listOfListing = new ArrayList<>();
    String query = "SELECT * FROM listing";
    PreparedStatement selectStatement = connection.prepareStatement(query);
    ResultSet res = selectStatement.executeQuery();
    while (res.next())
    {
      listOfListing.add(new Listing(res.getInt("id"), getCategoryNameFromId(res.getInt("idCategory")) , res.getString("title"),
          res.getString("description"), res.getDouble("price"),
          res.getString("city"), res.getString("condition"),
          userDAO.getById(Integer.parseInt(res.getString("studentNumber")))));
    }
    return listOfListing;
  }

  @Override public void create(Listing listing) throws SQLException
  {
    String query = "INSERT INTO listing (title,description,price,city,condition,studentNumber,idCategory) VALUES (?,?,?,?,?,?,?)";
    PreparedStatement insertStatement = connection.prepareStatement(query);
    insertStatement.setString(1, listing.getTitle());
    insertStatement.setString(2, listing.getDescription());
    insertStatement.setDouble(3, listing.getPrice());
    insertStatement.setString(4, listing.getCity());
    insertStatement.setString(5, listing.getCondition());
    insertStatement.setInt(6, listing.getSeller().getId());
    insertStatement.setInt(7, getIdCategoryFromName(listing.getCategoryName()));
    insertStatement.executeUpdate();
    System.out.println("DAO done");
  }

  @Override public void update(Listing listing) throws SQLException
  {
    String query = "UPDATE listing SET title=?,description=?,price=?,city=?,condition=?,studentNumber=?,idCategory=? WHERE id=? ;";
    PreparedStatement updateStatement = connection.prepareStatement(query);
    updateStatement.setString(1, listing.getTitle());
    updateStatement.setString(2, listing.getDescription());
    updateStatement.setDouble(3, listing.getPrice());
    updateStatement.setString(4, listing.getCity());
    updateStatement.setString(5, listing.getCondition());
    updateStatement.setInt(6, listing.getSeller().getId());
    updateStatement.setInt(7, 0);
    updateStatement.setInt(7, getIdCategoryFromName(listing.getCategoryName()));
    updateStatement.setInt(8, listing.getId());
    updateStatement.executeUpdate();
  }

  @Override public void delete(Listing listing)
      throws SQLException, RemoteException
  {
    String query = "DELETE FROM listing WHERE id=?";
    PreparedStatement deleteStatement = connection.prepareStatement(query);
    deleteStatement.setInt(1, listing.getId());
    deleteStatement.executeUpdate();
  }

  private int getIdCategoryFromName(String categoryName) throws SQLException
  {
    String categoryQuery = "SELECT idCategory FROM category WHERE name = ?";
    PreparedStatement selectStatement = connection.prepareStatement(categoryQuery);
    selectStatement.setString(1, categoryName);
    ResultSet categorySet = selectStatement.executeQuery();
    categorySet.next();
    return categorySet.getInt(1);
  }

  private String getCategoryNameFromId(int idCategory) throws SQLException
  {
    if(idCategory == 0) {
      return "<none>";
    }
    String categoryQuery = "SELECT name FROM category WHERE idCategory = ?";
    PreparedStatement selectStatement = connection.prepareStatement(categoryQuery);
    selectStatement.setInt(1, idCategory);
    ResultSet categorySet = selectStatement.executeQuery();
    categorySet.next();
    return categorySet.getString("name");
  }
}
