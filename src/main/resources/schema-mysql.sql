-- schema-mysql.sql
-- Database schema for Automated Timetable Generation System (MySQL version)

-- Table: Lecturer
CREATE TABLE IF NOT EXISTS Lecturer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    rank VARCHAR(50),
    CONSTRAINT chk_rank CHECK (rank IN ('Professor', 'Associate Professor', 'Assistant Professor', 'Lecturer'))
);

-- Table: Course
CREATE TABLE IF NOT EXISTS Course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    level VARCHAR(10) NOT NULL,
    lab_required TINYINT(1) DEFAULT 0,
    CONSTRAINT chk_level CHECK (level IN ('ND', 'HND'))
);

-- Table: Venue
CREATE TABLE IF NOT EXISTS Venue (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    CONSTRAINT chk_type CHECK (type IN ('Lecture Hall', 'Lab'))
);

-- Table: Timeslot
CREATE TABLE IF NOT EXISTS Timeslot (
    id INT PRIMARY KEY AUTO_INCREMENT,
    day_of_week VARCHAR(10) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT chk_day CHECK (day_of_week IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'))
);

-- Table: Availability
CREATE TABLE IF NOT EXISTS Availability (
    id INT PRIMARY KEY AUTO_INCREMENT,
    lecturer_id INT NOT NULL,
    timeslot_id INT NOT NULL,
    FOREIGN KEY (lecturer_id) REFERENCES Lecturer(id) ON DELETE CASCADE,
    FOREIGN KEY (timeslot_id) REFERENCES Timeslot(id) ON DELETE CASCADE,
    UNIQUE(lecturer_id, timeslot_id)
);

-- Table: TimetableEntry
CREATE TABLE IF NOT EXISTS TimetableEntry (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    lecturer_id INT NOT NULL,
    venue_id INT NOT NULL,
    timeslot_id INT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE,
    FOREIGN KEY (lecturer_id) REFERENCES Lecturer(id) ON DELETE CASCADE,
    FOREIGN KEY (venue_id) REFERENCES Venue(id) ON DELETE CASCADE,
    FOREIGN KEY (timeslot_id) REFERENCES Timeslot(id) ON DELETE CASCADE,
    UNIQUE(lecturer_id, timeslot_id),
    UNIQUE(venue_id, timeslot_id)
);