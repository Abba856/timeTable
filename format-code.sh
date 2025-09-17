#!/bin/bash

# format-code.sh - Format Java code using Google Java Format

echo "Formatting Java code..."

# Check if google-java-format is installed
if ! command -v google-java-format &> /dev/null
then
    echo "google-java-format could not be found. Please install it first."
    echo "You can download it from: https://github.com/google/google-java-format"
    exit 1
fi

# Format all Java files in the source directory
find src -name "*.java" -exec google-java-format -i {} \;

echo "Code formatting complete!"