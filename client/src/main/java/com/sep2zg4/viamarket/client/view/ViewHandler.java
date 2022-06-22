package com.sep2zg4.viamarket.client.view;

import com.sep2zg4.viamarket.client.viewmodel.ViewModelFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * An MVVM pattern class used for managing different stages of the application
 *
 * @author Rojus Paukste
 * @version 1.0 - April 2022
 */

public class ViewHandler
{
  public static final String LOGIN = "login";
  public static final String LISTINGS = "listings";
  public static final String USERINFO = "userInfo";
  public static final String LISTINGFORM = "listingForm";
  private final Scene currentScene;
  private Stage primaryStage;
  private final ViewFactory viewFactory;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewFactory = new ViewFactory(this, viewModelFactory);
    this.currentScene = new Scene(new Region());
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    openView(LOGIN);
  }

  public void openView(String view)
  {
    Region root = switch (view)
        {
          case LOGIN -> viewFactory.loadLogInView();
          case LISTINGS -> viewFactory.loadListingsView();
          case USERINFO -> viewFactory.loadUserInformationView();
          case LISTINGFORM -> viewFactory.loadListingFormView();
          default -> throw new IllegalArgumentException("Unknown bruh.view");
        };
    currentScene.setRoot(root);
    if (root.getUserData() == null)
      primaryStage.setTitle("");
    else
      primaryStage.setTitle(root.getUserData().toString());
    primaryStage.setScene(currentScene);
    primaryStage.sizeToScene();
    primaryStage.show();
  }

  public void closeView()
  {
    primaryStage.close();
  }

  public void displayAlert(Alert.AlertType alertType, String content)
  {
    Alert alert = new Alert(alertType);
    alert.setContentText(content);
    alert.show();
  }
}
