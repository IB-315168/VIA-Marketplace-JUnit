package com.sep2zg4.viamarket.servermodel;

import com.sep2zg4.viamarket.model.Listing;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 * @author Igor Bulinski
 * @version 1.0 - May 2022
 */

public interface WriteMap extends ReadMap
{
  /**
   *
   * @param listingsReference
   * @throws RemoteException if error in Server side
   */
  void write(ConcurrentHashMap<String, ArrayList<Listing>> listingsReference) throws
      RemoteException;

  void writeWishlist(ConcurrentHashMap<Integer, ArrayList<Listing>> wishlistReference) throws
      RemoteException;
}
