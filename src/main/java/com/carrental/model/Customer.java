package com.carrental.model;

import java.time.LocalDateTime;

/**
 * Customer entity class representing customers in the rental system
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class Customer {

  private int id;
  private String name;
  private String email;
  private String phone;
  private String address;
  private LocalDateTime createdDate;

  // Default constructor
  public Customer() {
  }

  // Constructor with parameters
  public Customer(String name, String email, String phone, String address) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public String toString() {
    return String.format("%s (%s) - %s", name, email, phone);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Customer customer = (Customer) obj;
    return id == customer.id && email.equals(customer.email);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id, email);
  }
}
