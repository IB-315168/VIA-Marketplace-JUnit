package com.sep2zg4.viamarket.client.model.comm;

import com.sep2zg4.viamarket.client.model.MarketplaceModel;
import com.sep2zg4.viamarket.model.Listing;
import com.sep2zg4.viamarket.servermodel.ReadMap;
import com.sep2zg4.viamarket.servermodel.ReadWriteAccess;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RMIListingsReader implements Runnable, Serializable
{
  private final MarketplaceModel model;
  private final ReadWriteAccess lock;

  public RMIListingsReader(ReadWriteAccess lock, MarketplaceModel model)
  {
    this.lock = lock;
    this.model = model;
  }

  public void pullUpdate()
  {
    synchronized (this)
    {
      notify();
    }
  }

  @Override public void run()
  {
    try
    {
      readToMap();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  private void readToMap() throws RemoteException
  {
    while (true)
    {
      ReadMap read = lock.acquireRead();
      HashMap<String, ArrayList<Listing>> copy = new HashMap<>();
      ArrayList<Listing> copyWishlist = new ArrayList<>();
      ConcurrentHashMap<String, ArrayList<Listing>> storage = read.getListings();
      ConcurrentHashMap<Integer, ArrayList<Listing>> storageWishlist = read.getWishlist();
      for (String s : storage.keySet())
      {
        copy.put(s, storage.get(s));
      }

      if (model.getCurrentUser() != null)
      {
        copyWishlist.addAll(
            storageWishlist.get(model.getCurrentUser().getId()));
      }
      model.setListings(copy);
      model.setWishlist(copyWishlist);
      lock.releaseRead();
      try
      {
        synchronized (this)
        {
          wait();
        }
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
}
