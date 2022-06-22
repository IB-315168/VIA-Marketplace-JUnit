package com.sep2zg4.viamarket.client.view;

import com.sep2zg4.viamarket.client.viewmodel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * Controller class for LogInView.fxml
 *
 * @author Rojus Paukste
 * @version 1.8 - May 2022
 */
public class LoginViewController
{
  @FXML private TextField userNameTextField;
  @FXML private PasswordField userPasswordTextField;

  private ViewHandler viewHandler;
  private LoginViewModel viewModel;
  private Region root;

  public void init(ViewHandler viewHandler, LoginViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    userNameTextField.textProperty().bindBidirectional(viewModel.getUserName());
    userPasswordTextField.textProperty()
        .bindBidirectional(viewModel.getUserPassword());
  }

  @FXML public void logIn()
  {
    try
    {
      if (viewModel.login())
      {
        viewHandler.closeView();
        viewHandler.openView(ViewHandler.LISTINGS);
      }
      reset();
    }
    catch (Exception e)
    {
      viewHandler.displayAlert(Alert.AlertType.ERROR, e.getMessage());
    }
    userPasswordTextField.getText();
  }

  public Region getRoot()
  {
    return root;
  }

  public void reset()
  {
    userNameTextField.setText("");
    userPasswordTextField.setText("");
  }
}
