package com.carrental.service;

import com.carrental.dao.CustomerDAO;
import com.carrental.model.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service class for handling customer-related business logic
 * 
 * @author Md Toriqul Islam
 * @version 1.0.0
 */
public class CustomerService {
    
    private CustomerDAO customerDAO;
    
    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Create a new customer with validation
     */
    public boolean createCustomer(String name, String email, String phone, String address) throws Exception {
        
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        
        // Validate email format
        if (!isValidEmail(email.trim())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Check if email already exists
        if (customerDAO.emailExists(email.trim())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create customer object
        Customer customer = new Customer(name.trim(), email.trim(), phone.trim(), address.trim());
        
        return customerDAO.createCustomer(customer);
    }
    
    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int id) throws SQLException {
        return customerDAO.getCustomerById(id);
    }
    
    /**
     * Get customer by email
     */
    public Customer getCustomerByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return customerDAO.getCustomerByEmail(email.trim());
    }
    
    /**
     * Search customers
     */
    public List<Customer> searchCustomers(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllCustomers();
        }
        return customerDAO.searchCustomers(searchTerm.trim());
    }
    
    /**
     * Update customer
     */
    public boolean updateCustomer(Customer customer) throws Exception {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        
        // Validate required fields
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        
        // Validate email format
        if (!isValidEmail(customer.getEmail().trim())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        return customerDAO.updateCustomer(customer);
    }
    
    /**
     * Delete customer
     */
    public boolean deleteCustomer(int id) throws SQLException {
        return customerDAO.deleteCustomer(id);
    }
    
    /**
     * Check if customer exists
     */
    public boolean customerExists(int id) throws SQLException {
        return customerDAO.getCustomerById(id) != null;
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
