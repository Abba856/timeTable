#!/bin/bash

# debug.sh - Run the application in debug mode

echo "Running the application in debug mode..."

# Run the application with debug options
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar timetable.jar