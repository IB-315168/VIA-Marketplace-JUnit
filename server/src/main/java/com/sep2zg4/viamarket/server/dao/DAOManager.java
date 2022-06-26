package com.sep2zg4.viamarket.server.dao;

import com.sep2zg4.viamarket.model.Listing;
import com.sep2zg4.viamarket.server.listingaccess.RMIListingsWriter;
import com.sep2zg4.viamarket.server.listingaccess.RMIWishlistWriter;
import com.sep2zg4.viamarket.servermodel.ReadWriteAccess;
import dk.via.remote.observer.RemotePropertyChangeSupport;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public final class DAOManager
{
  private static ThreadLocal<DAOManager> INSTANCE;
  private Connection connection;
  private LoginHandler loginHandler;

  private DAOManager() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public static DAOManager getInstance() throws SQLException
  {
    if (INSTANCE == null)
    {
      INSTANCE = new ThreadLocal<DAOManager>()
      {
        @Override protected DAOManager initialValue()
        {
          try
          {
            return new DAOManager();
          }
          catch (SQLException e)
          {
            return null;
          }
        }
      };
    }

    return INSTANCE.get();
  }

  public void open() throws SQLException
  {
    if (connection == null || connection.isClosed())
    {
      connection = DriverManager.getConnection(
          "jdbc:postgresql://abul.db.elephantsql.com/hhhjuqgd", "hhhjuqgd",
          "wxdqoaKUpqn_6iqvgnhXVPjWe5ULyMyW");
      connection.setSchema("via_market");
    }
  }

  public void resetDatabase() throws SQLException
  {
    if (connection == null || connection.isClosed())
    {
      this.open();
    }
    String query = "DROP SCHEMA IF EXISTS via_market CASCADE;\n"
        + "create schema via_market;\n" + "set schema 'via_market';\n" + "\n"
        + "\n" + "create table person\n" + "(\n"
        + "    studentNumber int primary key check (studentNumber > 100000 AND studentNumber < 999999),\n"
        + "    fullName      varchar(30)        not null,\n"
        + "    password      varchar(30)        not null check (length(password) > 7),\n"
        + "    phoneNumber   varchar(15) unique not null,\n"
        + "    email         varchar(30),\n"
        + "    isModerator   boolean            not null\n" + ");\n" + "\n"
        + "create table category\n" + "(\n"
        + "    idCategory serial primary key,\n"
        + "    name       varchar(30) unique\n" + ");\n" + "\n"
        + "create table listing\n" + "(\n"
        + "    id            serial primary key,\n"
        + "    title         varchar(20),\n"
        + "    description   varchar(500),\n" + "    price         float,\n"
        + "    city          varchar(20),\n"
        + "    condition     varchar(10),\n"
        + "    studentNumber int references person (studentNumber) on delete cascade,\n"
        + "    idCategory    int references category (idCategory) on delete set null\n"
        + ");\n" + "\n" + "create table wishlist\n" + "(\n"
        + "    studentNumber int references person (studentNumber) on delete cascade,\n"
        + "    idListing     int references listing (id) on delete cascade\n"
        + ");\n";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.executeUpdate();
  }

  public void close() throws SQLException
  {
    if (connection != null || !connection.isClosed())
    {
      connection.close();
    }
  }

  // Function responsible for retrieving Object-specific DAO, to be implemented once aforementioned are implemented
  public Dao getDao(Table t) throws SQLException, RemoteException
  {
    if (connection == null || connection.isClosed())
    {
      this.open();
    }

    switch (t)
    {
      case Listing:
        return new ListingDAO(connection);
      case User:
        return new UserDAO(connection);
      case Category:
        return new CategoryDAO(connection);
      case Wishlist:
        return new WishlistDAO(connection);
      default:
        throw new SQLException("Table does not exist");
    }
  }

  /*// Our version of the Function
  public Dao getDao(String table) throws SQLException
  {
    if(connection == null || connection.isClosed()) {
      this.open();
    }

    switch (table) {
      case "Listing":
        return new ListingDAO(connection);
      case "User":
        //        return new UserDao(connection);
      default:
        throw new SQLException("Table does not exist");
    }
  }*/

  public RMIListingsWriter getRMIListingsWriter(ReadWriteAccess lock, ListingDAO listingDAO, UserDAO userDAO,
      CategoryDAO categoryDAO, WishlistDAO wishlistDAO, RemotePropertyChangeSupport<String> support)
      throws SQLException
  {
    return RMIListingsWriter.getInstance(lock, connection, listingDAO, userDAO, categoryDAO, wishlistDAO, support);
  }

  public RMIWishlistWriter getRMIWishlistWriter(ReadWriteAccess lock, ListingDAO listingDAO, UserDAO userDAO,
      CategoryDAO categoryDAO, WishlistDAO wishlistDAO, RemotePropertyChangeSupport<String> support)
  {
    return RMIWishlistWriter.getInstance(lock, connection, listingDAO, userDAO, categoryDAO, wishlistDAO, support);
  }

  public LoginHandler getLoginHandler() throws SQLException, RemoteException
  {
    if (connection == null || connection.isClosed())
    {
      this.open();
    }

    if(loginHandler == null) {
      loginHandler = LoginHandler.getInstance();
      loginHandler.setConnection(connection);
    }

    return loginHandler;
  }


  public enum Table
  {Listing, User, Category, Wishlist}
}
