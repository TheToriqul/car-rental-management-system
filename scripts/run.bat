@echo off
REM Car Rental Management System Run Script for Windows

echo 🚗 Starting Car Rental Management System...
echo ==========================================

REM Check if build exists
if not exist "build\classes" (
    echo ❌ Build not found. Please run build.bat first.
    pause
    exit /b 1
)

REM Set classpath with explicit JAR files
set CLASSPATH=build\lib\sqlite-jdbc-3.44.1.0.jar;build\lib\commons-codec-1.15.jar;build\lib\commons-lang3-3.12.0.jar;build\lib\slf4j-api-1.7.36.jar;build\lib\slf4j-simple-1.7.36.jar;build\classes

REM Run the application
echo 🎯 Launching application...
java -cp "%CLASSPATH%" com.carrental.Main

echo 👋 Application closed.
pause
