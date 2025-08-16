# 🚗 Car Rental Management System

A comprehensive Java GUI application for managing car rental services with SQLite database integration, supporting Admin and Staff user roles with full CRUD operations and reporting capabilities.

## 📋 Project Overview

This project is developed as part of the **PRG2201E Programming Coursework** at INTI International University. The system provides a complete solution for car rental businesses to manage their inventory, customers, rentals, and administrative tasks through an intuitive graphical user interface.

## ✨ Features

### 🔐 Authentication & Security

- **Role-based Access Control**: Separate interfaces for Admin and Staff users
- **Secure Login System**: Password hashing and session management
- **User Management**: Admin can create, edit, delete, and manage staff accounts

### 🚗 Car Management (Admin)

- **Inventory Management**: Add, edit, delete, and view car details
- **Car Details**: Make, model, year, license plate, color, status, daily rate
- **Status Tracking**: Available, Rented, Under Maintenance
- **Search & Filter**: Find cars by make, model, or license plate

### 👥 Customer Management (Staff)

- **Customer Database**: Store customer information (name, email, phone, address)
- **Rental History**: Track customer rental history and preferences
- **Customer Search**: Quick search and filter customer records

### 📅 Rental Operations (Staff)

- **Car Checkout**: Process car rentals with date validation
- **Car Returns**: Handle returns with automatic cost calculation
- **Rental Tracking**: Monitor active rentals and their status
- **Date Validation**: Prevent invalid rental periods

### 📊 Reporting System (Admin)

- **Rental History Reports**: Comprehensive rental transaction history
- **Car Availability Reports**: Real-time car availability status
- **Revenue Statistics**: Financial reports and analytics
- **Export Functionality**: Generate reports in various formats

### 🔍 Search & Filter

- **Advanced Search**: Search cars by multiple criteria
- **Availability Check**: Real-time car availability status
- **Filter Options**: Filter by status, make, model, etc.

## 🛠️ Technology Stack

- **Backend**: Java 15+
- **GUI Framework**: Java Swing
- **Database**: SQLite with JDBC
- **Build Tool**: Maven
- **Architecture**: MVC Pattern with DAO Design Pattern

## 📁 Project Structure

```
car-rental-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── carrental/
│   │   │           ├── model/          # Entity classes
│   │   │           ├── dao/            # Data Access Objects
│   │   │           ├── service/        # Business Logic
│   │   │           ├── gui/            # User Interface
│   │   │           ├── util/           # Utilities
│   │   │           └── Main.java       # Application entry point
│   │   └── resources/
│   │       └── database/
│   │           └── carrental.db        # SQLite database
├── docs/                               # Documentation
├── screenshots/                        # Application screenshots
├── pom.xml                            # Maven configuration
└── README.md                          # Project documentation
```

## 🗄️ Database Schema

### Core Tables

- **users**: User accounts and authentication
- **cars**: Car inventory and details
- **customers**: Customer information
- **rentals**: Rental transactions and history

### Relationships

- Users can manage multiple rentals
- Cars can have multiple rental records
- Customers can have multiple rental history

## 🚀 Getting Started

### Prerequisites

- Java 15 or higher
- Maven 3.6+
- SQLite JDBC Driver (included in pom.xml)

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/TheToriqul/car-rental-management-system.git
   cd car-rental-management-system
   ```

2. **Build the project**

   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   java -jar target/car-rental-management-system-1.0.0.jar
   ```

### Default Login Credentials

- **Admin**: username: `admin`, password: `admin123`
- **Staff**: username: `staff`, password: `staff123`

## 📸 Screenshots

_Screenshots will be added as the project develops_

## 🎯 User Roles & Permissions

### Admin User

- ✅ Manage user accounts (create, edit, delete)
- ✅ Manage car inventory (add, edit, delete cars)
- ✅ Generate comprehensive reports
- ✅ View system logs and activity
- ✅ Access all system features

### Staff User

- ✅ Process car rentals and returns
- ✅ Manage customer information
- ✅ Search and view car availability
- ✅ Generate basic reports
- ✅ Limited administrative access

## 🔧 Development

### Architecture Patterns

- **MVC (Model-View-Controller)**: Separation of concerns
- **DAO (Data Access Object)**: Database abstraction layer
- **Service Layer**: Business logic encapsulation
- **Singleton**: Database connection management

### Key Design Principles

- **SOLID Principles**: Clean, maintainable code
- **Exception Handling**: Robust error management
- **Input Validation**: Data integrity protection
- **Security**: SQL injection prevention, password hashing

## 📊 Project Status

- [x] Project planning and requirements analysis
- [x] Database schema design
- [x] Core backend implementation
- [x] Basic GUI framework
- [ ] Complete GUI development
- [ ] Testing and debugging
- [ ] Documentation completion
- [ ] Final submission preparation

## 🤝 Contributing

This is an academic project for PRG2201E Programming Coursework. For questions or suggestions, please contact the development team.

## 📝 License

This project is developed for educational purposes as part of the PRG2201E Programming Coursework at INTI International University.

## 👨‍💻 Developer

**Md Toriqul Islam** ([@TheToriqul](https://github.com/TheToriqul))

- DevOps & Cloud Solutions Engineer
- Specializing in AWS, Kubernetes, and Infrastructure Automation
- Currently pursuing BSc in Computer Science (Cloud Computing) at INTI International University

### Connect

- 🌐 [Portfolio](https://thetoriqul.com/)
- 💼 [LinkedIn](https://linkedin.com/in/thetoriqul)
- 🐦 [Twitter](https://twitter.com/TheToriqul_X)
- 📧 Email: [Contact via Portfolio](https://thetoriqul.com/)

## 📚 Course Information

- **Course**: PRG2201E Programming
- **Institution**: INTI International University
- **Semester**: August 2025
- **Project Type**: Group Assignment
- **Duration**: 2 weeks
- **Technology**: Java GUI with SQLite Database

---

**Note**: This project is developed as part of academic coursework and demonstrates proficiency in Java programming, database management, and GUI development principles.
