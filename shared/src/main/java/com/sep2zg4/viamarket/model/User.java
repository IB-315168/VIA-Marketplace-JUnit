package com.sep2zg4.viamarket.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class representing user on the marketplace.
 *
 * @author Igor Bulinski
 * @version 1.0 - April 2022
 */
public class User implements Serializable
{
  private final int id;
  private final String fullName;
  private final String phoneNumber;
  private final String email;
  private final boolean isModerator;

  /**
   * 3-argument constructor.
   * @param id Id for the user, required not-blank
   * @param fullName Full name of the user, required not-blank
   * @param isModerator parameter stating user privileges <ul><li>false - user has standard access</li><li>true - user has moderator privileges</li></ul>
   * @throws IllegalArgumentException if argument conditions are not met
   */
  public User(int id, String fullName, String phoneNumber, String email,
      boolean isModerator) throws IllegalArgumentException, RemoteException
  {
    if(id < 0 || id > 999999) {
      throw new IllegalArgumentException("Incorrect id");
    }
    this.id = id;

    if(fullName == null || fullName.isBlank()) {
      throw new IllegalArgumentException("Full name cannot be empty.");
    }
    this.fullName = fullName;

    this.phoneNumber = phoneNumber;
    this.email = email;

    this.isModerator = isModerator;
  }

  public int getId()
  {
    return id;
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }

  public boolean isModerator()
  {
    return isModerator;
  }
}
