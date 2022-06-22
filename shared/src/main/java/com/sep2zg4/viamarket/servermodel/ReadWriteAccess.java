package com.sep2zg4.viamarket.servermodel;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Access management interface of reader-writers lock
 *
 * @author Igor Bulinski
 * @version 1.3 - May 2022
 */

public interface ReadWriteAccess extends Remote
{
  /**
   * Method granting read access
   *
   * @return interface containing methods available at read-level access
   * @throws RemoteException
   */
  ReadMap acquireRead() throws RemoteException;

  /**
   * Method releasing read access
   *
   * @throws RemoteException
   */
  void releaseRead() throws RemoteException;

  /**
   * Method granting write access
   *
   * @return interface containing methods available at write-level access
   * @throws RemoteException
   */
  WriteMap acquireWrite() throws RemoteException;

  /**
   * Method releasing write access
   *
   * @throws RemoteException
   */
  void releaseWrite() throws RemoteException;
}
