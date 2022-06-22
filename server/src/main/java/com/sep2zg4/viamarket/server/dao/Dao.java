package com.sep2zg4.viamarket.server.dao;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

/**
 * Remote interface for RMI
 *
 * @author Dércio Fernandes, Igor Bulinski
 * @version 1.0 - May 2022
 */

public interface Dao<Type>
{
  /**
   *
   * @param id          id of listing/category/user. required bigger than 0
   * @return object of the type listing/category/user
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  Type getById(int id) throws
      SQLException, RemoteException; //Reason why ID is a string is because user ID's are a string.

  /**
   * method to get list of all objects stored in database of a certain type
   * @return list of listing/category/user
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  List<Type> getAll() throws SQLException, RemoteException;

  /**
   * method to create an object of specific type in the database
   * @param type Object of type (listing/category/user)
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void create(Type type) throws SQLException, RemoteException;

  /**
   * method to edit an object of specific type in the database
   * @param type Object of type (listing/category/user)
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void update(Type type) throws SQLException, RemoteException;

  /**
   * method to delete an object of specific type in the database
   * @param type Object of type (listing/category/user)
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void delete(Type type) throws SQLException, RemoteException;
}
