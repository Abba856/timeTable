#!/bin/bash

# checkstyle.sh - Check Java code style using Checkstyle

echo "Checking code style with Checkstyle..."

# Check if checkstyle is installed
if ! command -v checkstyle &> /dev/null
then
    echo "Checkstyle could not be found. Please install it first."
    echo "You can download it from: https://checkstyle.sourceforge.io/"
    exit 1
fi

# Run Checkstyle on all Java files
checkstyle -c /google_checks.xml src/main/java/**/*.java

echo "Checkstyle analysis complete!"