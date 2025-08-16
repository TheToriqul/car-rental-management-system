#!/bin/bash

# Car Rental Management System - Setup Script
# This script sets up the development environment

echo "🚀 Setting up Car Rental Management System..."

# Check if Java is installed
echo "🔍 Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    echo "💡 Please install Java 15 or higher"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo "✅ Java version: $JAVA_VERSION"

if [ "$JAVA_VERSION" -lt 15 ]; then
    echo "⚠️ Warning: Java version $JAVA_VERSION detected. Java 15+ is recommended."
fi

# Create necessary directories
echo "📁 Creating project directories..."
mkdir -p ../src/main/resources/database
mkdir -p ../build/lib
mkdir -p ../target
mkdir -p ../screenshots

# Set executable permissions for scripts
echo "🔧 Setting script permissions..."
chmod +x *.sh

# Download dependencies if not present
echo "📦 Checking dependencies..."
if [ ! -f "../build/lib/sqlite-jdbc-3.44.1.0.jar" ]; then
    echo "📥 Downloading SQLite JDBC driver..."
    curl -L -o ../build/lib/sqlite-jdbc-3.44.1.0.jar \
        "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar"
fi

if [ ! -f "../build/lib/commons-lang3-3.12.0.jar" ]; then
    echo "📥 Downloading Apache Commons Lang..."
    curl -L -o ../build/lib/commons-lang3-3.12.0.jar \
        "https://repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar"
fi

if [ ! -f "../build/lib/commons-codec-1.15.jar" ]; then
    echo "📥 Downloading Apache Commons Codec..."
    curl -L -o ../build/lib/commons-codec-1.15.jar \
        "https://repo1.maven.org/maven2/commons-codec/commons-codec/1.15/commons-codec-1.15.jar"
fi

echo "✅ Setup completed successfully!"
echo "💡 Next steps:"
echo "   1. Run './scripts/build.sh' to build the project"
echo "   2. Run './scripts/run.sh' to start the application"
echo "   3. Run './scripts/test.sh' to run tests"
