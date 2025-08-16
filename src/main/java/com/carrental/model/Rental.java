package com.carrental.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Rental entity class representing rental transactions in the system
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class Rental {

  private int id;
  private int carId;
  private int customerId;
  private int staffId;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal totalCost;
  private String status; // ACTIVE, COMPLETED, CANCELLED
  private LocalDateTime createdDate;

  // Additional fields for display purposes
  private Car car;
  private Customer customer;
  private User staff;

  // Default constructor
  public Rental() {
  }

  // Constructor with parameters
  public Rental(int carId, int customerId, int staffId, LocalDate startDate, LocalDate endDate, BigDecimal totalCost) {
    this.carId = carId;
    this.customerId = customerId;
    this.staffId = staffId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.totalCost = totalCost;
    this.status = "ACTIVE";
  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCarId() {
    return carId;
  }

  public void setCarId(int carId) {
    this.carId = carId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getStaffId() {
    return staffId;
  }

  public void setStaffId(int staffId) {
    this.staffId = staffId;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  // Additional getters for related objects
  public Car getCar() {
    return car;
  }

  public void setCar(Car car) {
    this.car = car;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public User getStaff() {
    return staff;
  }

  public void setStaff(User staff) {
    this.staff = staff;
  }

  /**
   * Calculate the number of days for this rental
   */
  public long getNumberOfDays() {
    if (startDate != null && endDate != null) {
      return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    return 0;
  }

  @Override
  public String toString() {
    return String.format("Rental #%d: %s to %s - %s", id, startDate, endDate, status);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Rental rental = (Rental) obj;
    return id == rental.id;
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id);
  }
}
