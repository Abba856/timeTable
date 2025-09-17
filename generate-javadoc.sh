#!/bin/bash

# generate-javadoc.sh - Generate JavaDoc documentation for the project

echo "Generating JavaDoc documentation..."

# Create directory for JavaDoc
mkdir -p docs/javadoc

# Generate JavaDoc
javadoc -d docs/javadoc \
        -sourcepath src/main/java \
        -subpackages com.timetable.system \
        -encoding UTF-8 \
        -charset UTF-8 \
        -docencoding UTF-8 \
        -author \
        -version \
        -windowtitle "Automated Timetable Generation System" \
        -doctitle "<h1>Automated Timetable Generation System</h1>" \
        -header "<b>Automated Timetable Generation System</b>" \
        -bottom "<i>Copyright &#169; 2025 Automated Timetable Generation System. All rights reserved.</i>"

echo "JavaDoc documentation generated in docs/javadoc/"