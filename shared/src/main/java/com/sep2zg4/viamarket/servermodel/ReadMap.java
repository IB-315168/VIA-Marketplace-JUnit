package com.sep2zg4.viamarket.servermodel;

import com.sep2zg4.viamarket.model.Listing;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Read access interface of reader-writers lock
 *
 * @author Igor Bulinski
 * @version 1.3 - May 2022
 */

public interface ReadMap extends Remote
{
  /**
   * Method responsible for acquiring all available Listings
   *
   * @return Concurrent HashMap of all listings available
   * @throws RemoteException
   */
  ConcurrentHashMap<String, ArrayList<Listing>> getListings()
      throws RemoteException;

  /**
   * Method responsible for acquiring Wishlist of all Users
   *
   * @return Concurrent HashMap of WishList items
   * @throws RemoteException
   */
  ConcurrentHashMap<Integer, ArrayList<Listing>> getWishlist()
      throws RemoteException;
}
