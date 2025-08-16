@echo off
REM Car Rental Management System Setup Script for Windows

echo 🚀 Setting up Car Rental Management System...
echo ===========================================

REM Check Java installation
echo 🔍 Checking Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✅ Java is installed
    java -version
) else (
    echo ❌ Java is not installed or not in PATH
    echo Please install Java 15 or higher and add it to your PATH
    pause
    exit /b 1
)

REM Check Java version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
    set JAVA_VERSION=!JAVA_VERSION:"=!
)

echo 📋 Java version: %JAVA_VERSION%

REM Create necessary directories
echo 📁 Creating project directories...
if not exist "src\main\java\com\carrental" mkdir "src\main\java\com\carrental"
if not exist "src\main\java\com\carrental\model" mkdir "src\main\java\com\carrental\model"
if not exist "src\main\java\com\carrental\dao" mkdir "src\main\java\com\carrental\dao"
if not exist "src\main\java\com\carrental\service" mkdir "src\main\java\com\carrental\service"
if not exist "src\main\java\com\carrental\gui" mkdir "src\main\java\com\carrental\gui"
if not exist "src\main\java\com\carrental\util" mkdir "src\main\java\com\carrental\util"
if not exist "src\main\resources\database" mkdir "src\main\resources\database"
if not exist "build" mkdir "build"
if not exist "build\lib" mkdir "build\lib"
if not exist "screenshots" mkdir "screenshots"

echo ✅ Directories created successfully

REM Check if manifest.txt exists
if not exist "manifest.txt" (
    echo 📄 Creating manifest.txt...
    echo Main-Class: com.carrental.Main > manifest.txt
    echo ✅ manifest.txt created
) else (
    echo ℹ️ manifest.txt already exists
)

REM Check if assets directory exists
if not exist "assets" (
    echo 📁 Creating assets directory...
    mkdir "assets"
    echo ✅ Assets directory created
) else (
    echo ℹ️ Assets directory already exists
)

echo.
echo 🎉 Setup completed successfully!
echo.
echo Next steps:
echo 1. Run build.bat to compile the project
echo 2. Run run.bat to start the application
echo.
echo Default login credentials:
echo - Admin: Toriq / toriq123
echo - Staff: Jenesh / jenesh123
echo.
pause
