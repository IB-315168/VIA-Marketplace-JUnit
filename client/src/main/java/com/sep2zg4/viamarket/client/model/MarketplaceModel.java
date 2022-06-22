package com.sep2zg4.viamarket.client.model;

import com.sep2zg4.viamarket.model.Listing;
import com.sep2zg4.viamarket.model.User;

import java.beans.PropertyChangeListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Interface for the Model of application
 *
 * @author Igor Bulinski, Wojtek Rusinski, Dércio Fernandes
 * @version 2.2 - May 2022
 */
public interface MarketplaceModel
{

  /**
   * Method responsible for logging in user
   *
   * @param username Student Number
   * @param password Student password
   * @return <ul><li>true - if {@link com.sep2zg4.viamarket.client.model.comm.ClientMarketplaceCommunicator#login(int, String)} returns a User object, meaning that credentials (arguments) matched a record in a database</li><li>false if {@link com.sep2zg4.viamarket.client.model.comm.ClientMarketplaceCommunicator#login(int, String)} returns null</li></ul>
   * @throws RemoteException
   * @throws NotBoundException
   * @throws SQLException
   */
  boolean login(int username, String password)
      throws RemoteException, NotBoundException, SQLException;

  /**
   * Method responsible for returning currently logged-in User reference
   * Used for displaying current Users' data in the view
   *
   * @return reference of currently logged-in user.
   */
  User getCurrentUser();

  void closeCommunicator() throws RemoteException;

  /**
   * Method returning reference to currently selected Listing of a user
   * Used for applying ListingFormView for both creating and editing listing
   *
   * @return Listing object of current selected listing
   */
  Listing getCurrentSelectedUserListing();

  /**
   * Method setting reference to currently selected Listing of a user
   * Used for applying ListingFormView for both creating and editing listing
   *
   * @param currentSelectedUserListing currently selected User listing
   */
  void setCurrentSelectedUserListing(Listing currentSelectedUserListing);

  /**
   * Method responsible for returning ArrayList for given Key being category
   *
   * @param categoryName Category name
   * @return List of Listings matching the category key
   */
  ArrayList<Listing> getCategoryListing(String categoryName);

  /**
   * Method responsible for creating Listing
   *
   * @param listing Listing object to be created
   * @throws SQLException    if error in SQL
   * @throws RemoteException if error in Server side
   */
  void createListing(Listing listing) throws SQLException, RemoteException;

  /**
   * Method responsible for deleting Listing
   *
   * @param listing Listing object
   * @throws SQLException    if error in SQL
   * @throws RemoteException if error in Server side
   */
  void deleteListing(Listing listing) throws SQLException, RemoteException;

  /**
   * Method responsible for updating an object
   *
   * @param listing Listing object
   * @throws SQLException    if error in SQL
   * @throws RemoteException if error in Server side
   */
  void updateListing(Listing listing) throws SQLException, RemoteException;

  /**
   * Method responsible for returning listings HashMap stored in the model
   *
   * @return HashMap containing all listings sorted by categories
   */
  HashMap<String, ArrayList<Listing>> getListings();

  /**
   * Method responsible for setting HashMap of listings in the model
   *
   * @param listings HashMap containing listings sorted by categories
   */
  void setListings(HashMap<String, ArrayList<Listing>> listings);

  /**
   * Method responsible for returning HashMap listings in form of an Arraylist
   *
   * @return list of all listings
   */
  ArrayList<Listing> getAllListings();

  /**
   * Method responsible for returning an ArrayList of categories
   * formed from KeySet of the HashMap
   *
   * @return list of all categories
   */
  ArrayList<String> getAllCategories();

  /**
   * Observer pattern-related method, responsible for adding a listener
   *
   * @param listener PropertyChangeListener to be added
   */
  void addPropertyChangeListener(PropertyChangeListener listener);
  /**
   * Observer pattern-related method, responsible for removing a listener
   *
   * @param listener PropertyChangeListener to be removed
   */
  void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Method responsible for returning an ArrayList of all Listings
   * belonging to currently logged-in User
   *
   * @return List of all user listings
   */
  ArrayList<Listing> getUserListings();

  /**
   * Method responsible for deleting Category
   *
   * @param category Category name
   * @throws SQLException    if error in SQL
   * @throws RemoteException if error in Server side
   */
  void deleteCategory(String category) throws SQLException, RemoteException;

  /**
   * Method responsible for creating Category
   *
   * @param categoryName Category name
   * @throws SQLException    if error in SQL
   * @throws RemoteException if error in Server side
   */
  void createCategory(String categoryName) throws SQLException, RemoteException;

  /**
   * Method responsible for deleting item from Wishlist
   *
   * @param idListing Id of item to be removed from Wishlist
   * @throws SQLException    if error in SQL
   * @throws RemoteException if error in Server side
   */
  void deleteWishlistItem(Integer idListing)
      throws SQLException, RemoteException;

  /**
   * Method responsible for adding item to Wishlist
   *
   * @param idListing Id of item to be added from Wishlist
   * @throws SQLException    if error in SQL
   * @throws RemoteException if error in Server side
   */
  void addToListing(int idListing) throws SQLException, RemoteException;

  /**
   * Method to get all the current user wishlist listings
   *
   * @return list of listings on current user wishlist
   */
  ArrayList<Listing> getUserWishlist();

  /**
   * Method to set list of wishlist listings
   *
   * @param wishlist Hashmap with category and listings from wishlist listings
   */
  void setWishlist(ArrayList<Listing> wishlist);
}
