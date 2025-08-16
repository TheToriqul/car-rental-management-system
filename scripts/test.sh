#!/bin/bash

# Car Rental Management System - Test Script
# This script runs tests for the application

echo "🧪 Running Car Rental Management System Tests..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    exit 1
fi

# Check if project is built
if [ ! -f "../build/car-rental-management-system.jar" ]; then
    echo "⚠️ Project not built. Building first..."
    ./build.sh
fi

echo "🔍 Running database connection test..."
java -cp "../build/car-rental-management-system.jar:../build/lib/*" com.carrental.util.DatabaseManager

if [ $? -eq 0 ]; then
    echo "✅ Database connection test passed"
else
    echo "❌ Database connection test failed"
    exit 1
fi

echo "🔍 Running basic functionality tests..."

# Test authentication service
echo "  - Testing authentication service..."
java -cp "../build/car-rental-management-system.jar:../build/lib/*" com.carrental.service.AuthenticationService

# Test DAO operations
echo "  - Testing DAO operations..."
java -cp "../build/car-rental-management-system.jar:../build/lib/*" com.carrental.dao.UserDAO

echo "✅ All tests completed!"
echo "💡 Run './scripts/run.sh' to start the application"
