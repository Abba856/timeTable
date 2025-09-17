#!/bin/bash

# clean.sh - Clean the project build artifacts

echo "Cleaning project build artifacts..."

# Remove compiled classes
rm -rf bin

# Remove JAR file
rm -f timetable.jar

# Remove Maven target directory
rm -rf target

# Remove database file
rm -f timetable.db

echo "Clean complete!"