# Building the Automated Timetable Generation System

This document provides instructions for building the Automated Timetable Generation System from source code.

## Prerequisites

Before building the application, ensure you have the following installed:

1. **Java Development Kit (JDK) 11 or higher**
   - The application has been tested with JDK 11
   - Download from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [OpenJDK](https://openjdk.java.net/)

2. **Build Tools**
   - The project includes shell scripts for building on Unix-like systems (Linux, macOS)
   - On Windows, you can use the instructions in [Windows Installation Guide](docs/install_Windows.md)

## Building with Shell Scripts (Linux/macOS)

The project includes several shell scripts to simplify the build process:

### 1. Using the build script (Recommended)

```bash
./build.sh
```

This script will:
- Create necessary directories
- Download dependencies if not present
- Compile the source code
- Copy resources
- Create the JAR file

### 2. Using Maven (Alternative)

If you have Maven installed:

```bash
mvn clean package
```

Note: The project was initially set up with Maven, but the shell scripts provide a simpler build process.

## Building Manually

If you prefer to build manually or are on a system without the shell scripts:

1. **Compile the source code**:
   ```bash
   mkdir -p bin
   javac -d bin -cp "lib/*:src/main/resources" src/main/java/com/timetable/system/*.java src/main/java/com/timetable/system/gui/*.java
   ```

2. **Copy resources**:
   ```bash
   cp -r src/main/resources/* bin/
   ```

3. **Create the JAR file**:
   ```bash
   jar cfm timetable.jar manifest.txt -C bin .
   ```

## Dependencies

The application requires the following JAR files in the `lib` directory:

1. **SQLite JDBC Driver** (`sqlite-jdbc-3.36.0.3.jar`)
   - Used for SQLite database connectivity
   - Automatically downloaded by the build script if missing

2. **MySQL Connector** (`mysql-connector-java-8.0.28.jar`)
   - Used for MySQL database connectivity
   - Automatically downloaded by the build script if missing

The build script will automatically download these dependencies if they are not present in the `lib` directory.

## Output

After a successful build, you will have:

1. **Compiled class files** in the `bin` directory
2. **A runnable JAR file** named `timetable.jar` in the project root directory

## Running the Application

After building, you can run the application using:

```bash
./run.sh
```

Or manually:

```bash
java -jar timetable.jar
```

On Windows, see [Windows Installation Guide](docs/install_Windows.md) for specific instructions.

## Troubleshooting

1. **"javac: command not found"**: 
   - Ensure JDK is installed and added to your PATH
   - On some systems, you may need to install the development package separately

2. **Permission denied**:
   - Make the scripts executable: `chmod +x *.sh`

3. **Dependency download failures**:
   - Check your internet connection
   - Verify the download URLs in the build script
   - Manually download the JAR files and place them in the `lib` directory

4. **Compilation errors**:
   - Ensure you're using JDK 11 or higher
   - Check that all source files are present