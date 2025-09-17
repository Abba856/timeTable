#!/bin/bash

# run-clean.sh - Run the application with a clean database

echo "Running the application with a clean database..."

# Remove the existing database file
rm -f timetable.db

# Run the application
./run.sh