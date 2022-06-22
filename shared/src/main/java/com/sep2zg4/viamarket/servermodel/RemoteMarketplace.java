package com.sep2zg4.viamarket.servermodel;

import com.sep2zg4.viamarket.model.Listing;
import com.sep2zg4.viamarket.model.User;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Remote interface for RMI
 *
 * @author Igor Bulinski
 * @version 1.8 - May 2022
 */
public interface RemoteMarketplace extends Remote
{

  /**
   * Method responsible for handling login request
   * @param studentNumber             Student identification number
   * @param password                  Student password
   * @return Object of a User with matching credentials, constructed on data from database (null if credentials did not match any record)
   * @throws SQLException
   * @throws RemoteException
   */
  User login(int studentNumber, String password)
      throws RemoteException, SQLException;

  /**
   * Method responsible for acquiring Listing from database by its id
   * @param id                        Listings id
   * @return listing object
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  Listing getListingById(String id) throws SQLException, RemoteException;

  /**
   * method to get all listings
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void getAllListing() throws SQLException, RemoteException;

  /**
   * method to create a listing
   * @param listing              Listing to be added to database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void createListing(Listing listing) throws SQLException, RemoteException;

  /**
   * method to edit a listing
   * @param listing              Listing to be updated on database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void updateListing(Listing listing) throws SQLException, RemoteException;

  /**
   * method to delete a listing
   * @param listing              Listing to be deleted on database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void deleteListing(Listing listing) throws SQLException, RemoteException;

  /**
   * method to create a user
   * @param user                User to be added to database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void createUser(User user) throws SQLException,RemoteException;

  /**
   * method to edit a user
   * @param user                 User to be updated on database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void updateUser(User user) throws SQLException, RemoteException;

  /**
   * method to delete a user
   * @param user                 User to be deleted on database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void deleteUser(User user) throws SQLException, RemoteException;

  /**
   * method to get a specific user by id
   * @param id                   User id
   * @return User
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  User getUserById(String id) throws SQLException, RemoteException;

  /**
   * method to create a category
   * @param categoryName           Category name to be added on database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void createCategory(String categoryName) throws SQLException,RemoteException;

  /**
   * method to delete a category
   * @param category               Category name to be deleted on database
   * @throws SQLException if error in SQL
   * @throws RemoteException if error in Server side
   */
  void deleteCategory(String category) throws SQLException,RemoteException;

  void deleteWishlistItem(Integer idListing, int idUser) throws SQLException, RemoteException;

  void addToWishlist(int idListing, int idUser) throws SQLException, RemoteException;

  /**
   *
   * @throws RemoteException
   */
  void exampleMethod() throws RemoteException;

  /**
   *
   * @return
   * @throws RemoteException
   */
  ReadWriteAccess getLock() throws RemoteException;

  void addRemotePropertyChangeListener(RemotePropertyChangeListener<String> listener) throws RemoteException;
  void removeRemotePropertyChangeListener(RemotePropertyChangeListener<String> listener) throws RemoteException;
}
