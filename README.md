# 🚗 Car Rental Management System

A comprehensive Java GUI application for managing car rental services with SQLite database integration, supporting Admin and Staff user roles with full CRUD operations and reporting capabilities.

## 📋 Project Overview

This project is developed as part of the **PRG2201E Programming Coursework** at INTI International University. The system provides a complete solution for car rental businesses to manage their inventory, customers, rentals, and administrative tasks through an intuitive graphical user interface.

**🎉 Project Status: COMPLETE & FULLY FUNCTIONAL**

- ✅ All core requirements implemented and tested
- ✅ Complete CRUD operations for all entities
- ✅ Role-based access control working perfectly
- ✅ Professional user interface with modern design
- ✅ Comprehensive reporting system operational
- ✅ Cross-platform build scripts verified
- ✅ Complete documentation and user manual
- ✅ Successfully tested on macOS (darwin 24.6.0)

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
- **GUI Framework**: Java Swing with Modern UI Theme
- **Database**: SQLite 3.44.1 with JDBC
- **Build System**: Custom scripts with automated dependency management
- **Dependencies**:
  - SQLite JDBC 3.44.1.0
  - Apache Commons Codec 1.15
  - Apache Commons Lang3 3.12.0
  - SLF4J API 1.7.36
  - SLF4J Simple 1.7.36
- **Architecture**: MVC Pattern with DAO Design Pattern and Service Layer
- **Platform Support**: Windows, macOS, and Linux

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
│   ├── car-rental-management-system.jar  # Executable JAR file
│   ├── classes/             # Compiled Java classes
│   └── lib/                 # Dependencies and libraries
├── 📸 screenshots/          # Application Screenshots and Documentation
└── 📖 README.md             # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java Runtime**: Java 15 or higher (Java 17+ recommended)
- **Operating System**: Windows 10/11, macOS 10.14+, or Linux
- **Memory**: 512MB RAM minimum, 1GB recommended
- **Storage**: 100MB available disk space
- **Internet**: Required for initial dependency download

### Quick Start

1. **Clone the repository**

   ```bash
   git clone https://github.com/TheToriqul/car-rental-management-system.git
   cd car-rental-management-system
   ```

2. **Build the project**

   ```bash
   # Windows
   scripts\build.bat

   # macOS/Linux
   chmod +x scripts/build.sh
   ./scripts/build.sh
   ```

3. **Run the application**

   ```bash
   # Windows
   scripts\run.bat

   # macOS/Linux
   chmod +x scripts/run.sh
   ./scripts/run.sh
   ```

### Alternative: Run from JAR

If the project is already built, you can run directly from the JAR file:

```bash
java -jar build/car-rental-management-system.jar
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
- ✅ Customer database management
- ✅ Rental transaction monitoring

### Staff User

- ✅ Process car rentals and returns
- ✅ Manage customer information
- ✅ Search and view car availability
- ✅ Generate basic reports
- ✅ Limited administrative access
- ✅ Rental workflow management

## 📚 Documentation

### Essential Documents

- **[User Manual](docs/deliverables/USER_MANUAL.md)** - Complete system usage guide
- **[Technical Architecture](docs/technical/architecture.md)** - System design documentation
- **[Database Schema](docs/technical/database-schema.md)** - Database design documentation
- **[Database Features](docs/technical/database-features.md)** - Advanced database implementation
- **[System Design Diagrams](docs/technical/system-design-diagrams.md)** - Mermaid diagrams
- **[Project Summary](docs/deliverables/FINAL_PROJECT_SUMMARY.md)** - Complete project overview
- **[Demo Script](docs/deliverables/DEMO_SCRIPT.md)** - Presentation guide

### Screenshots & Visual Documentation

- **[Screenshots Directory](screenshots/README.md)** - Application interface captures
- System architecture diagrams
- Database ERD diagrams
- User interface flowcharts
- Business process workflows

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
- **Modern UI**: Professional interface with gradients and shadows

### Build System

- **Automated Dependency Management**: Downloads required JARs automatically
- **Cross-platform Scripts**: Windows (.bat) and Unix (.sh) support
- **Clean Build Process**: Automated compilation and packaging
- **JAR Generation**: Creates executable JAR with all dependencies

## 📊 Project Status & Testing

**🎉 Project Status: COMPLETE & FULLY FUNCTIONAL**

### ✅ Completed Features

- [x] Complete CRUD operations for all entities
- [x] Role-based access control system
- [x] Professional user interface design
- [x] Comprehensive reporting system
- [x] Cross-platform compatibility
- [x] Automated build and deployment
- [x] Complete documentation suite
- [x] Database initialization and management
- [x] User authentication and authorization
- [x] Car rental workflow management

### 🧪 Testing Results

- **Database Initialization**: ✅ Successfully tested
- **Application Launch**: ✅ Successfully tested on macOS
- **Login System**: ✅ Working with default credentials
- **Build Process**: ✅ Automated build scripts verified
- **Cross-platform**: ✅ Windows, macOS, and Linux support

### 🔄 Recent Updates

- **Last Build**: September 2, 2025
- **Database**: SQLite 3.44.1 with latest JDBC driver
- **Dependencies**: Updated to latest stable versions
- **Documentation**: Comprehensive user manual and technical docs
- **Build System**: Optimized build scripts for all platforms

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
- **Grade**: Pending (Project Complete)

## 🚀 Quick Demo

To see the system in action:

1. **Build and run** the application using the scripts above
2. **Login** with admin credentials: `Toriq` / `toriq123`
3. **Explore** the modern dashboard interface
4. **Test** CRUD operations in each management section
5. **Generate** sample reports and analytics

## 🤝 Contributing

This is an academic project, but suggestions and improvements are welcome:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

This project is developed for academic purposes as part of the PRG2201E course at INTI International University.

---

**Note**: This project demonstrates proficiency in Java programming, database management, GUI development, and software architecture principles. The system is production-ready and can be deployed in real car rental businesses with minimal modifications.

**Last Updated**: September 2, 2025
**Version**: 1.0.0
**Status**: Complete & Tested ✅
