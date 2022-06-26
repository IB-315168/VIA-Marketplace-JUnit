package com.sep2zg4.viamarket.model;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;
import java.util.Objects;

/**
 * Class representing an item on the marketplace.
 *
 * @author Igor Bulinski
 * @version 1.0 - April 2022
 */
public class Listing implements Serializable
{
  private int id;
  private String categoryName;
  private String title;
  private String description;
  private double price;
  private String city;
  private String condition;
  private User seller;

  /**
   * 8-argument constructor with constraints from {@link com.sep2zg4.viamarket.model.Listing#set(int, String, String, String, double, String, String, User)} method
   *
   * @param id           listings id, required bigger than 0
   * @param categoryName name of the category listing belongs to
   * @param title        listings title, required not-blank
   * @param description  listings description, required not-blank
   * @param price        listings price, required bigger than 0.0
   * @param city         listings location (i.e. where is the listing available for pick-up)
   * @param condition    listed item condition, required to be either "New", "Used" or "Defective"
   * @param seller       {@link User} listings owner
   * @throws IllegalArgumentException if argument conditions are not met
   */
  public Listing(int id, String categoryName, String title, String description, double price, String city,
      String condition, User seller) throws IllegalArgumentException,
      RemoteException
  {
    set(id, categoryName, title, description, price, city, condition, seller);
  }

  /**
   * 8-argument method for setting listings properties
   *
   * @param id          listings id, required bigger than 0
   * @param categoryName name of the category listing belongs to
   * @param title       listings title, required not-blank
   * @param description listings description, required not-blank
   * @param price       listings price, required bigger than 0.0
   * @param city        listings location (i.e. where is the listing available for pick-up)
   * @param condition   listed item condition, required to be either "New", "Used" or "Defective"
   * @param seller      {@link User} listings owner
   * @throws IllegalArgumentException if argument conditions are not met
   */
  public void set(int id, String categoryName, String title, String description, double price, String city,
      String condition, User seller) throws IllegalArgumentException,
      RemoteException {
    if(id < 1) {
      throw new IllegalArgumentException("Id cannot be lower than zero");
    }
    this.id = id;

    this.categoryName = categoryName;

    if(title == null || title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be empty.");
    }
    this.title = title;

    if(description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be empty");
    }
    this.description = description;

    if(price < 0.0) {
      throw new IllegalArgumentException("Price cannot be lower than zero");
    }
    this.price = price;

    this.city = city;

    if(condition == null || !(condition.toLowerCase(Locale.ROOT).equals("new") || condition.toLowerCase(Locale.ROOT).equals("used") || condition.toLowerCase(
        Locale.ROOT).equals("defective"))) {
      throw new IllegalArgumentException("Condition must be either New, Used or Defective");
    }
    this.condition = condition;

    if(seller == null) {
      throw new IllegalArgumentException("Seller cannot be null");
    }
    this.seller = seller;
  }

  public int getId()
  {
    return id;
  }

  public String getCategoryName()
  {
    return categoryName;
  }

  public void setCategoryName(String categoryName)
  {
    this.categoryName = categoryName;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public double getPrice()
  {
    return price;
  }

  public String getCity()
  {
    return city;
  }

  public String getCondition()
  {
    return condition;
  }

  public User getSeller()
  {
    return seller;
  }

  @Override public String toString() {
    return title + " - " + price + "DKK";
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Listing listing = (Listing) o;
    return id == listing.id && Double.compare(listing.price, price) == 0
        && Objects.equals(categoryName, listing.categoryName) && Objects.equals(
        title, listing.title) && Objects.equals(description,
        listing.description) && Objects.equals(city, listing.city)
        && Objects.equals(condition, listing.condition) && Objects.equals(seller, listing.seller);
  }
}
