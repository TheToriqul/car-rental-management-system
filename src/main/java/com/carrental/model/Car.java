package com.carrental.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Car entity class representing cars in the rental system
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class Car {

  private int id;
  private String make;
  private String model;
  private int year;
  private String licensePlate;
  private String color;
  private String status; // AVAILABLE, RENTED, MAINTENANCE
  private BigDecimal dailyRate;
  private LocalDateTime createdDate;

  // Default constructor
  public Car() {
  }

  // Constructor with parameters
  public Car(String make, String model, int year, String licensePlate, String color, BigDecimal dailyRate) {
    this.make = make;
    this.model = model;
    this.year = year;
    this.licensePlate = licensePlate;
    this.color = color;
    this.dailyRate = dailyRate;
    this.status = "AVAILABLE";
  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getLicensePlate() {
    return licensePlate;
  }

  public void setLicensePlate(String licensePlate) {
    this.licensePlate = licensePlate;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getDailyRate() {
    return dailyRate;
  }

  public void setDailyRate(BigDecimal dailyRate) {
    this.dailyRate = dailyRate;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public String toString() {
    return String.format("%s %s %s (%s) - %s", make, model, year, licensePlate, status);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Car car = (Car) obj;
    return id == car.id && licensePlate.equals(car.licensePlate);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id, licensePlate);
  }
}
