package com.sep2zg4.viamarket.client.viewmodel;

import com.sep2zg4.viamarket.client.model.MarketplaceModel;
import com.sep2zg4.viamarket.model.Listing;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A viewmodel for Listings view
 *
 * @author Wojtek Rusinski
 * @version 1.6 - May 2022
 */
public class ListingsViewModel
{
  private final MarketplaceModel model;
  private final ObservableList<Listing> listingsList;
  private final ObservableList<String> categoryList;
  private final StringProperty userType;

  /**
   * Constructor for ListingsViewModel taking Model as an argument
   *
   * @param model Model Manager reference
   */
  public ListingsViewModel(MarketplaceModel model)
  {
    this.model = model;
    this.userType = new SimpleStringProperty("");
    this.listingsList = FXCollections.observableList(model.getAllListings());
    this.categoryList = FXCollections.observableList(model.getAllCategories());

    model.addPropertyChangeListener(evt -> {
      setListingsList();
      setCategoryList();
    });
  }

  public void setListingsList()
  {
    listingsList.setAll(model.getAllListings());
  }

  public void getByCategory(String categoryName)
  {
    listingsList.setAll(model.getCategoryListing(categoryName));
  }

  public void setCategoryList()
  {
    categoryList.setAll(model.getAllCategories());
  }

  public ObservableList<String> getCategoryList()
  {
    return categoryList;
  }

  public ObservableList<Listing> getListingsList()
  {
    return listingsList;
  }

  public StringProperty getUserType()
  {
    return userType;
  }

  public void setUserType()
  {
    if (model.getCurrentUser().isModerator())
    {
      this.userType.setValue(
          "Moderator : " + model.getCurrentUser().getFullName());
    }
    else
    {
      this.userType.setValue("User : " + model.getCurrentUser().getFullName());
    }
  }

  public void deleteListing(Listing listing)
      throws SQLException, RemoteException
  {
    model.deleteListing(listing);
  }

  public void deleteCategory(String category)
      throws SQLException, RemoteException
  {
    model.deleteCategory(category);
  }

  public void createCategory(String categoryName)
      throws SQLException, RemoteException
  {
    model.createCategory(categoryName);
  }

  public void addToWishlist(Listing wishlistListing)
      throws SQLException, RemoteException
  {
    model.addToListing(wishlistListing.getId());
  }

  public ArrayList<Listing> getWishlistItem()
  {
    return model.getUserWishlist();
  }

  public boolean isModerator()
  {
    return model.getCurrentUser().isModerator();
  }

}
