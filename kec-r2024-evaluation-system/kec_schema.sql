-- ============================================================================
-- Kongu Engineering College (Autonomous), Perundurai, Erode - 638060
-- Regulation 2024 (R2024) Student Academic & Continuous Evaluation System
-- Database Schema Script: kec_schema.sql
-- ============================================================================

-- 1. Students Table
CREATE TABLE IF NOT EXISTS students (
    roll_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    current_semester INT NOT NULL CHECK (current_semester BETWEEN 1 AND 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Courses Table
CREATE TABLE IF NOT EXISTS courses (
    course_code VARCHAR(20) PRIMARY KEY,
    course_title VARCHAR(150) NOT NULL,
    credits INT NOT NULL CHECK (credits > 0),
    course_type VARCHAR(20) NOT NULL CHECK (course_type IN ('THEORY', 'PRACTICAL', 'EMBEDDED')),
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Enrollments & Attendance Table
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(20) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 8),
    total_classes INT NOT NULL DEFAULT 0 CHECK (total_classes >= 0),
    attended_classes INT NOT NULL DEFAULT 0 CHECK (attended_classes >= 0),
    is_condoned BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE,
    CONSTRAINT uq_student_course_sem UNIQUE (roll_no, course_code, semester),
    CONSTRAINT chk_classes CHECK (attended_classes <= total_classes)
);

-- 4. Phased Assessment Marks Table
-- Phase 1: CIA Test 1 (0-100), CIA Test 2 (0-100)
-- Phase 2: Assignment / Quiz / Lab Evaluation (0-100)
-- Phase 3: End Semester Exam (ESE) Marks (0-100) or Absence (AB)
CREATE TABLE IF NOT EXISTS assessment_marks (
    mark_id INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id INT NOT NULL UNIQUE,
    cia1 DECIMAL(5,2) DEFAULT NULL CHECK (cia1 IS NULL OR (cia1 >= 0 AND cia1 <= 100)),
    cia2 DECIMAL(5,2) DEFAULT NULL CHECK (cia2 IS NULL OR (cia2 >= 0 AND cia2 <= 100)),
    assignment_quiz DECIMAL(5,2) DEFAULT NULL CHECK (assignment_quiz IS NULL OR (assignment_quiz >= 0 AND assignment_quiz <= 100)),
    ese_marks DECIMAL(5,2) DEFAULT NULL CHECK (ese_marks IS NULL OR (ese_marks >= 0 AND ese_marks <= 100)),
    is_absent BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_marks_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE
);

-- Sample Initial Data for Verification
-- Students
INSERT INTO students (roll_no, name, department, current_semester) VALUES
('24ITR001', 'Aravind Kumar S', 'Information Technology', 3),
('24ITR002', 'Bhavani Shankar M', 'Information Technology', 3),
('24ITR003', 'Chandru V', 'Information Technology', 3),
('24ITR004', 'Deepika R', 'Information Technology', 3),
('24CSR050', 'Gokulnath P', 'Computer Science & Engineering', 3);

-- Courses (R2024 Curriculum)
INSERT INTO courses (course_code, course_title, credits, course_type, semester) VALUES
('24ITT31', 'Data Structures and Algorithms', 3, 'THEORY', 3),
('24ITP31', 'Data Structures Laboratory', 2, 'PRACTICAL', 3),
('24ITE31', 'Object Oriented Programming with Java', 4, 'EMBEDDED', 3),
('24MAT31', 'Discrete Mathematics and Linear Algebra', 4, 'THEORY', 3);

-- Enrollments with Attendance Records
-- Aravind: >75% attendance across courses (Eligible)
INSERT INTO enrollments (roll_no, course_code, semester, total_classes, attended_classes, is_condoned) VALUES
('24ITR001', '24ITT31', 3, 50, 46, FALSE),
('24ITR001', '24ITP31', 3, 30, 28, FALSE),
('24ITR001', '24ITE31', 3, 60, 55, FALSE),
('24ITR001', '24MAT31', 3, 50, 45, FALSE);

-- Bhavani: Regular attendance (Eligible)
INSERT INTO enrollments (roll_no, course_code, semester, total_classes, attended_classes, is_condoned) VALUES
('24ITR002', '24ITT31', 3, 50, 48, FALSE),
('24ITR002', '24ITP31', 3, 30, 29, FALSE),
('24ITR002', '24ITE31', 3, 60, 58, FALSE);

-- Chandru: Attendance Shortage (<65%) in 24ITT31 (Defaulter: SA)
INSERT INTO enrollments (roll_no, course_code, semester, total_classes, attended_classes, is_condoned) VALUES
('24ITR003', '24ITT31', 3, 50, 28, FALSE),
('24ITR003', '24ITP31', 3, 30, 25, FALSE),
('24ITR003', '24ITE31', 3, 60, 41, TRUE);

-- Deepika: Absent in ESE for 24ITT31
INSERT INTO enrollments (roll_no, course_code, semester, total_classes, attended_classes, is_condoned) VALUES
('24ITR004', '24ITT31', 3, 50, 42, FALSE),
('24ITR004', '24ITE31', 3, 60, 52, FALSE);

-- Marks Records
-- Aravind: Outstanding marks
INSERT INTO assessment_marks (enrollment_id, cia1, cia2, assignment_quiz, ese_marks, is_absent) VALUES
(1, 92.0, 95.0, 96.0, 94.0, FALSE),
(2, 90.0, 92.0, 95.0, 92.0, FALSE),
(3, 88.0, 90.0, 94.0, 90.0, FALSE),
(4, 85.0, 88.0, 90.0, 86.0, FALSE);

-- Bhavani: High marks
INSERT INTO assessment_marks (enrollment_id, cia1, cia2, assignment_quiz, ese_marks, is_absent) VALUES
(5, 78.0, 82.0, 85.0, 80.0, FALSE),
(6, 85.0, 88.0, 90.0, 88.0, FALSE),
(7, 75.0, 80.0, 84.0, 78.0, FALSE);

-- Chandru: SA in 24ITT31, passed others
INSERT INTO assessment_marks (enrollment_id, cia1, cia2, assignment_quiz, ese_marks, is_absent) VALUES
(8, 45.0, 50.0, 60.0, NULL, FALSE),
(9, 70.0, 75.0, 80.0, 74.0, FALSE),
(10, 65.0, 68.0, 72.0, 66.0, FALSE);

-- Deepika: Absent in 24ITT31
INSERT INTO assessment_marks (enrollment_id, cia1, cia2, assignment_quiz, ese_marks, is_absent) VALUES
(11, 80.0, 85.0, 85.0, 0.0, TRUE),
(12, 72.0, 76.0, 80.0, 70.0, FALSE);

