package com.sep2zg4.viamarket.client.view;

import com.sep2zg4.viamarket.client.viewmodel.UserInformationViewModel;
import com.sep2zg4.viamarket.model.Listing;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Controller class for UserInformationView.fxml
 *
 * @author Rojus Paukste
 * @version 2.0 - May 2022
 */
public class UserInformationViewController
{
  @FXML private Label userName;
  @FXML private ListView<Listing> userListings;
  @FXML private ListView<Listing> userWishList;
  @FXML private Label title;
  @FXML private Label price;
  @FXML private Label city;
  @FXML private Label condition;
  @FXML private Label contacts;
  @FXML private TextArea description;

  private ViewHandler viewHandler;
  private UserInformationViewModel viewModel;
  private Region root;

  public void init(ViewHandler viewHandler, UserInformationViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;
    this.userName.textProperty().bindBidirectional(viewModel.getFullName());
    this.userListings.setItems(viewModel.getUserListings());
    this.userWishList.setItems(viewModel.getUserWishlist());
    this.userListings.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Listing>()
        {
          @Override public void changed(
              ObservableValue<? extends Listing> observable, Listing oldValue,
              Listing newValue)
          {
            viewModel.setCurrentSelectedUserListing(newValue);
          }
        });
    this.userWishList.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Listing>()
        {
          @Override public void changed(
              ObservableValue<? extends Listing> observable, Listing oldValue,
              Listing newValue)
          {
            if (newValue != null)
            {
              title.setText(newValue.getTitle());
              price.setText(String.valueOf(newValue.getPrice()));
              city.setText(newValue.getCity());
              condition.setText(newValue.getCondition());
              contacts.setText(newValue.getSeller().getFullName() + "\n"
                  + newValue.getSeller().getEmail() + "\n"
                  + newValue.getSeller().getPhoneNumber());
              description.setText(newValue.getDescription());
            }
          }
        });
  }

  @FXML public void goBack()
  {
    viewHandler.closeView();
    viewHandler.openView(ViewHandler.LISTINGS);
  }

  @FXML public void create()
  {
    viewModel.setCurrentSelectedUserListing(null);
    viewHandler.closeView();
    viewHandler.openView(ViewHandler.LISTINGFORM);
  }

  @FXML public void edit()
  {
    viewHandler.closeView();
    viewHandler.openView(ViewHandler.LISTINGFORM);
  }

  @FXML public void remove() throws SQLException, RemoteException
  {
    Listing listing = userListings.getSelectionModel().getSelectedItem();
    viewModel.deleteListing(listing);
  }

  @FXML public void deleteWishlistItem()
  {
    Listing wishlistListing = userWishList.getSelectionModel()
        .getSelectedItem();
    try
    {
      viewModel.deleteWishlistItem(wishlistListing);
    }
    catch (SQLException | RemoteException e)
    {
      viewHandler.displayAlert(Alert.AlertType.ERROR, e.getMessage());
    }
  }

  public Region getRoot()
  {
    return root;
  }

  public void reset()
  {
    viewModel.setFullname();
  }
}
