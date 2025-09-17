# Running the Automated Timetable Generation System on Windows

This guide provides detailed instructions for running the Automated Timetable Generation System on a Windows PC.

## Prerequisites

Before running the application, ensure you have the following installed on your Windows PC:

1. **Java Development Kit (JDK) 11 or higher**
   - Download from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [OpenJDK](https://openjdk.java.net/)
   - Installation includes the Java Runtime Environment (JRE) needed to run the application

2. **MySQL Server (Optional)**
   - Required only if you want to use MySQL instead of SQLite
   - Download from [MySQL's website](https://dev.mysql.com/downloads/mysql/)
   - During installation, note the root password you set

## Installation Steps

### 1. Download the Application

First, you need to get the application files on your Windows PC:

1. Clone or download the repository to your local machine
2. Extract the files to a folder (e.g., `C:\timetable`)

### 2. Install JDK and Set Environment Variables

1. Download and install JDK 11 or higher
2. Set the JAVA_HOME environment variable:
   - Press `Win + R`, type `sysdm.cpl`, and press Enter
   - Click on the "Advanced" tab
   - Click "Environment Variables"
   - Under "System Variables", click "New"
   - Set:
     - Variable name: `JAVA_HOME`
     - Variable value: Path to your JDK installation (e.g., `C:\Program Files\Java\jdk-11.0.12`)
   - Edit the PATH variable:
     - Select "Path" and click "Edit"
     - Click "New" and add `%JAVA_HOME%\bin`
   - Click "OK" to save all changes
   - Restart any open command prompts

### 3. Configure the Database

The application supports both SQLite (default) and MySQL:

#### For SQLite (Recommended for Windows - No additional setup required):
- The application will automatically create a `timetable.db` file in the project directory
- No additional configuration needed

#### For MySQL:
1. Install MySQL Server using the default settings
2. During installation, set a root password and note it down
3. After installation, open MySQL Command Line Client
4. Create a database named `timetable`:
   ```sql
   CREATE DATABASE timetable;
   ```
5. Update `src/main/resources/database.properties`:
   ```
   db.url=jdbc:mysql://localhost:3306/timetable
   db.user=root
   db.password=your_root_password
   ```

## Running the Application

Since Windows doesn't natively support the Unix shell scripts, you have several options:

### Option 1: Using Command Prompt or PowerShell (Recommended)

1. Open Command Prompt or PowerShell as Administrator
2. Navigate to the project directory:
   ```cmd
   cd C:\timetable
   ```

3. Compile the application:
   ```cmd
   mkdir bin
   javac -d bin -cp "lib/*;src/main/resources" src/main/java/com/timetable/system/*.java src/main/java/com/timetable/system/gui/*.java
   ```

4. Copy resources:
   ```cmd
   xcopy src\main\resources\* bin\ /E /I
   ```

5. Create the JAR file:
   ```cmd
   jar cfm timetable.jar manifest.txt -C bin .
   ```

6. Run the application:
   ```cmd
   java -cp "timetable.jar;lib/*" com.timimetable.system.TimetableApplication
   ```

### Option 2: Using an IDE (Recommended for Development)

1. Import the project into an IDE like IntelliJ IDEA, Eclipse, or NetBeans
2. Add the JAR files in the `lib` folder to your project's classpath:
   - `lib/sqlite-jdbc-3.36.0.3.jar`
   - `lib/mysql-connector-java-8.0.28.jar` (if using MySQL)
3. Set the project SDK to JDK 11 or higher
4. Run the `TimetableApplication.java` file directly from the IDE

### Option 3: Using Pre-built JAR (If Available)

If you have a pre-built `timetable.jar` file:

1. Ensure you have Java installed
2. Copy the `lib` folder and `timetable.jar` to the same directory
3. Run the application:
   ```cmd
   java -cp "timetable.jar;lib/*" com.timetable.system.TimetableApplication
   ```

## Creating a Windows Batch File (Optional)

To simplify running the application, you can create a batch file:

1. Create a file named `run.bat` in the project directory with the following content:
   ```batch
   @echo off
   java -cp "timetable.jar;lib/*" com.timetable.system.TimetableApplication
   pause
   ```

2. Double-click the `run.bat` file to run the application

## Troubleshooting

1. **"javac is not recognized"**: 
   - Ensure Java is installed correctly
   - Verify that JAVA_HOME is set and added to your PATH
   - Restart your command prompt after setting environment variables

2. **Database connection errors**:
   - For MySQL, verify the server is running and credentials are correct
   - For SQLite, ensure the application has write permissions to the directory

3. **ClassNotFoundException**:
   - Ensure all required JAR files are in the `lib` folder
   - Verify the classpath includes all necessary JAR files

4. **Port already in use (MySQL)**:
   - Make sure no other application is using port 3306
   - You can change the port in the database.properties file and MySQL configuration

## Application Features

Once running, the application provides:

- Lecturer Management (Add, Edit, Delete)
- Course Management (Add, Edit, Delete)
- Venue Management (Add, Edit, Delete)
- Timeslot Management (Add, Edit, Delete)
- Lecturer Availability Management
- Timetable Generation
- Timetable Viewing
- Export Functionality (Placeholder)

The application will display "Developed by Rukayya Bello Abubakar With Matric HND/SWD/23/00" at the bottom of the main window.

## Updating the Application

To update the application:

1. Download the latest version of the source code
2. Backup your database if you want to preserve data
3. Replace the source files
4. Rebuild the application using the steps above