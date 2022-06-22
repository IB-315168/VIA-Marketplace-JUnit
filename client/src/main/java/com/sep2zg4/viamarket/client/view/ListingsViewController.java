package com.sep2zg4.viamarket.client.view;

import com.sep2zg4.viamarket.client.viewmodel.ListingsViewModel;
import com.sep2zg4.viamarket.model.Listing;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * Controller class for ListingsView.fxml
 *
 * @author Rojus Paukste
 * @version 2.2 - May 2022
 */

public class ListingsViewController
{
  @FXML private ListView<String> categoryList;
  @FXML private ListView<Listing> listingsList;
  @FXML private Label title;
  @FXML private Label price;
  @FXML private Label city;
  @FXML private Label condition;
  @FXML private Label contacts;
  @FXML private TextArea description;
  @FXML private Label loggedAs;
  @FXML private ToggleGroup sort;
  @FXML private ToggleGroup filter;
  @FXML private RadioButton mostRecent;
  @FXML private RadioButton lowToHigh;
  @FXML private RadioButton HighToLow;
  @FXML private TextField searchKey;
  @FXML private Menu moderatorPanel;
  @FXML private TextField minimumPrice;
  @FXML private TextField maximumPrice;
  @FXML private RadioButton conditionNew;
  @FXML private RadioButton conditionUsed;
  @FXML private RadioButton conditionDefective;
  @FXML private MenuItem addToWishlistMI;
  private ArrayList<Listing> searchResults;

  private ViewHandler viewHandler;
  private ListingsViewModel viewModel;
  private Region root;

  public void init(ViewHandler viewHandler, ListingsViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    moderatorPanel.setVisible(false);

    this.categoryList.setItems(viewModel.getCategoryList());
    this.listingsList.setItems(viewModel.getListingsList());
    this.loggedAs.textProperty().bindBidirectional(viewModel.getUserType());
    viewModel.setListingsList();
    viewModel.setCategoryList();

    viewModel.setUserType();

    if (viewModel.isModerator())
    {
      moderatorPanel.setVisible(true);
    }

    conditionNew.setUserData(1);
    conditionUsed.setUserData(2);
    conditionDefective.setUserData(3);

    mostRecent.setUserData(1);
    lowToHigh.setUserData(2);
    HighToLow.setUserData(3);

    this.sort.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
    {
      @Override public void changed(
          ObservableValue<? extends Toggle> observable, Toggle oldValue,
          Toggle newValue)
      {

        //        System.out.println(newValue.getUserData().toString());
        System.out.println(viewModel.getListingsList());
        if (newValue != null)
        {
          switch (newValue.getUserData().toString())
          {
            case "1":
              Collections.sort(viewModel.getListingsList(),
                  (o1, o2) -> o2.getId() - o1.getId());
              if (searchResults != null)
                Collections.sort(searchResults,
                    (o1, o2) -> o2.getId() - o1.getId());
              break;
            case "2":
              Collections.sort(viewModel.getListingsList(),
                  (o1, o2) -> (int) (o1.getPrice() - o2.getPrice()));
              if (searchResults != null)
                Collections.sort(searchResults,
                    (o1, o2) -> (int) (o1.getPrice() - o2.getPrice()));
              break;
            case "3":
              Collections.sort(viewModel.getListingsList(),
                  (o1, o2) -> (int) (o2.getPrice() - o1.getPrice()));
              if (searchResults != null)
                Collections.sort(searchResults,
                    (o1, o2) -> (int) (o2.getPrice() - o1.getPrice()));
              break;
          }
        }

      }
    });

