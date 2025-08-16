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

### 👤 User Management (Admin)

- **User Account Management**: Create, edit, delete user accounts
- **Role Assignment**: Assign Admin or Staff roles
- **User Search**: Find users by username or role
- **Access Control**: Manage system access permissions

### 🔍 Search & Filter

- **Advanced Search**: Search cars by multiple criteria
- **Availability Check**: Real-time car availability status
- **Filter Options**: Filter by status, make, model, etc.

## 🛠️ Technology Stack

- **Backend**: Java 15+
- **GUI Framework**: Java Swing
- **Database**: SQLite with JDBC
- **Build System**: Custom scripts with automated dependency management
- **Dependencies**: SQLite JDBC, Apache Commons Codec, Apache Commons Lang3, SLF4J
- **Architecture**: MVC Pattern with DAO Design Pattern and Service Layer

## 📁 Project Structure

```
car-rental-management-system/
├── 📦 src/main/java/com/carrental/
│   ├── model/               # Domain Entities (User, Car, Customer, Rental)
│   ├── dao/                 # Data Access Layer (UserDAO, CarDAO, CustomerDAO, RentalDAO)
│   ├── service/             # Business Logic Layer (AuthService, CarService, etc.)
│   ├── gui/                 # Presentation Layer (LoginFrame, AdminDashboard, etc.)
│   ├── util/                # Utility Classes (DatabaseManager)
│   └── Main.java            # Application entry point
├── 📚 docs/                 # Project Documentation
│   ├── deliverables/        # User Manual, Demo Script, Project Summary
│   ├── technical/           # Architecture and Database Schema
│   └── project-planning/    # Project Requirements and Implementation Plan
├── 🔧 scripts/              # Build & Deployment Scripts
│   ├── build.sh/.bat        # Automated build process
│   ├── run.sh/.bat          # Application launcher
│   ├── clean.sh/.bat        # Build artifact cleanup
│   ├── test.sh/.bat         # Test execution
│   └── setup.sh/.bat        # Environment setup
├── 🏗️ build/                # Build Artifacts
├── 📸 screenshots/          # Application Screenshots
└── 📖 README.md             # This file
```

## 🚀 Getting Started

### Prerequisites

- Java 15 or higher
- Internet connection (for initial dependency download)

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/TheToriqul/car-rental-management-system.git
   cd car-rental-management-system
   ```

2. **Build the project**

   ```bash
   # Windows
   scripts\build.bat

   # Linux/macOS
   ./scripts/build.sh
   ```

3. **Run the application**

   ```bash
   # Windows
   scripts\run.bat

   # Linux/macOS
   ./scripts/run.sh
   ```

### Default Login Credentials

- **Admin**: username: `Toriq`, password: `toriq123`
- **Staff**: username: `Jenesh`, password: `jenesh123`

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

## 📚 Documentation

### Essential Documents

- **[User Manual](docs/deliverables/USER_MANUAL.md)** - Complete system usage guide
- **[Technical Architecture](docs/technical/architecture.md)** - System design documentation
- **[Database Schema](docs/technical/database-schema.md)** - Database design documentation
- **[Database Features](docs/technical/database-features.md)** - Advanced database implementation
- **[System Design Diagrams](docs/technical/system-design-diagrams.md)** - Mermaid diagrams
- **[Project Summary](docs/deliverables/FINAL_PROJECT_SUMMARY.md)** - Complete project overview
- **[Demo Script](docs/deliverables/DEMO_SCRIPT.md)** - Presentation guide

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
- **Database Features**: Transactions, prepared statements, ACID properties

## 📊 Project Status

**🎉 Project Status: COMPLETE & FULLY FUNCTIONAL**

- ✅ All core requirements implemented
- ✅ Complete CRUD operations for all entities
- ✅ Role-based access control
- ✅ Professional user interface
- ✅ Comprehensive reporting system
- ✅ Cross-platform build scripts
- ✅ Complete documentation

## 👨‍💻 Developer

**Md Toriqul Islam** ([@TheToriqul](https://github.com/TheToriqul))

- DevOps & Cloud Solutions Engineer
- Specializing in AWS, Kubernetes, and Infrastructure Automation
- Currently pursuing BSc in Computer Science (Cloud Computing) at INTI International University

### Connect

- 🌐 [Portfolio](https://thetoriqul.com/)
- 💼 [LinkedIn](https://linkedin.com/in/thetoriqul)
- 🐦 [Twitter](https://twitter.com/TheToriqul_X)

## 📚 Course Information

- **Course**: PRG2201E - Object Oriented Programming
- **Institution**: INTI International University
- **Semester**: August 2025
- **Project Type**: Group Assignment
- **Duration**: 2 weeks
- **Technology**: Java GUI with SQLite Database

---

**Note**: This project is developed as part of academic coursework and demonstrates proficiency in Java programming, database management, and GUI development principles.
