-- schema.sql
-- Database schema for Automated Timetable Generation System

-- Table: Lecturer
CREATE TABLE IF NOT EXISTS Lecturer (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    rank TEXT,
    CONSTRAINT chk_rank CHECK (rank IN ('Professor', 'Associate Professor', 'Assistant Professor', 'Lecturer'))
);

-- Table: Course
CREATE TABLE IF NOT EXISTS Course (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    level TEXT NOT NULL,
    lab_required BOOLEAN DEFAULT FALSE,
    CONSTRAINT chk_level CHECK (level IN ('ND', 'HND'))
);

-- Table: Venue
CREATE TABLE IF NOT EXISTS Venue (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    capacity INTEGER NOT NULL,
    type TEXT NOT NULL,
    CONSTRAINT chk_type CHECK (type IN ('Lecture Hall', 'Lab'))
);

-- Table: Timeslot
CREATE TABLE IF NOT EXISTS Timeslot (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    day_of_week TEXT NOT NULL,
    start_time TEXT NOT NULL,
    end_time TEXT NOT NULL,
    CONSTRAINT chk_day CHECK (day_of_week IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'))
);

-- Table: Availability
CREATE TABLE IF NOT EXISTS Availability (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lecturer_id INTEGER NOT NULL,
    timeslot_id INTEGER NOT NULL,
    FOREIGN KEY (lecturer_id) REFERENCES Lecturer(id) ON DELETE CASCADE,
    FOREIGN KEY (timeslot_id) REFERENCES Timeslot(id) ON DELETE CASCADE,
    UNIQUE(lecturer_id, timeslot_id)
);

-- Table: TimetableEntry
CREATE TABLE IF NOT EXISTS TimetableEntry (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    course_id INTEGER NOT NULL,
    lecturer_id INTEGER NOT NULL,
    venue_id INTEGER NOT NULL,
    timeslot_id INTEGER NOT NULL,
    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE,
    FOREIGN KEY (lecturer_id) REFERENCES Lecturer(id) ON DELETE CASCADE,
    FOREIGN KEY (venue_id) REFERENCES Venue(id) ON DELETE CASCADE,
    FOREIGN KEY (timeslot_id) REFERENCES Timeslot(id) ON DELETE CASCADE,
    UNIQUE(lecturer_id, timeslot_id),
    UNIQUE(venue_id, timeslot_id)
);