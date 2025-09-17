#!/bin/bash

# run.sh - Run script for the Automated Timetable Generation System

# Define the classpath to include the main JAR and all JARs in the lib directory
CLASSPATH="timetable.jar:lib/*"

# Run the application with the correct classpath
java -cp "$CLASSPATH" com.timetable.system.TimetableApplication