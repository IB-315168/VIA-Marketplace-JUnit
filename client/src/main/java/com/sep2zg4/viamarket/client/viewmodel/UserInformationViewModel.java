package com.sep2zg4.viamarket.client.viewmodel;

import com.sep2zg4.viamarket.client.model.MarketplaceModel;
import com.sep2zg4.viamarket.model.Listing;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * A viewmodel for user information
 *
 * @author Wojtek Rusinski
 * @version 1.6 - May 2022
 */

public class UserInformationViewModel
{
  private final MarketplaceModel model;
  private final ObservableList<Listing> userListings;
  private final ObservableList<Listing> userWishlist;
  private final StringProperty fullname;

  public UserInformationViewModel(MarketplaceModel model)
  {
    this.model = model;
    userListings = FXCollections.observableList(model.getUserListings());
    userWishlist = FXCollections.observableList(model.getUserWishlist());
    model.addPropertyChangeListener(evt -> setUserListings());
    model.addPropertyChangeListener(evt -> setUserWishlist());
    fullname = new SimpleStringProperty("");
  }

  public void setCurrentSelectedUserListing(Listing listing)
  {
    model.setCurrentSelectedUserListing(listing);
  }

  public void setUserListings()
  {
    userListings.setAll(model.getUserListings());
  }

  public void setUserWishlist()
  {
    userWishlist.setAll(model.getUserWishlist());
  }

  public void setFullname()
  {
    fullname.setValue(model.getCurrentUser().getFullName());
  }

  public ObservableList<Listing> getUserListings()
  {
    return userListings;
  }

  public ObservableList<Listing> getUserWishlist()
  {
    return userWishlist;
  }

  public void deleteListing(Listing listing)
      throws SQLException, RemoteException
  {
    model.deleteListing(listing);
  }

  public void deleteWishlistItem(Listing wishlist)
      throws SQLException, RemoteException
  {
    model.deleteWishlistItem(wishlist.getId());
  }

  public StringProperty getFullName()
  {
    return fullname;
  }
}
