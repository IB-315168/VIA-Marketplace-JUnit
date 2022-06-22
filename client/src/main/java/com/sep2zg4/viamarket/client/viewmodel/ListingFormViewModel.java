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
 * A view model for ListingForm
 *
 * @author Wojtek Rusinski
 * @version 1.6 - May 2022
 */
public class ListingFormViewModel
{
  private final MarketplaceModel model;
  private final ObservableList<String> categories;
  private final StringProperty title;
  private final StringProperty city;
  private final StringProperty price;
  private final StringProperty condition;
  private final StringProperty phoneNumber;
  private final StringProperty email;
  private final StringProperty socialMediaAndUsername;
  private final StringProperty description;

  public ListingFormViewModel(MarketplaceModel model)
  {
    this.model = model;

    this.categories = getAllCategories();
    this.title = new SimpleStringProperty("");
    this.city = new SimpleStringProperty("");
    this.price = new SimpleStringProperty("");
    this.condition = new SimpleStringProperty("");
    this.phoneNumber = new SimpleStringProperty("");
    this.email = new SimpleStringProperty("");
    this.socialMediaAndUsername = new SimpleStringProperty("");
    this.description = new SimpleStringProperty("");
  }

  public StringProperty getListingTitle()
  {
    return title;
  }

  public StringProperty getListingCity()
  {
    return city;
  }

  public StringProperty getListingPrice()
  {
    return price;
  }

  public StringProperty getListingCondition()
  {
    return condition;
  }

  public StringProperty getListingDescription()
  {
    return description;
  }

  public Listing getSelectedUserListing()
  {
    return model.getCurrentSelectedUserListing();
  }

  public void createListing(String categoryName)
      throws SQLException, RemoteException
  {
    model.createListing(new Listing(1, categoryName, getListingTitle().get(),
        getListingDescription().get(),
        Double.parseDouble(getListingPrice().get()), getListingCity().get(),
        getListingCondition().get(), model.getCurrentUser()));
  }

  public void updateListing(String categoryName)
      throws SQLException, RemoteException
  {
    model.updateListing(
        new Listing(model.getCurrentSelectedUserListing().getId(), categoryName,
            getListingTitle().get(), getListingDescription().get(),
            Double.parseDouble(getListingPrice().get()), getListingCity().get(),
            getListingCondition().get(), model.getCurrentUser()));
  }

  public ObservableList<String> getAllCategories()
  {
    ArrayList<String> categories = model.getAllCategories();
    categories.remove("All");
    categories.remove("<none>");
    return FXCollections.observableList(categories);
  }
}
