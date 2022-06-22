package com.sep2zg4.viamarket.client.view;

import com.sep2zg4.viamarket.client.viewmodel.ListingFormViewModel;
import com.sep2zg4.viamarket.model.Listing;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Controller class for ListingsFormView.fxml
 *
 * @author Rojus Paukste
 * @version 2.2 - May 2022
 */
public class ListingFormViewController
{
  @FXML private TextField title;
  @FXML private TextField city;
  @FXML private TextField price;
  @FXML private ToggleGroup condition;
  @FXML private ChoiceBox<String> category;
  @FXML private TextArea description;
  @FXML private RadioButton conditionDefective;
  @FXML private RadioButton conditionUsed;
  @FXML private RadioButton conditionNew;

  private ViewHandler viewHandler;
  private ListingFormViewModel viewModel;
  private Region root;

  public void init(ViewHandler viewHandler, ListingFormViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    this.conditionNew.setUserData("New");
    this.conditionUsed.setUserData("Used");
    this.conditionDefective.setUserData("Defective");

    this.category.setItems(viewModel.getAllCategories());
    this.title.textProperty().bindBidirectional(viewModel.getListingTitle());
    this.city.textProperty().bindBidirectional(viewModel.getListingCity());
    this.price.textProperty().bindBidirectional(viewModel.getListingPrice());
    this.description.textProperty()
        .bindBidirectional(viewModel.getListingDescription());
    this.condition.selectedToggleProperty()
        .addListener(new ChangeListener<Toggle>()
        {
          @Override public void changed(
              ObservableValue<? extends Toggle> observable, Toggle oldValue,
              Toggle newValue)
          {

            viewModel.getListingCondition()
                .setValue(newValue.getUserData().toString());
          }
        });
  }

  @FXML public void save() throws SQLException, RemoteException
  {
    if (viewModel.getSelectedUserListing() == null)
    {
      viewModel.createListing(category.getSelectionModel().getSelectedItem());
    }
    else
    {
      viewModel.updateListing(category.getSelectionModel().getSelectedItem());
    }
    viewHandler.closeView();
    viewHandler.openView(ViewHandler.USERINFO);
  }

  @FXML public void goBack()
  {
    viewHandler.closeView();
    viewHandler.openView(ViewHandler.LISTINGS);
  }

  public Region getRoot()
  {
    return root;
  }

  public void reset()
  {
    if (viewModel.getSelectedUserListing() == null)
    {
      category.setValue("");
      condition.selectToggle(conditionNew);
      title.setText("");
      price.setText("");
      city.setText("");
      description.setText("");
    }
    else
    {
      Listing listing = viewModel.getSelectedUserListing();
      category.setValue(listing.getCategoryName());
      condition.selectToggle(
          switch (listing.getCondition().toLowerCase(Locale.ROOT))
              {
                case "new" -> conditionNew;
                case "used" -> conditionUsed;
                case "defective" -> conditionDefective;
                default -> throw new IllegalStateException(
                    "Unexpected value: " + listing.getCondition());
              });
      title.setText(listing.getTitle());
      price.setText(String.valueOf(listing.getPrice()));
      city.setText(listing.getCity());
      description.setText(listing.getDescription());
    }
  }
}
