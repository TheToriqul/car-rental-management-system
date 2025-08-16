@echo off
REM Car Rental Management System Test Script for Windows

echo 🧪 Testing Car Rental Management System...
echo =======================================

REM Check if build exists
if not exist "build\classes" (
    echo ❌ Build not found. Please run build.bat first.
    pause
    exit /b 1
)

REM Set classpath
set CLASSPATH=build\lib\*;build\classes

echo 🔍 Running basic functionality tests...

REM Test 1: Check if main class can be loaded
echo 📋 Test 1: Main class loading...
java -cp "%CLASSPATH%" com.carrental.Main --test
if %ERRORLEVEL% EQU 0 (
    echo ✅ Main class loads successfully
) else (
    echo ❌ Main class loading failed
)

REM Test 2: Check database connection
echo 📋 Test 2: Database connection...
java -cp "%CLASSPATH%" com.carrental.util.DatabaseManager --test
if %ERRORLEVEL% EQU 0 (
    echo ✅ Database connection successful
) else (
    echo ❌ Database connection failed
)

REM Test 3: Check if JAR file exists
echo 📋 Test 3: JAR file creation...
if exist "build\car-rental-management-system.jar" (
    echo ✅ JAR file created successfully
) else (
    echo ❌ JAR file not found
)

echo.
echo 🎉 Testing completed!
echo.
echo To run the full application:
echo run.bat
echo.
pause
