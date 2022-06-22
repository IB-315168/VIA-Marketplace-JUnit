package com.sep2zg4.viamarket.server;

import com.sep2zg4.viamarket.model.Listing;
import com.sep2zg4.viamarket.model.User;
import com.sep2zg4.viamarket.server.dao.*;
import com.sep2zg4.viamarket.server.listingaccess.*;
import com.sep2zg4.viamarket.servermodel.ReadWriteAccess;
import com.sep2zg4.viamarket.servermodel.RemoteMarketplace;
import com.sep2zg4.viamarket.servermodel.WriteMap;
import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the Remote interface {@link RemoteMarketplace}
 *
 * @author Igor Bulinski
 * @version 1.0 - April 2022
 */
public class RemoteMarketplaceImplementation extends UnicastRemoteObject
    implements RemoteMarketplace, WriteMap
{

  private final DAOManager daoManager = DAOManager.getInstance();
  private ConcurrentHashMap<String, ArrayList<Listing>> listings;
  private ConcurrentHashMap<Integer, ArrayList<Listing>> wishlist;
  private final UserDAO userDAO;
  private final CategoryDAO categoryDAO;
  private final ListingDAO listingDAO;
  private final WishlistDAO wishlistDAO;
  private final RMIListingsWriter writer;
  private final RMIWishlistWriter writerWishlist;
  private final ReadWriteAccess lock;
  private final LoginHandler loginHandler;
  private final RemotePropertyChangeSupport<String> support;

  /**
   * Class constructor
   *
   * @throws RemoteException
   */
  public RemoteMarketplaceImplementation()
      throws RemoteException, SQLException, InterruptedException
  {
    lock = new MapAccess(this);
    listings = new ConcurrentHashMap<>();
    wishlist = new ConcurrentHashMap<>();
    userDAO = (UserDAO) daoManager.getDao(DAOManager.Table.User);
    categoryDAO = (CategoryDAO) daoManager.getDao(DAOManager.Table.Category);
    listingDAO = (ListingDAO) daoManager.getDao(DAOManager.Table.Listing);
    wishlistDAO = (WishlistDAO) daoManager.getDao(DAOManager.Table.Wishlist);
    support = new RemotePropertyChangeSupport<String>(this);
    System.out.println(support.getPropertyChangeListeners().length);
    writerWishlist = daoManager.getRMIWishlistWriter(lock, listingDAO, userDAO, categoryDAO, wishlistDAO, support);
    writer = daoManager.getRMIListingsWriter(lock, listingDAO, userDAO, categoryDAO, wishlistDAO, support);
    loginHandler = daoManager.getLoginHandler();

    Thread writerThreadv2 = new Thread(writerWishlist);
    writerThreadv2.start();

    Thread writerThread = new Thread(writer);
    writerThread.start();
  }

  /**
   * 2-argument method checking credentials with which user is trying to log in
   *
   * @param studentNumber student identification sent by the client
   * @param password password sent by the client
   * @return <ul><li>true - if credentials are correct</li><li>false - otherwise</li></ul>
   * @throws RemoteException
   */
  @Override public User login(int studentNumber, String password)
      throws RemoteException, SQLException
  {
    return loginHandler.attemptLogin(studentNumber, password);
  }

  @Override public Listing getListingById(String id)
      throws SQLException, RemoteException
  {
    return listingDAO.getById(Integer.parseInt(id));
  }

  @Override public void getAllListing()
      throws SQLException, RemoteException
  {

  }

  @Override public void createListing(Listing listing)
      throws SQLException, RemoteException
  {
    listingDAO.create(listing);
    writer.pushUpdate();
  }

  @Override public void updateListing(Listing listing)
      throws SQLException, RemoteException
  {
    listingDAO.update(listing);
    writer.pushUpdate();
    writerWishlist.pushUpdate();
  }

  @Override public void deleteListing(Listing listing)
      throws SQLException, RemoteException
  {
    try
    {
      listingDAO.delete(listing);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
    writer.pushUpdate();
    writerWishlist.pushUpdate();
  }

  @Override
  public void createUser(User user) throws SQLException, RemoteException {
    userDAO.create(user);
    writer.pushUpdate();
  }

  @Override
  public void updateUser(User user) throws SQLException, RemoteException {
    userDAO.update(user);
    writer.pushUpdate();
    writerWishlist.pushUpdate();
  }

  @Override
  public void deleteUser(User user) throws SQLException, RemoteException {
    try {
      userDAO.delete(user);
    }
    catch (RemoteException e){
      throw new RuntimeException(e);
    }
    writer.pushUpdate();
    writerWishlist.pushUpdate();
  }

  @Override
  public User getUserById(String id) throws SQLException, RemoteException {
    return userDAO.getById(Integer.parseInt(id));
  }

  @Override public void createCategory(String categoryName)
      throws SQLException, RemoteException
  {
    categoryDAO.create(categoryName);
    writer.pushUpdate();
  }

  @Override public void deleteCategory(String category)
      throws SQLException, RemoteException
  {
    categoryDAO.delete(category);
    writer.pushUpdate();
    writerWishlist.pushUpdate();
  }

  @Override public void deleteWishlistItem(Integer idListing, int idUser) throws SQLException, RemoteException{
    wishlistDAO.setCurrentStudentNumber(idUser);
    wishlistDAO.delete(idListing);
    writerWishlist.pushUpdate();
  }

  @Override public void addToWishlist(int idListing, int idUser) throws SQLException, RemoteException{
    wishlistDAO.setCurrentStudentNumber(idUser);
    wishlistDAO.create(idListing);
    writerWishlist.pushUpdate();
  }

  //Debug purpose, showing issues with reading
  public void exampleMethod() {
    writer.pushUpdate();
  }

  @Override public ReadWriteAccess getLock() throws RemoteException
  {
    return lock;
  }

  @Override public void addRemotePropertyChangeListener(
      RemotePropertyChangeListener<String> listener) throws RemoteException
  {
    System.out.println(support.getPropertyChangeListeners().length);
    support.addPropertyChangeListener(listener);
    System.out.println(support.getPropertyChangeListeners().length);
  }

  @Override public void removeRemotePropertyChangeListener(
      RemotePropertyChangeListener<String> listener) throws RemoteException
  {
    System.out.println(support.getPropertyChangeListeners().length);
    support.removePropertyChangeListener(listener);
    System.out.println(support.getPropertyChangeListeners().length);
  }

  @Override public ConcurrentHashMap<String, ArrayList<Listing>> getListings()
      throws RemoteException
  {
    return listings;
  }

  @Override public ConcurrentHashMap<Integer, ArrayList<Listing>> getWishlist()
      throws RemoteException
  {
    return wishlist;
  }

  @Override public void write(
      ConcurrentHashMap<String, ArrayList<Listing>> listingsReference)
      throws RemoteException
  {
    this.listings = listingsReference;
  }

  @Override public void writeWishlist(
      ConcurrentHashMap<Integer, ArrayList<Listing>> wishlistReference)
      throws RemoteException
  {
    this.wishlist = wishlistReference;
  }
}
