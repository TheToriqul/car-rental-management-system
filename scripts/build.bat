@echo off
REM Car Rental Management System Build Script for Windows
REM This script compiles and runs the project without Maven

echo 🚗 Car Rental Management System - Build Script
echo ==============================================

REM Create build directory
echo 📁 Creating build directory...
if not exist "build" mkdir build
if not exist "build\lib" mkdir build\lib

REM Download dependencies (if not already present)
echo 📦 Checking dependencies...
if not exist "build\lib\sqlite-jdbc-3.44.1.0.jar" (
    echo Downloading SQLite JDBC driver...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar' -OutFile 'build\lib\sqlite-jdbc-3.44.1.0.jar'"
)

if not exist "build\lib\commons-codec-1.15.jar" (
    echo Downloading Apache Commons Codec...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/commons-codec/commons-codec/1.15/commons-codec-1.15.jar' -OutFile 'build\lib\commons-codec-1.15.jar'"
)

if not exist "build\lib\commons-lang3-3.12.0.jar" (
    echo Downloading Apache Commons Lang3...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar' -OutFile 'build\lib\commons-lang3-3.12.0.jar'"
)

if not exist "build\lib\slf4j-api-1.7.36.jar" (
    echo Downloading SLF4J API...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'build\lib\slf4j-api-1.7.36.jar'"
)

if not exist "build\lib\slf4j-simple-1.7.36.jar" (
    echo Downloading SLF4J Simple...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'build\lib\slf4j-simple-1.7.36.jar'"
)

REM Build classpath
set CLASSPATH=build\lib\*;build\classes

REM Compile Java files
echo 🔨 Compiling Java source files...
javac -cp "%CLASSPATH%" -d build\classes ^
    src\main\java\com\carrental\Main.java ^
    src\main\java\com\carrental\model\*.java ^
    src\main\java\com\carrental\dao\*.java ^
    src\main\java\com\carrental\service\*.java ^
    src\main\java\com\carrental\gui\*.java ^
    src\main\java\com\carrental\util\*.java

if %ERRORLEVEL% EQU 0 (
    echo ✅ Compilation successful!
    
    REM Create executable JAR
    echo 📦 Creating executable JAR...
    jar cfm build\car-rental-management-system.jar manifest.txt -C build\classes .
    
    echo 🎉 Build completed successfully!
    echo.
    echo To run the application:
    echo java -jar build\car-rental-management-system.jar
    echo.
    echo Or run directly:
    echo java -cp "%CLASSPATH%" com.carrental.Main
    
) else (
    echo ❌ Compilation failed!
    exit /b 1
)
