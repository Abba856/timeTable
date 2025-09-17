# Build and Run Instructions

This document explains how to build and run the Automated Timetable Generation System.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- For the build.sh script: wget (to download dependencies)
- For packaging: zip (to create distribution archives)

## Building the Application

You can build the application in two ways:

### Option 1: Using Maven (Recommended)

1. Open a terminal/command prompt
2. Navigate to the project root directory
3. Run the following command to build the project:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

### Option 2: Using the build.sh script

1. Open a terminal/command prompt
2. Navigate to the project root directory
3. Run the build script:

```bash
./build.sh
```

This will:
- Download the required dependencies (SQLite JDBC driver)
- Compile the source code
- Create a JAR file named `timetable.jar`

## Running the Application

### Using Maven

After building the project with Maven, you can run the application using:

```bash
java -jar target/automated-timetable-system-1.0-SNAPSHOT.jar
```

Alternatively, you can run the application directly using Maven:

```bash
mvn exec:java
```

### Using the run.sh script

If you built the application using the build.sh script, you can run it with:

```bash
./run.sh
```

Or directly:

```bash
java -jar timetable.jar
```

## Running with a Clean Database

To run the application with a fresh database (deleting any existing data):

```bash
./run-clean.sh
```

## Running Tests

To run the unit tests:

```bash
./test.sh
```

Or with Maven:

```bash
mvn test
```

## Packaging for Distribution

To create a distributable package of the application:

```bash
./package.sh
```

This will create a zip file in the project root directory containing:
- The application JAR file
- Required dependencies
- Documentation files
- A run script

## Cleaning Build Artifacts

To remove all build artifacts and start fresh:

```bash
./clean.sh
```

Or with Maven:

```bash
mvn clean
```

## Development Scripts

The project includes several scripts to aid in development:

- `debug.sh` - Run the application in debug mode
- `format-code.sh` - Format Java code using Google Java Format
- `checkstyle.sh` - Check code style using Checkstyle
- `generate-javadoc.sh` - Generate JavaDoc documentation
- `check-project.sh` - Verify the project structure

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