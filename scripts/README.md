# 🔧 Car Rental Management System - Build Scripts

This directory contains build and deployment scripts for the Car Rental Management System.

## 📁 Available Scripts

### 🐧 Linux/macOS (Shell)

- **`build.sh`** - Compile project and download dependencies
- **`run.sh`** - Launch the application
- **`clean.sh`** - Remove build artifacts
- **`test.sh`** - Run basic tests
- **`setup.sh`** - Initialize environment

### 🪟 Windows (Batch)

- **`build.bat`** - Compile project and download dependencies
- **`run.bat`** - Launch the application
- **`clean.bat`** - Remove build artifacts
- **`test.bat`** - Run basic tests
- **`setup.bat`** - Initialize environment

## 🚀 Quick Start

### Windows

```cmd
setup.bat
build.bat
run.bat
```

### Linux/macOS

```bash
./setup.sh
./build.sh
./run.sh
```

## 📋 Script Details

### Build Scripts (`build.sh` / `build.bat`)

- Downloads dependencies (SQLite JDBC, Apache Commons, SLF4J)
- Compiles Java source files
- Creates executable JAR
- Handles compilation errors

### Run Scripts (`run.sh` / `run.bat`)

- Launches the application
- Sets proper classpath
- Verifies build exists

### Clean Scripts (`clean.sh` / `clean.bat`)

- Removes build directory
- Removes target directory
- Cleans workspace

### Test Scripts (`test.sh` / `test.bat`)

- Verifies build
- Tests main class loading
- Tests database connection
- Validates JAR creation

### Setup Scripts (`setup.sh` / `setup.bat`)

- Checks Java installation
- Creates project directories
- Sets up environment

## 🔧 Dependencies

| Dependency           | Version  | Purpose                |
| -------------------- | -------- | ---------------------- |
| SQLite JDBC          | 3.44.1.0 | Database connectivity  |
| Apache Commons Codec | 1.15     | Password hashing       |
| Apache Commons Lang3 | 3.12.0   | String utilities       |
| SLF4J API            | 1.7.36   | Logging API            |
| SLF4J Simple         | 1.7.36   | Logging implementation |

## 🐛 Troubleshooting

### Common Issues

- **Java not found**: Install Java 15+ and add to PATH
- **Build fails**: Check Java version and source files
- **Dependencies missing**: Run build script again
- **Permission denied** (Linux/macOS): `chmod +x *.sh`

### Windows-Specific

- **PowerShell execution policy**: Run as Administrator and set execution policy
- **Path issues**: Use backslashes and ensure Java in PATH

## 📊 Platform Differences

| Feature                 | Linux/macOS | Windows                        |
| ----------------------- | ----------- | ------------------------------ |
| **Shell**               | Bash/Zsh    | Command Prompt                 |
| **Extension**           | `.sh`       | `.bat`                         |
| **Path Separator**      | `:`         | `;`                            |
| **Directory Separator** | `/`         | `\`                            |
| **Download Method**     | `curl`      | `PowerShell Invoke-WebRequest` |

---

**Note**: All scripts provide the same functionality across platforms. Choose the appropriate extension for your OS.
