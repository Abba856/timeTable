#!/bin/bash

# verify-all.sh - Run all verification scripts

echo "=========================================="
echo "  Automated Timetable Generation System  "
echo "         Verification Suite              "
echo "=========================================="
echo

echo "1. Checking project structure..."
./check-project.sh
echo

echo "2. Building the project..."
./build.sh
echo

echo "3. Running unit tests..."
./test.sh
echo

echo "4. Checking code style..."
./checkstyle.sh
echo

echo "5. Formatting code..."
./format-code.sh
echo

echo "Verification suite complete!"
echo
echo "Run './run.sh' to start the application"