package com.sep2zg4.viamarket.server.listingaccess;

import com.sep2zg4.viamarket.server.RemoteMarketplaceImplementation;
import com.sep2zg4.viamarket.servermodel.ReadMap;
import com.sep2zg4.viamarket.servermodel.ReadWriteAccess;
import com.sep2zg4.viamarket.servermodel.WriteMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MapAccess extends UnicastRemoteObject implements ReadWriteAccess
{
  private int readers;
  private int writers;
  private int waitingWriters;
  private final ReadMap readMap;
  private final WriteMap writeMap;

  public MapAccess(RemoteMarketplaceImplementation marketplaceImplementation) throws
      RemoteException
  {
    readers = 0;
    writers = 0;
    waitingWriters = 0;
    readMap = marketplaceImplementation;
    writeMap = marketplaceImplementation;
  }

  @Override public synchronized ReadMap acquireRead()
  {
    while (writers > 0 || waitingWriters > 0)
    {
      try
      {
        wait();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    readers++;
    notifyAll();
    return readMap;
  }

  @Override public synchronized void releaseRead()
  {
    readers--;
    if (readers == 0)
    {
      notify();
    }
  }

  @Override public synchronized WriteMap acquireWrite()
  {
    waitingWriters++;
    while (readers > 0 || writers > 0)
    {
      try
      {
        wait();   // in a try-catchblock(not shownhere)}
      }
      catch (InterruptedException e)
      {
        throw new RuntimeException(e);
      }
    }
    waitingWriters--;
    writers++;
    return writeMap;
  }

  @Override public synchronized void releaseWrite()
  {
    writers--;
    notifyAll();
  }
}
