# Automated Timetable Generation System

A standalone desktop application in Java to automate the generation of academic timetables.

## Overview

This system allows department administrators to manage courses, lecturers, venues, and timeslots, and to generate, view, and export conflict-free timetables. The application follows a structured System Development Life Cycle (SDLC) and an iterative development approach.

The application provides a complete solution for academic timetable management with a user-friendly graphical interface and robust data management capabilities.

## Technology Stack

- **Language:** Java
- **GUI:** Java Swing for a rich desktop experience
- **Data Persistence:** JDBC for database connectivity
- **Database:** Support for either SQLite (lightweight, embedded) or MySQL (for a multi-user, server-based option)

## Core Functionality

### 1. Data Management Modules

- **Lecturers Management**: Add, update, and delete lecturers with attributes like ID, name, and rank (Professor, Associate Professor, Assistant Professor, Lecturer).
- **Courses Management**: Add, update, and delete courses with attributes like ID, title, level (ND/HND), and lab requirements.
- **Venues Management**: Add, update, and delete venues with attributes like ID, name, capacity, and type (Lecture Hall or Lab).
- **Timeslots Management**: Add, update, and delete timeslots with day of week and time range.
- **Availability Management**: Define lecturer availability by linking lecturers to specific timeslots.

### 2. Timetable Generation

- **Constraint Checking**: The core algorithm prevents hard constraints from being violated:
  - Double-booking a lecturer at the same time
  - Double-booking a venue at the same time
  - Assigning a course to a venue that does not meet its capacity or type requirements
  - Assigning a lecturer to a timeslot they are not available for
- **Generation Algorithm**: Implementation of a greedy algorithm to generate timetables
- **Conflict Reporting**: The system identifies and reports any unavoidable conflicts with suggested resolutions

### 3. Viewing and Reporting

- **Timetable Display**: View generated timetables in a clear, tabular format organized by days and time slots
- **Export Functionality**: Export timetables to common formats (CSV export functionality is implemented as a placeholder)

## System Architecture & Design

### Architecture

The application follows a three-layer architecture:
1. **Presentation Layer**: Java Swing GUI providing an intuitive interface for non-technical users
2. **Application Logic Layer**: Java core classes containing the scheduling algorithm and business rules
3. **Database Layer**: JDBC layer for data access and persistence

### Data Model

The database schema is designed with the following key entities and their relationships:
- `Lecturer`: Represents teaching staff with their attributes
- `Course`: Represents academic courses with their requirements
- `Venue`: Represents classrooms and labs with capacity and type
- `Timeslot`: Represents time periods when classes can be scheduled
- `Availability`: Links lecturers to timeslots they are available
- `TimetableEntry`: Represents scheduled classes linking courses, lecturers, venues, and timeslots

### User Interface

The GUI features:
- Left-hand navigation menu for accessing different modules
- Main content area with forms for data entry
- Tabular views for displaying records with sortable and searchable tables
- Intuitive design for non-technical users

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/timetable/system/
│   │       ├── gui/
│   │       │   ├── MainWindow.java
│   │       │   ├── LecturerPanel.java
│   │       │   ├── CoursePanel.java
│   │       │   ├── VenuePanel.java
│   │       │   ├── TimeslotPanel.java
│   │       │   ├── AvailabilityPanel.java
│   │       │   └── TimetablePanel.java
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
│       ├── schema.sql
│       └── schema-mysql.sql
└── test/
    └── java/
```

## Building and Running the Application

See [BUILDING.md](BUILDING.md) for detailed instructions on how to build and run the application.

### Quick Start

1. **Build the application**:
   ```bash
   ./build.sh
   ```

2. **Run the application**:
   ```bash
   ./run.sh
   ```

### Running on Windows

For detailed instructions on running the application on Windows, see [Windows Installation Guide](docs/install_Windows.md).

## Database Configuration

The application supports both SQLite and MySQL databases.

### SQLite Configuration (Default)

SQLite is the default database for simplicity and portability:
1. The application automatically creates a `timetable.db` file in the project directory
2. No additional setup is required

### MySQL Configuration

To use MySQL instead of SQLite:

1. Open `src/main/resources/database.properties`
2. Comment the SQLite lines and uncomment/modify these lines:
   ```
   db.url=jdbc:mysql://localhost:3306/timetable
   db.user=your_username
   db.password=your_password
   ```
3. Make sure you have a MySQL server running and a database named `timetable` created

## Features Implemented

All core modules have been implemented with full CRUD (Create, Read, Update, Delete) operations:

- [x] Lecturer Management
- [x] Course Management
- [x] Venue Management
- [x] Timeslot Management
- [x] Lecturer Availability Management
- [x] Timetable Generation
- [x] Timetable Viewing
- [x] Export Functionality (Placeholder)

## Contributing

This project is under active development. Contributions are welcome!

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.