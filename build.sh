#!/bin/bash

# build.sh - Build script for the Automated Timetable Generation System

# Create directories if they don't exist
mkdir -p bin
mkdir -p lib

# Download dependencies if they don't exist
if [ ! -f lib/sqlite-jdbc-3.36.0.3.jar ]; then
    echo "Downloading SQLite JDBC driver..."
    wget -O lib/sqlite-jdbc-3.36.0.3.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar
fi

# Compile the source code
echo "Compiling source code..."
javac -d bin -cp "lib/*:src/main/resources" src/main/java/com/timetable/system/*.java src/main/java/com/timetable/system/gui/*.java

# Copy resources to the bin directory
echo "Copying resources..."
cp -r src/main/resources/* bin/

# Create a JAR file
echo "Creating JAR file..."
jar cfm timetable.jar manifest.txt -C bin .

echo "Build complete! Run the application with: java -jar timetable.jar"