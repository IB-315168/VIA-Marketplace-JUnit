package com.sep2zg4.viamarket.client;

import com.sep2zg4.viamarket.client.model.MarketplaceModelManager;
import com.sep2zg4.viamarket.client.view.ViewHandler;
import com.sep2zg4.viamarket.client.viewmodel.ViewModelFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;

public class Start extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    MarketplaceModelManager model = new MarketplaceModelManager();
    ViewModelFactory viewModelFactory = new ViewModelFactory(model);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(primaryStage);
    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent t) {
        try
        {
          model.closeCommunicator();
        }
        catch (RemoteException e)
        {
          viewHandler.displayAlert(Alert.AlertType.ERROR, e.getMessage());
        }
        Platform.exit();
        System.exit(0);
      }
    });
  }
  public static void main(String[] args) throws RemoteException
  {
    launch();
  }
}