    this.categoryList.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<String>()
        {
          @Override public void changed(
              ObservableValue<? extends String> observable, String oldValue,
              String newValue)
          {
            if (newValue == null)
            {
              viewModel.setListingsList();
            }
            else
            {
              viewModel.getByCategory(newValue);
            }
          }
        });

    this.listingsList.getSelectionModel().selectedItemProperty()
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
              ArrayList<Listing> wishlistItems = viewModel.getWishlistItem();
              addToWishlistMI.setDisable(false);
              for (Listing listing : wishlistItems)
              {
                if (listing.getId() == newValue.getId())
                {
                  addToWishlistMI.setDisable(true);
                }
              }
            }
          }
        });
  }

  @FXML public void goBack()
  {
    viewHandler.closeView();
    viewHandler.openView(ViewHandler.LOGIN);
  }

  @FXML public void userInformation()
  {
    viewHandler.closeView();
    viewHandler.openView(ViewHandler.USERINFO);
  }

  @FXML public void deleteListing() throws SQLException, RemoteException
  {
    if (viewModel.isModerator())
    {
      Listing listing = listingsList.getSelectionModel().getSelectedItem();
      viewModel.deleteListing(listing);
    }
    else
    {
      viewHandler.displayAlert(Alert.AlertType.ERROR,
          "User does not have permission to do this action.");
    }
  }

  @FXML public void createCategory(ActionEvent event)
      throws SQLException, RemoteException
  {
    if (viewModel.isModerator())
    {
      TextInputDialog textInputDialog = new TextInputDialog();
      textInputDialog.setTitle("Create category");
      textInputDialog.getDialogPane().setContentText("Category name: ");
      Optional<String> result = textInputDialog.showAndWait();
      TextField input = textInputDialog.getEditor();
      viewModel.createCategory(input.getText());
    }
    else
    {
      viewHandler.displayAlert(Alert.AlertType.ERROR,
          "User does not have permission to do this action.");
    }
  }

  @FXML public void deleteCategory() throws SQLException, RemoteException
  {
    if (viewModel.isModerator())
    {
      String category = categoryList.getSelectionModel().getSelectedItem();
      viewModel.deleteCategory(category);
    }
    else
    {
      viewHandler.displayAlert(Alert.AlertType.ERROR,
          "User does not have permission to do this action.");
    }
  }

  @FXML public void addToWishlist()
  {
    Listing wishlistListing = listingsList.getSelectionModel()
        .getSelectedItem();

    try
    {
      viewModel.addToWishlist(wishlistListing);
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
    moderatorPanel.setVisible(viewModel.isModerator());
    viewModel.setUserType();
    clear();
  }

  @FXML public void clear()
  {
    searchKey.setText("");
    conditionNew.selectedProperty().set(false);
    conditionUsed.selectedProperty().set(false);
    conditionDefective.selectedProperty().set(false);
    minimumPrice.setText("");
    maximumPrice.setText("");
    sort.selectToggle(null);
    this.listingsList.setItems(viewModel.getListingsList());
  }

  @FXML public void search(ActionEvent actionEvent)
  {
    searchResults = new ArrayList<Listing>();
    for (Listing listing : viewModel.getListingsList())
    {
      if (listing.getTitle().toLowerCase()
          .contains(searchKey.getText().toLowerCase()))
      {
        searchResults.add(listing);
      }
    }
    this.listingsList.setItems(FXCollections.observableList(searchResults));
    if (searchKey.getText().equals(""))
    {
      this.listingsList.setItems(viewModel.getListingsList());
    }
  }

  @FXML public void filter(ActionEvent actionEvent)
  {
    ArrayList<Listing> filterResults = new ArrayList<>(
        viewModel.getListingsList());

    if (!minimumPrice.getText().isEmpty())
    {
      double minPrice = 0.0;
      try
      {
        minPrice = Double.parseDouble(minimumPrice.getText());
      }
      catch (Exception e)
      {
        viewHandler.displayAlert(Alert.AlertType.ERROR, e.getMessage());
      }
      double finalMinPrice = minPrice;
      filterResults.removeIf(listing -> listing.getPrice() < finalMinPrice);
    }

    if (!maximumPrice.getText().isEmpty())
    {
      double maxPrice = 0.0;
      try
      {
        maxPrice = Double.parseDouble(maximumPrice.getText());
      }
      catch (Exception e)
      {
        viewHandler.displayAlert(Alert.AlertType.ERROR, e.getMessage());
      }
      double finalMaxPrice = maxPrice;
      filterResults.removeIf(listing -> listing.getPrice() > finalMaxPrice);
    }

    if (filter.getSelectedToggle() != null)
    {
      switch ((Integer) filter.getSelectedToggle().getUserData())
      {
        case 1:
          filterResults.removeIf(listing -> !listing.getCondition()
              .equals(conditionNew.getText()));
          break;
        case 2:
          filterResults.removeIf(listing -> !listing.getCondition()
              .equals(conditionUsed.getText()));
          break;
        case 3:
          filterResults.removeIf(listing -> !listing.getCondition()
              .equals(conditionDefective.getText()));
          break;
        default:
          return;
      }
    }

    this.listingsList.setItems(FXCollections.observableList(filterResults));
  }
}
