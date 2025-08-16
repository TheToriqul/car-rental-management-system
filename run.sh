#!/bin/bash

# Car Rental Management System Run Script

echo "🚗 Starting Car Rental Management System..."
echo "=========================================="

# Check if build exists
if [ ! -d "build/classes" ]; then
    echo "❌ Build not found. Please run ./build.sh first."
    exit 1
fi

# Set classpath
CLASSPATH="build/lib/*:build/classes"

# Run the application
echo "🎯 Launching application..."
java -cp "$CLASSPATH" com.carrental.Main

echo "👋 Application closed."
