#!/bin/bash

# init-developer.sh - Initialize a new developer's environment

echo "=========================================="
echo "  Automated Timetable Generation System  "
echo "       Developer Environment Setup        "
echo "=========================================="
echo

echo "1. Initializing project structure..."
./init-project.sh
echo

echo "2. Checking project structure..."
./check-project.sh
echo

echo "3. Building the project..."
./build.sh
echo

echo "4. Initializing database with sample data..."
./init-sample-data.sh
echo

echo "5. Running unit tests..."
./test.sh
echo

echo "Developer environment setup complete!"
echo
echo "Run './run.sh' to start the application"
echo "Run './project-status.sh' to check the project status"