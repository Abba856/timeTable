# Automated Timetable Generation System

A standalone desktop application in Java to automate the generation of academic timetables.

## Overview

This system allows department administrators to manage courses, lecturers, and venues, and to generate, view, and export conflict-free timetables. The application follows a structured System Development Life Cycle (SDLC) and an iterative development approach.

## Technology Stack

- **Language:** Java
- **GUI:** Java Swing for a rich desktop experience
- **Data Persistence:** JDBC for database connectivity
- **Database:** Support for either SQLite (lightweight, embedded) or MySQL (for a multi-user, server-based option)

## Core Functionality

1. **Data Management:**
   - Manage Courses: Add, update, and delete courses with attributes like ID, title, level (e.g., ND/HND), and lab requirements.
   - Manage Lecturers: Add, update, and delete lecturers with attributes like ID, name, rank, and specified availability.
   - Manage Venues: Add, update, and delete venues with attributes like ID, name, capacity, and type (e.g., lecture hall, lab).

2. **Timetable Generation:**
   - Constraint Checking: The core algorithm prevents hard constraints from being violated:
     - Double-booking a lecturer at the same time
     - Double-booking a venue at the same time
     - Assigning a course to a venue that does not meet its capacity or type
     - Assigning a lecturer to a timeslot they are not available for
   - Generation Algorithm: Implement a heuristic or greedy algorithm to generate the timetable
   - Conflict Reporting: The system identifies and reports any unavoidable conflicts with suggested resolutions

3. **Viewing and Reporting:**
   - View Timetable: Display generated timetables in a clear, tabular format
   - Filtering: Allow users to filter the timetable by various criteria (e.g., by level, by lecturer)
   - Export: Provide options to export the timetable in common formats like CSV or PDF

## System Architecture & Design

- **Architecture:** Three-layer architecture:
  1. Presentation Layer: The Java Swing GUI
  2. Application Logic Layer: Java core classes containing the scheduling algorithm and business rules
  3. Database Layer: The JDBC layer for data access and persistence

- **Data Model:** The database schema is designed with the following key entities and their relationships: `Lecturer`, `Course`, `Venue`, `Timeslot`, `TimetableEntry`, and `Availability`.

- **User Interface:** The GUI is intuitive for non-technical users, featuring a left-hand navigation menu for different modules, with the main content area using sortable and searchable tables for data entry and display.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/timetable/system/
│   │       ├── gui/
│   │       │   └── MainWindow.java
│   │       ├── BaseEntity.java
│   │       ├── Course.java
│   │       ├── CourseDAO.java
│   │       ├── DAO.java
│   │       ├── DatabaseConnection.java
│   │       ├── DatabaseInitializer.java
│   │       ├── Lecturer.java
│   │       ├── LecturerDAO.java
│   │       ├── TimetableApplication.java
│   │       ├── TimetableEntry.java
│   │       ├── TimetableEntryDAO.java
│   │       ├── Timeslot.java
│   │       ├── TimeslotDAO.java
│   │       ├── Availability.java
│   │       ├── AvailabilityDAO.java
│   │       ├── Venue.java
│   │       └── VenueDAO.java
│   └── resources/
│       ├── database.properties
│       └── schema.sql
└── test/
    └── java/
```

## Building and Running the Application

See [BUILDING.md](BUILDING.md) for detailed instructions on how to build and run the application.

## Database Configuration

The application supports both SQLite and MySQL databases. By default, it uses SQLite for simplicity.

To configure the database:

1. Open `src/main/resources/database.properties`
2. For SQLite (default), ensure these lines are uncommented:
   ```
   db.url=jdbc:sqlite:timetable.db
   db.user=
   db.password=
   ```
3. For MySQL, comment the SQLite lines and uncomment/modify these lines:
   ```
   # db.url=jdbc:mysql://localhost:3306/timetable
   # db.user=root
   # db.password=password
   ```

## Contributing

This project is under active development. Contributions are welcome!

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.