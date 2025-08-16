@echo off
REM Car Rental Management System Clean Script for Windows

echo 🧹 Cleaning Car Rental Management System...
echo =========================================

REM Remove build directory
if exist "build" (
    echo 📁 Removing build directory...
    rmdir /s /q build
    echo ✅ Build directory removed.
) else (
    echo ℹ️ Build directory does not exist.
)

REM Remove target directory (if exists)
if exist "target" (
    echo 📁 Removing target directory...
    rmdir /s /q target
    echo ✅ Target directory removed.
) else (
    echo ℹ️ Target directory does not exist.
)

echo 🎉 Clean completed successfully!
pause
