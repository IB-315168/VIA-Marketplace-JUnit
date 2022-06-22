package com.sep2zg4.viamarket.client.model;

import com.sep2zg4.viamarket.client.model.comm.ClientMarketplaceCommunicator;
import com.sep2zg4.viamarket.model.Listing;
import com.sep2zg4.viamarket.model.User;
import javafx.application.Platform;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of the {@link MarketplaceModel}
 *
 * @author Igor Bulinski, Wojtek Rusinski
 * @version 2.2 - May 2022
 */
public class MarketplaceModelManager implements MarketplaceModel
{
  private HashMap<String, ArrayList<Listing>> listings;
  private ArrayList<Listing> wishlist;
  private final ClientMarketplaceCommunicator client;
  private User currentUser;
  private Listing currentSelectedUserListing;
  private final PropertyChangeSupport support;

  /**
   * Constructor creating a {@link ClientMarketplaceCommunicator} object and establishing connection
   *
   * @throws RemoteException
   */
  public MarketplaceModelManager() throws RemoteException
  {
    client = new ClientMarketplaceCommunicator("localhost",
        Registry.REGISTRY_PORT, this);
    listings = new HashMap<>();
    wishlist = new ArrayList<>();
    this.support = new PropertyChangeSupport(this);
  }

  /**
   * 2-argument method for passing credentials to the {@link ClientMarketplaceCommunicator#login(int, String)} method and handling the result
   *
   * @param username username of the user
   * @param password matching password
   * @return result of {@link com.sep2zg4.viamarket.client.model.comm.ClientMarketplaceCommunicator#login(int, String)}
   * @throws RemoteException
   */
  public boolean login(int username, String password)
      throws RemoteException, NotBoundException, SQLException
  {
    currentUser = client.login(username, password);
    if (currentUser != null)
    {
      client.propertyChange(null);
      return true;
    }
    return false;
  }

  public void closeCommunicator() throws RemoteException
  {
    client.close();
  }

  public User getCurrentUser()
  {
    return currentUser;
  }

  public Listing getCurrentSelectedUserListing()
  {
    return currentSelectedUserListing;
  }

  public void setCurrentSelectedUserListing(Listing currentSelectedUserListing)
  {
    this.currentSelectedUserListing = currentSelectedUserListing;
  }

  /**
   * Method used for creating a listing
   *
   * @param listing Listing that is being created
   * @throws SQLException
   * @throws RemoteException
   */
  public void createListing(Listing listing)
      throws SQLException, RemoteException
  {
    client.createListing(listing);
  }

  /**
   * Method used for deleting a listing
   *
   * @param listing Listing that is being deleted
   * @throws SQLException
   * @throws RemoteException
   */
  public void deleteListing(Listing listing)
      throws SQLException, RemoteException
  {
    client.deleteListing(listing);
  }

  /**
   * Method used for updating a listing
   *
   * @param listing Listing that is being updated
   * @throws SQLException
   * @throws RemoteException
   */
  public void updateListing(Listing listing)
      throws SQLException, RemoteException
  {
    client.updateListing(listing);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }

  public HashMap<String, ArrayList<Listing>> getListings()
  {
    return listings;
  }

  public void setListings(HashMap<String, ArrayList<Listing>> listings)
  {
    this.listings = listings;
    Platform.runLater(new Runnable()
    {
      @Override public void run()
      {
        support.firePropertyChange("dbupdate", 0, 1);
      }
    });
  }

  @Override public ArrayList<Listing> getAllListings()
  {
    ArrayList<Listing> allListings = new ArrayList<>();
    for (String s : listings.keySet())
    {
      allListings.addAll(listings.get(s));
    }
    return allListings;
  }

  @Override public ArrayList<String> getAllCategories()
  {
    ArrayList<String> categories = new ArrayList<>();
    categories.add("All");
    categories.addAll(listings.keySet());
    return categories;
  }

  public ArrayList<Listing> getCategoryListing(String categoryName)
  {
    if (categoryName.equals("All"))
    {
      return getAllListings();
    }
    return listings.get(categoryName);
  }

  @Override public ArrayList<Listing> getUserListings()
  {
    ArrayList<Listing> userListings = new ArrayList<>();
    for (String s : listings.keySet())
    {
      for (Listing listing : listings.get(s))
      {
        if (listing.getSeller().getId() == currentUser.getId())
        {
          userListings.add(listing);
        }
      }
    }
    return userListings;
  }

  public void createCategory(String categoryName)
      throws SQLException, RemoteException
  {
    client.createCategory(categoryName);
  }

  public void deleteCategory(String category)
      throws SQLException, RemoteException
  {
    client.deleteCategory(category);
  }

  @Override public void deleteWishlistItem(Integer idListing)
      throws SQLException, RemoteException
  {
    client.deleteWishlistItem(idListing, getCurrentUser().getId());
  }

  @Override public void addToListing(int idListing)
      throws SQLException, RemoteException
  {
    client.addToWishlist(idListing, getCurrentUser().getId());
  }

  public void setWishlist(ArrayList<Listing> wishlist)
  {
    this.wishlist = wishlist;
    Platform.runLater(new Runnable()
    {
      @Override public void run()
      {
        support.firePropertyChange("dbupdate", 0, 2);
      }
    });
  }

  @Override public ArrayList<Listing> getUserWishlist()
  {
    return wishlist;
  }
}
