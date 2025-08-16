#!/bin/bash

# Car Rental Management System Build Script
# This script compiles and runs the project without Maven

echo "🚗 Car Rental Management System - Build Script"
echo "=============================================="

# Create build directory
echo "📁 Creating build directory..."
mkdir -p build
mkdir -p build/lib

# Download dependencies (if not already present)
echo "📦 Checking dependencies..."
if [ ! -f "build/lib/sqlite-jdbc-3.44.1.0.jar" ]; then
    echo "Downloading SQLite JDBC driver..."
    curl -L -o build/lib/sqlite-jdbc-3.44.1.0.jar \
        "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar"
fi

if [ ! -f "build/lib/commons-codec-1.15.jar" ]; then
    echo "Downloading Apache Commons Codec..."
    curl -L -o build/lib/commons-codec-1.15.jar \
        "https://repo1.maven.org/maven2/commons-codec/commons-codec/1.15/commons-codec-1.15.jar"
fi

if [ ! -f "build/lib/commons-lang3-3.12.0.jar" ]; then
    echo "Downloading Apache Commons Lang3..."
    curl -L -o build/lib/commons-lang3-3.12.0.jar \
        "https://repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar"
fi

if [ ! -f "build/lib/slf4j-api-1.7.36.jar" ]; then
    echo "Downloading SLF4J API..."
    curl -L -o build/lib/slf4j-api-1.7.36.jar \
        "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar"
fi

if [ ! -f "build/lib/slf4j-simple-1.7.36.jar" ]; then
    echo "Downloading SLF4J Simple..."
    curl -L -o build/lib/slf4j-simple-1.7.36.jar \
        "https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar"
fi

# Build classpath
CLASSPATH="build/lib/*:build/classes"

# Compile Java files
echo "🔨 Compiling Java source files..."
javac -cp "$CLASSPATH" -d build/classes \
    src/main/java/com/carrental/Main.java \
    src/main/java/com/carrental/model/*.java \
    src/main/java/com/carrental/dao/*.java \
    src/main/java/com/carrental/service/*.java \
    src/main/java/com/carrental/gui/*.java \
    src/main/java/com/carrental/util/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    
    # Create executable JAR
    echo "📦 Creating executable JAR..."
    jar cfm build/car-rental-management-system.jar manifest.txt -C build/classes .
    
    echo "🎉 Build completed successfully!"
    echo ""
    echo "To run the application:"
    echo "java -jar build/car-rental-management-system.jar"
    echo ""
    echo "Or run directly:"
    echo "java -cp \"$CLASSPATH\" com.carrental.Main"
    
else
    echo "❌ Compilation failed!"
    exit 1
fi
