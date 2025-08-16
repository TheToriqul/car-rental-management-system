#!/bin/bash

# Car Rental Management System Clean Script

echo "🧹 Cleaning Car Rental Management System..."
echo "========================================="

# Remove build directory
if [ -d "build" ]; then
    echo "📁 Removing build directory..."
    rm -rf build
    echo "✅ Build directory removed."
else
    echo "ℹ️ Build directory does not exist."
fi

# Remove target directory (if exists)
if [ -d "target" ]; then
    echo "📁 Removing target directory..."
    rm -rf target
    echo "✅ Target directory removed."
else
    echo "ℹ️ Target directory does not exist."
fi

echo "🎉 Clean completed successfully!" 
