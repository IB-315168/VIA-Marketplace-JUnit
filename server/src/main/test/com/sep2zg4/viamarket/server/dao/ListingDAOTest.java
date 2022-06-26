package com.sep2zg4.viamarket.server.dao;

import com.sep2zg4.viamarket.model.Listing;
import com.sep2zg4.viamarket.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListingDAOTest
{
  DAOManager manager = DAOManager.getInstance();
  ListingDAO listingDAO;
  UserDAO userDAO;
  CategoryDAO categoryDAO;
  List<Listing> listingList;
  User user;
  Listing listing1;
  Listing listing2;
  Listing listing3;

  ListingDAOTest() throws SQLException
  {
  }

  @BeforeEach void setUp() throws SQLException, RemoteException
  {
    listingList = new ArrayList<>();
    user = new User(311517, "Test user",
        "12345678", "test@domain.com", false);
    listing1 = new Listing(1, "test", "test",
        "This is a test listing.", 123, "Horsens", "New",
        user);
    listing2 = new Listing(2, "test", "test2",
        "This is a second test listing.", 456, "Horsens", "New",
        user);
    manager.resetDatabase();
    listingDAO = (ListingDAO) manager.getDao(DAOManager.Table.Listing);
    userDAO = (UserDAO) manager.getDao(DAOManager.Table.User);
    categoryDAO = (CategoryDAO) manager.getDao(DAOManager.Table.Category);
    userDAO.create(user);
    categoryDAO.create("test");
  }

  @AfterEach void tearDown()
  {
  }

  // Zero
  @Test public void acquireEmptyDatabase() throws SQLException, RemoteException
  {
    listingList = listingDAO.getAll();
    assertEquals(0, listingList.size());
  }

  // One
  @Test public void pushListing() throws RemoteException, SQLException
  {
    listingDAO.create(listing1);
    listingList = listingDAO.getAll();
    assertEquals(1, listingList.size());
  }

  @Test public void getListing() throws SQLException, RemoteException
  {
    listingDAO.create(listing1);
    Listing refListing = listingDAO.getById(listing1.getId());
    assertTrue(refListing.equals(listing1));
  }

  @Test public void sizeOneAfterUpdate() throws SQLException, RemoteException
  {
    listingDAO.create(listing1);
    listing1 = new Listing(1, "test", "test2",
      "This is a second test listing.", 456, "Horsens", "New",
      user);
    listingDAO.update(listing1);
    listingList = listingDAO.getAll();
    assertEquals(1, listingList.size());
  }

  @Test public void removeListing() throws SQLException, RemoteException
  {
    listingDAO.delete(listing1);
    assertEquals(0, listingList.size());
  }

  // Many
  @Test public void addTwo() throws SQLException, RemoteException
  {
    listingDAO.create(listing1);
    listingDAO.create(listing2);
    listingList = listingDAO.getAll();
    assertEquals(2, listingList.size());
  }

  @Test public void areElementsSame() throws SQLException, RemoteException
  {
    List<Listing> refList = new ArrayList<>(List.of(new Listing[] {listing1, listing2}));
    listingDAO.create(listing1);
    listingDAO.create(listing2);
    listingList = listingDAO.getAll();
    assertTrue(refList.get(0).equals(listingList.get(0)));
    assertTrue(refList.get(1).equals(listingList.get(1)));
  }

  @Test public void lengthOneWhenOneDeleted()
      throws SQLException, RemoteException
  {
    List<Listing> refList = new ArrayList<>(List.of(new Listing[] {listing1, listing2}));
    listingDAO.create(listing1);
    listingDAO.create(listing2);
    listingDAO.delete(listing1);
    listingList = listingDAO.getAll();
    assertEquals(1, listingList.size());
  }

  @Test public void lengthZeroWhenBothDeleted()
      throws SQLException, RemoteException
  {
    List<Listing> refList = new ArrayList<>(List.of(new Listing[] {listing1, listing2}));
    listingDAO.create(listing1);
    listingDAO.create(listing2);
    listingDAO.delete(listing1);
    listingDAO.delete(listing2);
    listingList = listingDAO.getAll();
    assertEquals(0, listingList.size());
  }

  // Boundaries - no tests due to lack of boundaries
  // Exceptions

  @Test public void attemptToRetrieveFromEmpty()
      throws SQLException, RemoteException
  {
    assertThrows(SQLException.class, () -> {
      listingDAO.getById(1);
    });
  }

  @Test public void attemptToInsertWithTitleTooLong()
      throws SQLException, RemoteException
  {
    listing1 = new Listing(1, "test", "testcontainingtitlethatistoolong",
        "This is a second test listing.", 456, "Horsens", "New",
        user);
    assertThrows(SQLException.class, () -> {
      listingDAO.create(listing1);
    });
  }

  @Test public void attemptToInsertWithNoSeller() throws RemoteException
  {
    assertThrows(IllegalArgumentException.class, () -> {
      listing1 = new Listing(1, "test", "test",
          "This is a second test listing.", 123, "Horsens", "New",
          null);
      listingDAO.create(listing1);
    });
  }

  @Test public void attemptToInsertWithNonExistentSeller()
      throws RemoteException
  {
    user = new User(311111, "Test user",
        "12345678", "test@domain.com", false);
    listing1 = new Listing(1, "test", "test",
        "This is a second test listing.", 123, "Horsens", "New",
        user);
    assertThrows(SQLException.class, () -> {
      listingDAO.create(listing1);
    });
  }
}