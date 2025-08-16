# Project Specification: Car Rental Management System

Your task is to design and implement a **Java GUI application** to manage a car rental service. This system must handle inventory management, customer rentals and returns, searching, sorting, and administrative tasks. The system should be integrated with a **database** to ensure data persistence. If database integration is not feasible, you may use file-based storage or in-memory arrays with a clear justification.  

---

## User Roles & Functions   
The application must support two distinct user roles, each with its own set of permissions: an **Admin** and a **Staff Member**.

* **Admin Functions:**  
  * **Manage Users:** Add, edit, delete, and view user accounts for staff members.  
  * **Manage Cars:** Add new cars to the inventory, and edit or remove existing ones. Car details should include: make, model, year, license plate, color, and rental status (available, rented, or under maintenance).  
  * **Generate Reports:** View comprehensive reports on rental history, car availability, and revenue statistics.  
  * **System Logs:** Access system logs to monitor activity and troubleshoot issues.  
      
* **Staff Member Functions:**  
  * **Manage Rentals:** Check out cars to customers and process returns. This includes recording the start and end dates of the rental period.  
  * **View & Update Availability:** Quickly check the rental status of any car and update it as needed.  
  * **Search Functionality:** Search for cars by make, model, or license plate.  
  * **Customer Management:** View and edit customer details, including name, contact information, and rental history.

---

## Data Management

**Database storage is compulsory.** You are required to integrate your Java application with a relational database (e.g., MySQL, PostgreSQL, or SQLite) to store all data. This includes car inventory, user accounts, and rental records.

### Sample Justification (for non-database systems):

In this project, it was originally intended to connect the system to a relational database (e.g., MySQL). Due to technical constraints, such as a lack of server access and time limitations for configuring a database, this implementation uses a **file-based storage system** (e.g., CSV files or serialized objects) to handle data persistence. All core logic and features are preserved, and the project demonstrates proper data handling in Java. The limitations of this approach, such as potential concurrency issues and reduced data integrity, are acknowledged in the final report.*  

---

## Deliverables

Your submission must include the following:

* **Software Submission:** A complete Java GUI project in a ZIP file, including all source files, any external libraries (e.g., JDBC driver), and database files if a local database (like SQLite) is used.  
    
* **Documentation (PDF or Word):**  
  * Cover Page & Table of Contents  
  * Introduction & Assumptions: A brief overview of the project and any key assumptions made.  
  * System Design:  
    * Pseudocode, flowchart, or UML diagrams.  
    * ER Diagram (Entity-Relationship Diagram) if a database is used.  
  * Implementation:  
    * Code snippets with explanations.  
    * Detailed description of database queries or file handling logic.  
  * Sample Output Screens: Screenshots of the main GUI interfaces.  
  * References: List of any external resources.  
  * Reflection: A summary of your experience, challenges, and what you learned.  
  * Workload Matrix: A breakdown of tasks if it is a group project.

---

## Presentation Requirements

* **Group Presentation (Max 10 minutes):**  
  * Deliver a concise presentation of the key features.  
* **Screen-Recorded Demo (5 minutes):**  
  * Submit a screen-recorded demo showing the application in action.  
  * Showcase a clean GUI design, proper event handling, and exception management.

---

## Grading Criteria

* **Distinction (80-100%):** All requirements met, creative and unique system design, use of advanced Java concepts and database features (e.g., transactions, prepared statements), strong documentation, and clear justification.  
* **Credit (65-79%):** Most requirements met, functional and well-organized, good data validation and clear logic, and solid presentation.  
* **Pass (50-64%):** Basic requirements met, minor logic or design issues, and average documentation.  
* **Fail (\<50%):** Many requirements missing, major errors, incomplete implementation, or poor documentation.

