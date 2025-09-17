#!/bin/bash

# check-project.sh - Check the project structure and report any missing files

echo "Checking project structure..."

# List of expected files and directories
expected_paths=(
    "src/main/java/com/timetable/system/BaseEntity.java"
    "src/main/java/com/timetable/system/Course.java"
    "src/main/java/com/timetable/system/CourseDAO.java"
    "src/main/java/com/timetable/system/DAO.java"
    "src/main/java/com/timetable/system/DatabaseConnection.java"
    "src/main/java/com/timetable/system/DatabaseInitializer.java"
    "src/main/java/com/timetable/system/Lecturer.java"
    "src/main/java/com/timetable/system/LecturerDAO.java"
    "src/main/java/com/timetable/system/TimetableApplication.java"
    "src/main/java/com/timetable/system/TimetableEntry.java"
    "src/main/java/com/timetable/system/TimetableEntryDAO.java"
    "src/main/java/com/timetable/system/Timeslot.java"
    "src/main/java/com/timetable/system/TimeslotDAO.java"
    "src/main/java/com/timetable/system/Availability.java"
    "src/main/java/com/timetable/system/AvailabilityDAO.java"
    "src/main/java/com/timetable/system/Venue.java"
    "src/main/java/com/timetable/system/VenueDAO.java"
    "src/main/java/com/timetable/system/gui/MainWindow.java"
    "src/main/resources/schema.sql"
    "src/main/resources/database.properties"
    "src/test/java/com/timetable/system/LecturerTest.java"
    "src/test/java/com/timetable/system/LecturerDAOTest.java"
    "pom.xml"
    "README.md"
    "LICENSE"
    "BUILDING.md"
    "DEVELOPMENT_LOG.md"
    "build.sh"
    "run.sh"
    "test.sh"
    "clean.sh"
    "package.sh"
    "run-clean.sh"
    "init-sample-data.sh"
    "check-project.sh"
    ".gitignore"
)

missing_files=()

# Check each expected path
for path in "${expected_paths[@]}"; do
    if [ ! -f "$path" ] && [ ! -d "$path" ]; then
        missing_files+=("$path")
    fi
done

# Report results
if [ ${#missing_files[@]} -eq 0 ]; then
    echo "All expected files and directories are present!"
else
    echo "Missing files/directories:"
    for file in "${missing_files[@]}"; do
        echo "  - $file"
    done
fi