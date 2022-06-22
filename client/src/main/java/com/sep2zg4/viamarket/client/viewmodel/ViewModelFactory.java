package com.sep2zg4.viamarket.client.viewmodel;

import com.sep2zg4.viamarket.client.model.MarketplaceModel;

/**
 * ViewModelFactory for ViaMarketplace
 *
 * @author Wojtek Rusinski
 * @version 1.6 - May 2022
 */
public class ViewModelFactory
{
  private final ListingsViewModel listingsViewModel;
  private final LoginViewModel loginViewModel;
  private final UserInformationViewModel userInformationViewModel;
  private final ListingFormViewModel listingFormViewModel;

  public ViewModelFactory(MarketplaceModel model)
  {
    listingsViewModel = new ListingsViewModel(model);
    loginViewModel = new LoginViewModel(model);
    userInformationViewModel = new UserInformationViewModel(model);
    listingFormViewModel = new ListingFormViewModel(model);
  }

  public ListingsViewModel getListingsViewModel()
  {
    return listingsViewModel;
  }

  public LoginViewModel getLoginViewModel()
  {
    return loginViewModel;
  }

  public UserInformationViewModel getUserInformationViewModel()
  {
    return userInformationViewModel;
  }

  public ListingFormViewModel getListingFormViewModel()
  {
    return listingFormViewModel;
  }
}
