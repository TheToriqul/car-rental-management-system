#!/bin/bash

# Car Rental Management System Run Script

echo "🚗 Starting Car Rental Management System..."
echo "=========================================="

# Check if build exists
if [ ! -d "build/classes" ]; then
    echo "❌ Build not found. Please run ./build.sh first."
    exit 1
fi

# Set classpath with explicit JAR files
CLASSPATH="build/lib/sqlite-jdbc-3.44.1.0.jar:build/lib/commons-codec-1.15.jar:build/lib/commons-lang3-3.12.0.jar:build/lib/slf4j-api-1.7.36.jar:build/lib/slf4j-simple-1.7.36.jar:build/classes"

# Run the application
echo "🎯 Launching application..."
java -cp "$CLASSPATH" com.carrental.Main

echo "👋 Application closed."
