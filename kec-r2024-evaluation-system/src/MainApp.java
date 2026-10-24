import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Kongu Engineering College (Autonomous), Perundurai, Erode - 638060
 * Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * MainApp: Console Menu-Driven Interface for Data Entry and Report Generation
 */
public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final AcademicDAO academicDAO = new AcademicDAO();

    // ANSI Colors for readable console output
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        // Automatically initialize DB schema and verify connection
        printBanner();
        DBConnection.initializeDatabase();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntChoice("Enter your choice (1-8): ", 1, 8);
            System.out.println();

            switch (choice) {
                case 1:
                    handleStudentManagement();
                    break;
                case 2:
                    handleCourseManagement();
                    break;
                case 3:
                    handleEnrollmentAndAttendance();
                    break;
                case 4:
                    handleIncrementalMarksEntry();
                    break;
                case 5:
                    handleReportsAndAnalytics();
                    break;
                case 6:
                    displayEvaluationRulesGuide();
                    break;
                case 7:
                    handleDatabaseConfiguration();
                    break;
                case 8:
                    System.out.println(GREEN + "Thank you for using Kongu Engineering College Academic System (R2024)." + RESET);
                    DBConnection.closeConnection();
                    running = false;
                    break;
            }
        }
    }

    private static void printBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("=========================================================================================");
        System.out.println("               KONGU ENGINEERING COLLEGE (AUTONOMOUS) - PERUNDURAI, ERODE                ");
        System.out.println("                    REGULATION 2024 (R2024) - ACADEMIC EVALUATION SYSTEM                 ");
        System.out.println("                           Java JDBC & SQL Evaluation Engine                             ");
        System.out.println("=========================================================================================" + RESET);
    }

    private static void printMainMenu() {
        System.out.println("\n" + BLUE + BOLD + "---------------------------------[ MAIN MENU ]---------------------------------" + RESET);
        System.out.println("  1. Student Registration & Directory");
        System.out.println("  2. Course Catalog Management");
        System.out.println("  3. Course Enrollment & Attendance Tracking");
        System.out.println("  4. Phased Incremental Marks Entry (Phase 1, 2 & 3)");
        System.out.println("  5. Academic Performance Reports (Mark Sheet, Defaulters, Class Summary)");
        System.out.println("  6. KEC R2024 Regulations & Evaluation Rules Reference");
        System.out.println("  7. Database Connection Status & Utilities");
        System.out.println("  8. Exit System");
        System.out.println(BLUE + "-------------------------------------------------------------------------------" + RESET);
    }

    // =========================================================================
    // 1. STUDENT MANAGEMENT
    // =========================================================================

    private static void handleStudentManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + CYAN + BOLD + "--- [ 1. Student Registration & Directory ] ---" + RESET);
            System.out.println("  1. Register New Student");
            System.out.println("  2. Search Student by Roll Number");
            System.out.println("  3. View All Registered Students");
            System.out.println("  4. Return to Main Menu");
            int choice = readIntChoice("Select option (1-4): ", 1, 4);

            switch (choice) {
                case 1:
                    registerNewStudent();
                    break;
                case 2:
                    searchStudentByRollNo();
                    break;
                case 3:
                    viewAllStudents();
                    break;
                case 4:
                    back = true;
                    break;
            }
        }
    }

    private static void registerNewStudent() {
        System.out.println("\n" + BOLD + ">>> Register New Student <<<" + RESET);
        System.out.print("Enter Roll Number (e.g., 24ITR005, 24CSR051): ");
        String rollNo = scanner.nextLine().trim().toUpperCase();

        try {
            if (studentDAO.getStudentByRollNo(rollNo) != null) {
                System.out.println(RED + "[!] Student with Roll Number '" + rollNo + "' already exists!" + RESET);
                return;
            }
            System.out.print("Enter Student Full Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Department (e.g., Information Technology, CSE, ECE, MECH): ");
            String dept = scanner.nextLine().trim();

            int sem = readIntChoice("Enter Current Semester (1-8): ", 1, 8);

            Student student = new Student(rollNo, name, dept, sem);
            boolean success = studentDAO.addStudent(student);
            if (success) {
                System.out.println(GREEN + "[+] Student registered successfully: " + student + RESET);
            } else {
                System.out.println(RED + "[!] Failed to register student." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void searchStudentByRollNo() {
        System.out.print("Enter Roll Number to search: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();
        try {
            Student s = studentDAO.getStudentByRollNo(rollNo);
            if (s != null) {
                System.out.println(GREEN + "\nStudent Record Found:" + RESET);
                System.out.println("  Roll Number : " + s.getRollNo());
                System.out.println("  Full Name   : " + s.getName());
                System.out.println("  Department  : " + s.getDepartment());
                System.out.println("  Semester    : " + s.getCurrentSemester());
            } else {
                System.out.println(YELLOW + "[!] No student found with Roll Number: " + rollNo + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void viewAllStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            System.out.println("\n" + BOLD + "=========================== REGISTERED STUDENTS (" + students.size() + ") ===========================" + RESET);
            System.out.printf("%-12s | %-25s | %-30s | %-8s%n", "ROLL NO", "NAME", "DEPARTMENT", "SEMESTER");
            System.out.println("---------------------------------------------------------------------------------");
            for (Student s : students) {
                System.out.printf("%-12s | %-25s | %-30s | Sem %-4d%n",
                        s.getRollNo(), s.getName(), s.getDepartment(), s.getCurrentSemester());
            }
            System.out.println("---------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    // =========================================================================
    // 2. COURSE MANAGEMENT
    // =========================================================================

    private static void handleCourseManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + CYAN + BOLD + "--- [ 2. Course Catalog Management ] ---" + RESET);
            System.out.println("  1. Add New Course");
            System.out.println("  2. View All Courses");
            System.out.println("  3. Filter Courses by Semester");
            System.out.println("  4. Return to Main Menu");
            int choice = readIntChoice("Select option (1-4): ", 1, 4);

            switch (choice) {
                case 1:
                    addNewCourse();
                    break;
                case 2:
                    viewAllCourses();
                    break;
                case 3:
                    viewCoursesBySemester();
                    break;
                case 4:
                    back = true;
                    break;
            }
        }
    }

    private static void addNewCourse() {
        System.out.println("\n" + BOLD + ">>> Add New Course to Catalog <<<" + RESET);
        System.out.print("Enter Course Code (e.g., 24ITT31, 24ITP31, 24ITE31): ");
        String code = scanner.nextLine().trim().toUpperCase();

        try {
            if (studentDAO.getCourseByCode(code) != null) {
                System.out.println(RED + "[!] Course with code '" + code + "' already exists!" + RESET);
                return;
            }
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine().trim();

            int credits = readIntChoice("Enter Course Credits (1-6): ", 1, 6);

            System.out.println("Select Course Type:");
            System.out.println("  1. THEORY    (CIA: 40% | ESE: 60% | ESE Min: 27/60)");
            System.out.println("  2. PRACTICAL (CIA: 60% | ESE: 40% | ESE Min: 20/40)");
            System.out.println("  3. EMBEDDED  (CIA: 50% | ESE: 50% | ESE Min: 22.5/50)");
            int typeChoice = readIntChoice("Enter course type (1-3): ", 1, 3);
            String courseType = (typeChoice == 1) ? "THEORY" : (typeChoice == 2) ? "PRACTICAL" : "EMBEDDED";

            int sem = readIntChoice("Enter Semester Offered (1-8): ", 1, 8);

            Course course = new Course(code, title, credits, courseType, sem);
            boolean success = studentDAO.addCourse(course);
            if (success) {
                System.out.println(GREEN + "[+] Course successfully added to catalog: " + course + RESET);
            } else {
                System.out.println(RED + "[!] Failed to add course." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void viewAllCourses() {
        try {
            List<Course> courses = studentDAO.getAllCourses();
            printCourseTable(courses);
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void viewCoursesBySemester() {
        int sem = readIntChoice("Enter Semester to view (1-8): ", 1, 8);
        try {
            List<Course> courses = studentDAO.getCoursesBySemester(sem);
            System.out.println("\nCourses for Semester " + sem + ":");
            printCourseTable(courses);
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void printCourseTable(List<Course> courses) {
        System.out.println("\n" + BOLD + "=============================== KEC COURSE CATALOG (" + courses.size() + ") ===============================" + RESET);
        System.out.printf("%-10s | %-40s | %-7s | %-12s | %-5s%n", "CODE", "TITLE", "CREDITS", "TYPE", "SEM");
        System.out.println("----------------------------------------------------------------------------------------");
        for (Course c : courses) {
            System.out.printf("%-10s | %-40s | %-7d | %-12s | Sem %-3d%n",
                    c.getCourseCode(), c.getCourseTitle(), c.getCredits(), c.getCourseType(), c.getSemester());
        }
        System.out.println("----------------------------------------------------------------------------------------");
    }

    // =========================================================================
    // 3. ENROLLMENT & ATTENDANCE TRACKING
    // =========================================================================

    private static void handleEnrollmentAndAttendance() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + CYAN + BOLD + "--- [ 3. Course Enrollment & Attendance Tracking ] ---" + RESET);
            System.out.println("  1. Enroll Student in Course");
            System.out.println("  2. Record / Update Attendance for Enrolled Course");
            System.out.println("  3. View Student Course Enrollments");
            System.out.println("  4. View All Course Enrollments with Attendance");
            System.out.println("  5. Return to Main Menu");
            int choice = readIntChoice("Select option (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    enrollStudentInCourse();
                    break;
                case 2:
                    updateStudentAttendance();
                    break;
                case 3:
                    viewStudentEnrollments();
                    break;
                case 4:
                    viewAllEnrollments();
                    break;
                case 5:
                    back = true;
                    break;
            }
        }
    }

    private static void enrollStudentInCourse() {
        System.out.println("\n" + BOLD + ">>> Enroll Student in Course <<<" + RESET);
        System.out.print("Enter Student Roll Number: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();

        try {
            Student s = studentDAO.getStudentByRollNo(rollNo);
            if (s == null) {
                System.out.println(RED + "[!] Student '" + rollNo + "' not found. Register the student first." + RESET);
                return;
            }

            System.out.print("Enter Course Code: ");
            String courseCode = scanner.nextLine().trim().toUpperCase();
            Course c = studentDAO.getCourseByCode(courseCode);
            if (c == null) {
                System.out.println(RED + "[!] Course '" + courseCode + "' not found in catalog." + RESET);
                return;
            }

            int sem = readIntChoice("Enter Semester (1-8): ", 1, 8);

            Enrollment existing = academicDAO.getEnrollment(rollNo, courseCode, sem);
            if (existing != null) {
                System.out.println(YELLOW + "[!] Student is already enrolled in this course for semester " + sem + "!" + RESET);
                return;
            }

            int totalClasses = readIntChoice("Enter Total Classes Held to Date (0-150): ", 0, 150);
            int attendedClasses = readIntChoice("Enter Classes Attended (0-" + totalClasses + "): ", 0, totalClasses);

            boolean isCondoned = false;
            double attPct = (totalClasses > 0) ? ((attendedClasses * 100.0) / totalClasses) : 100.0;
            if (attPct >= 65.0 && attPct < 75.0) {
                System.out.print("Attendance is " + String.format("%.1f", attPct) + "% (65-74.9%). Has Medical Condonation been approved? (Y/N): ");
                String resp = scanner.nextLine().trim();
                isCondoned = resp.equalsIgnoreCase("Y");
            }

            int enrollmentId = academicDAO.enrollStudent(rollNo, courseCode, sem, totalClasses, attendedClasses, isCondoned);
            if (enrollmentId > 0) {
                System.out.println(GREEN + "[+] Enrollment successful! Enrollment ID: " + enrollmentId + RESET);
                System.out.printf("    Attendance: %d/%d (%.1f%%) | Eligibility: %s%n",
                        attendedClasses, totalClasses, attPct,
                        (attPct >= 75.0 ? "Eligible" : isCondoned ? "Condoned (Eligible)" : "Shortage (SA)"));
            } else {
                System.out.println(RED + "[!] Enrollment failed." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void updateStudentAttendance() {
        System.out.println("\n" + BOLD + ">>> Update Course Attendance <<<" + RESET);
        System.out.print("Enter Student Roll Number: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        int sem = readIntChoice("Enter Semester: ", 1, 8);

        try {
            Enrollment en = academicDAO.getEnrollment(rollNo, courseCode, sem);
            if (en == null) {
                System.out.println(RED + "[!] No enrollment found for " + rollNo + " in " + courseCode + " (Sem " + sem + ")." + RESET);
                return;
            }

            System.out.printf("Current Record: Total Classes = %d, Attended = %d (%.1f%%)%n",
                    en.getTotalClasses(), en.getAttendedClasses(), en.getAttendancePercentage());

            int total = readIntChoice("Enter New Total Classes Held: ", 1, 200);
            int attended = readIntChoice("Enter New Classes Attended: ", 0, total);

            double newPct = (attended * 100.0) / total;
            boolean isCondoned = en.isCondoned();
            if (newPct >= 65.0 && newPct < 75.0) {
                System.out.print("Attendance is " + String.format("%.1f", newPct) + "%. Approve Medical Condonation? (Y/N): ");
                String resp = scanner.nextLine().trim();
                isCondoned = resp.equalsIgnoreCase("Y");
            } else if (newPct < 65.0) {
                isCondoned = false;
                System.out.println(RED + "[!] Shortage of Attendance: " + String.format("%.1f", newPct) + "% (<65%). Student is BARRED from ESE (Grade: SA)." + RESET);
            }

            boolean updated = academicDAO.updateAttendance(en.getEnrollmentId(), total, attended, isCondoned);
            if (updated) {
                System.out.println(GREEN + "[+] Attendance updated successfully!" + RESET);
            } else {
                System.out.println(RED + "[!] Failed to update attendance." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void viewStudentEnrollments() {
        System.out.print("Enter Student Roll Number: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();
        int sem = readIntChoice("Enter Semester (1-8): ", 1, 8);

        try {
            List<Enrollment> list = academicDAO.getEnrollmentsByStudent(rollNo, sem);
            if (list.isEmpty()) {
                System.out.println(YELLOW + "[!] No enrollments found for " + rollNo + " in Semester " + sem + "." + RESET);
                return;
            }

            System.out.println("\n" + BOLD + "Enrollments for " + rollNo + " (Semester " + sem + "):" + RESET);
            printEnrollmentList(list);
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void viewAllEnrollments() {
        int sem = readIntChoice("Enter Semester (or 0 for All Semesters): ", 0, 8);
        try {
            List<Enrollment> list = academicDAO.getAllEnrollments(sem == 0 ? null : sem);
            printEnrollmentList(list);
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void printEnrollmentList(List<Enrollment> list) {
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-10s | %-9s | %-28s | %-10s | %-12s | %-6s | %-16s%n",
                "ID", "ROLL NO", "COURSE", "TITLE", "TYPE", "ATTENDANCE", "PCT", "ELIGIBILITY");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        for (Enrollment e : list) {
            System.out.printf("%-4d | %-10s | %-9s | %-28s | %-10s | %3d / %-3d    | %5.1f%% | %-16s%n",
                    e.getEnrollmentId(), e.getRollNo(), e.getCourseCode(),
                    truncate(e.getCourseTitle(), 28), e.getCourseType(),
                    e.getAttendedClasses(), e.getTotalClasses(),
                    e.getAttendancePercentage(), e.getEligibilityStatus());
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

    // =========================================================================
    // 4. INCREMENTAL MARKS ENTRY ("Then and Now")
    // =========================================================================

    private static void handleIncrementalMarksEntry() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + CYAN + BOLD + "--- [ 4. Phased Incremental Marks Entry (\"Then and Now\") ] ---" + RESET);
            System.out.println("  1. Phase 1 (Mid-Semester): Record CIA Test 1 & CIA Test 2 (0-100)");
            System.out.println("  2. Phase 2 (End of Semester - CIA): Record Assignments / Quizzes / Lab (0-100)");
            System.out.println("  3. Phase 3 (Post Semester): Record End Semester Exam (ESE) Marks (0-100) / Absence (AB)");
            System.out.println("  4. View Incremental Assessment Ledger");
            System.out.println("  5. Return to Main Menu");
            int choice = readIntChoice("Select option (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    recordPhase1CIA();
                    break;
                case 2:
                    recordPhase2CIA();
                    break;
                case 3:
                    recordPhase3ESE();
                    break;
                case 4:
                    viewAssessmentLedger();
                    break;
                case 5:
                    back = true;
                    break;
            }
        }
    }

    private static void recordPhase1CIA() {
        System.out.println("\n" + BOLD + ">>> Phase 1 (Mid-Semester Assessment): CIA Test 1 & CIA Test 2 <<<" + RESET);
        Enrollment en = selectEnrollmentPrompt();
        if (en == null) return;

        try {
            AssessmentMarks currentMarks = academicDAO.getMarksByEnrollmentId(en.getEnrollmentId());
            if (currentMarks != null) {
                System.out.printf("Current Status -> CIA 1: %s, CIA 2: %s%n",
                        currentMarks.getCia1() != null ? currentMarks.getCia1() : "Not Entered",
                        currentMarks.getCia2() != null ? currentMarks.getCia2() : "Not Entered");
            }

            double cia1 = readDoubleScore("Enter CIA Test 1 Marks (0 - 100): ");
            double cia2 = readDoubleScore("Enter CIA Test 2 Marks (0 - 100): ");

            boolean ok = academicDAO.recordPhase1Marks(en.getEnrollmentId(), cia1, cia2);
            if (ok) {
                System.out.println(GREEN + "[+] Phase 1 Marks successfully recorded for " + en.getRollNo() + " in " + en.getCourseCode() + "!" + RESET);
                double avg = (cia1 + cia2) / 2.0;
                System.out.printf("    CIA Test Average: %.2f / 100%n", avg);
            } else {
                System.out.println(RED + "[!] Failed to record Phase 1 marks." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void recordPhase2CIA() {
        System.out.println("\n" + BOLD + ">>> Phase 2 (End of Semester - CIA Component): Assignments / Quizzes / Lab Evaluations <<<" + RESET);
        Enrollment en = selectEnrollmentPrompt();
        if (en == null) return;

        try {
            AssessmentMarks currentMarks = academicDAO.getMarksByEnrollmentId(en.getEnrollmentId());
            if (currentMarks != null) {
                System.out.printf("Current Phase 1 -> CIA 1: %s, CIA 2: %s | Current Phase 2: %s%n",
                        currentMarks.getCia1() != null ? currentMarks.getCia1() : "--",
                        currentMarks.getCia2() != null ? currentMarks.getCia2() : "--",
                        currentMarks.getAssignmentQuiz() != null ? currentMarks.getAssignmentQuiz() : "--");
            }

            double score = readDoubleScore("Enter Assignment / Quiz / Lab Evaluation Score (0 - 100): ");

            boolean ok = academicDAO.recordPhase2Marks(en.getEnrollmentId(), score);
            if (ok) {
                System.out.println(GREEN + "[+] Phase 2 Marks successfully recorded!" + RESET);
                if (currentMarks != null && currentMarks.getCia1() != null && currentMarks.getCia2() != null) {
                    double testAvg = (currentMarks.getCia1() + currentMarks.getCia2()) / 2.0;
                    double totalCia100 = (testAvg * 0.5) + (score * 0.5);
                    System.out.printf("    Aggregated Raw CIA (out of 100): %.2f%n", totalCia100);
                }
            } else {
                System.out.println(RED + "[!] Failed to record Phase 2 marks." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static void recordPhase3ESE() {
        System.out.println("\n" + BOLD + ">>> Phase 3 (Post Semester Examination): End Semester Examination (ESE) <<<" + RESET);
        Enrollment en = selectEnrollmentPrompt();
        if (en == null) return;

        // Check Attendance Filter Eligibility first!
        double attPct = en.getAttendancePercentage();
        if (attPct < 65.0) {
            System.out.println(RED + BOLD + "[!] ATTENDANCE SHORTAGE DETECTED (" + String.format("%.1f", attPct) + "% < 65%)!" + RESET);
            System.out.println(RED + "    Under KEC R2024, this student is BARRED from appearing in ESE." + RESET);
            System.out.println(RED + "    Grade awarded is automatically 'SA' (0 Grade Points). ESE entry blocked." + RESET);
            return;
        } else if (attPct < 75.0 && !en.isCondoned()) {
            System.out.println(YELLOW + BOLD + "[!] CONDITIONAL ATTENDANCE (" + String.format("%.1f", attPct) + "%). Medical Condonation NOT APPROVED!" + RESET);
            System.out.print("    Approve Medical Condonation now? (Y/N): ");
            String ans = scanner.nextLine().trim();
            if (ans.equalsIgnoreCase("Y")) {
                try {
                    academicDAO.updateAttendance(en.getEnrollmentId(), en.getTotalClasses(), en.getAttendedClasses(), true);
                    en.setCondoned(true);
                    System.out.println(GREEN + "[+] Medical Condonation recorded! Student is now eligible for ESE." + RESET);
                } catch (SQLException e) {
                    System.out.println(RED + "[!] Error updating condonation: " + e.getMessage() + RESET);
                    return;
                }
            } else {
                System.out.println(RED + "    Student is BARRED from ESE due to lack of condonation (Grade: SA)." + RESET);
                return;
            }
        }

        System.out.print("Was the student ABSENT in the End Semester Examination? (Y/N): ");
        String absInput = scanner.nextLine().trim();
        boolean isAbsent = absInput.equalsIgnoreCase("Y");

        Double eseScore = null;
        if (!isAbsent) {
            eseScore = readDoubleScore("Enter Raw End Semester Exam (ESE) Marks (0 - 100): ");
        }

        try {
            boolean ok = academicDAO.recordPhase3Marks(en.getEnrollmentId(), eseScore, isAbsent);
            if (ok) {
                if (isAbsent) {
                    System.out.println(YELLOW + "[+] Student marked ABSENT in ESE. Final Grade: AB (0 Grade Points)." + RESET);
                } else {
                    System.out.println(GREEN + "[+] Phase 3 ESE Marks recorded successfully!" + RESET);
                    AssessmentMarks marks = academicDAO.getMarksByEnrollmentId(en.getEnrollmentId());
                    EvaluationResult eval = EvaluationEngine.evaluateCourse(en, marks);
                    System.out.printf("    ESE Scaled (%s): %.1f | Total Converted: %.0f/100 | Grade: %s | Result: %s%n",
                            en.getCourseType(), eval.getScaledEse(), eval.getTotalMarks(), eval.getLetterGrade(), eval.getResultStatus());
                }
            } else {
                System.out.println(RED + "[!] Failed to record Phase 3 marks." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    private static Enrollment selectEnrollmentPrompt() {
        System.out.print("Enter Student Roll Number: ");
        String rollNo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        int sem = readIntChoice("Enter Semester: ", 1, 8);

        try {
            Enrollment en = academicDAO.getEnrollment(rollNo, courseCode, sem);
            if (en == null) {
                System.out.println(RED + "[!] No enrollment found for " + rollNo + " in " + courseCode + " (Sem " + sem + ")." + RESET);
                return null;
            }
            return en;
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
            return null;
        }
    }

    private static void viewAssessmentLedger() {
        int sem = readIntChoice("Enter Semester (or 0 for All Semesters): ", 0, 8);
        try {
            List<Enrollment> enrollments = academicDAO.getAllEnrollments(sem == 0 ? null : sem);
            System.out.println("\n" + BOLD + "==================================== ASSESSMENT MARKS LEDGER ====================================" + RESET);
            System.out.printf("%-10s | %-8s | %-10s | %-6s | %-6s | %-8s | %-8s | %-6s | %-6s%n",
                    "ROLL NO", "COURSE", "TYPE", "CIA 1", "CIA 2", "ASSIGN", "ESE", "GRADE", "STATUS");
            System.out.println("-------------------------------------------------------------------------------------------------");
            for (Enrollment en : enrollments) {
                AssessmentMarks m = academicDAO.getMarksByEnrollmentId(en.getEnrollmentId());
                EvaluationResult eval = EvaluationEngine.evaluateCourse(en, m);

                String c1 = (m != null && m.getCia1() != null) ? String.format("%.1f", m.getCia1()) : "--";
                String c2 = (m != null && m.getCia2() != null) ? String.format("%.1f", m.getCia2()) : "--";
                String aq = (m != null && m.getAssignmentQuiz() != null) ? String.format("%.1f", m.getAssignmentQuiz()) : "--";
                String ese = (m != null && m.isAbsent()) ? "AB" : (m != null && m.getEseMarks() != null) ? String.format("%.1f", m.getEseMarks()) : "--";

                System.out.printf("%-10s | %-8s | %-10s | %-6s | %-6s | %-8s | %-8s | %-6s | %-6s%n",
                        en.getRollNo(), en.getCourseCode(), en.getCourseType(), c1, c2, aq, ese,
                        eval.getLetterGrade(), eval.getResultStatus());
            }
            System.out.println("-------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    // =========================================================================
    // 5. ACADEMIC PERFORMANCE REPORTS & ANALYTICS
    // =========================================================================

    private static void handleReportsAndAnalytics() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + CYAN + BOLD + "--- [ 5. Academic Performance Reports & Result Analytics ] ---" + RESET);
            System.out.println("  1. Student Semester Mark Sheet (Formal Grade Card with SGPA & %)");
            System.out.println("  2. Attendance Defaulter Report (Attendance < 65% flagged SA)");
            System.out.println("  3. Class Performance & Department Result Summary");
            System.out.println("  4. Return to Main Menu");
            int choice = readIntChoice("Select report (1-4): ", 1, 4);

            switch (choice) {
                case 1:
                    generateStudentMarkSheet();
                    break;
                case 2:
                    generateAttendanceDefaulterReport();
                    break;
                case 3:
                    generateClassPerformanceSummary();
                    break;
                case 4:
                    back = true;
                    break;
            }
        }
    }

    /**
     * Formal Student Semester Mark Sheet Output
     */
    private static void generateStudentMarkSheet() {
        System.out.println("\n" + BOLD + ">>> Generate Student Semester Mark Sheet <<<" + RESET);
        System.out.print("Enter Student Roll Number (e.g., 24ITR001): ");
        String rollNo = scanner.nextLine().trim().toUpperCase();
        int sem = readIntChoice("Enter Semester (1-8): ", 1, 8);

        try {
            Student s = studentDAO.getStudentByRollNo(rollNo);
            if (s == null) {
                System.out.println(RED + "[!] Student with Roll Number '" + rollNo + "' not found." + RESET);
                return;
            }

            List<EvaluationResult> results = academicDAO.getStudentSemesterEvaluation(rollNo, sem);
            if (results.isEmpty()) {
                System.out.println(YELLOW + "[!] No courses enrolled for student " + rollNo + " in Semester " + sem + "." + RESET);
                return;
            }

            // Print Formal KEC Mark Sheet
            System.out.println("\n" + CYAN + BOLD);
            System.out.println("+=========================================================================================================+");
            System.out.println("|                                  KONGU ENGINEERING COLLEGE (AUTONOMOUS)                                 |");
            System.out.println("|                                    AFFILIATED TO ANNA UNIVERSITY                                        |");
            System.out.println("|                                 OFFICIAL SEMESTER GRADE STATEMENT                                       |");
            System.out.println("|                                  REGULATION 2024 (R2024) B.E. / B.TECH                                  |");
            System.out.println("+=========================================================================================================+" + RESET);

            System.out.printf("| Roll Number : %-25s | Name       : %-42s |%n", s.getRollNo(), s.getName());
            System.out.printf("| Department  : %-25s | Semester   : Sem %-39d |%n", s.getDepartment(), sem);
            System.out.println("+---------------------------------------------------------------------------------------------------------+");
            System.out.printf("| %-9s | %-24s | %-8s | %-4s | %-6s | %-7s | %-7s | %-5s | %-5s | %-4s | %-6s |%n",
                    "CODE", "COURSE TITLE", "TYPE", "CRD", "ATT %", "CIA(W)", "ESE(W)", "TOTAL", "GRADE", "GP", "RESULT");
            System.out.println("+---------------------------------------------------------------------------------------------------------+");

            int totalEnrolledCredits = 0;
            int totalEarnedCredits = 0;
            int totalWeightedGradePoints = 0;
            boolean allPassed = true;

            for (EvaluationResult r : results) {
                totalEnrolledCredits += r.getCredits();
                if ("PASS".equals(r.getResultStatus())) {
                    totalEarnedCredits += r.getCredits();
                } else {
                    allPassed = false;
                }
                totalWeightedGradePoints += (r.getCredits() * r.getGradePoints());

                String ciaStr = String.format("%.1f", r.getScaledCia());
                String eseStr = r.isAbsent() ? "AB" : (r.getRawEseMarks() == null ? "--" : String.format("%.1f", r.getScaledEse()));
                String totalStr = (r.getTotalMarks() > 0 || "PASS".equals(r.getResultStatus())) ? String.format("%.0f", r.getTotalMarks()) : "--";

                System.out.printf("| %-9s | %-24s | %-8s | %-4d | %5.1f%% | %-7s | %-7s | %-5s | %-5s | %-4d | %-6s |%n",
                        r.getCourseCode(),
                        truncate(r.getCourseTitle(), 24),
                        r.getCourseType(),
                        r.getCredits(),
                        r.getAttendancePercentage(),
                        ciaStr,
                        eseStr,
                        totalStr,
                        r.getLetterGrade(),
                        r.getGradePoints(),
                        r.getResultStatus());
            }

            System.out.println("+=========================================================================================================+");
            double sgpa = EvaluationEngine.calculateSGPA(results);
            double equivPct = EvaluationEngine.calculateEquivalentPercentage(sgpa);

            System.out.printf("| Total Credits Enrolled : %-15d | Total Credits Earned : %-39d |%n",
                    totalEnrolledCredits, totalEarnedCredits);
            System.out.printf("| Grade Points Achieved  : %-15d | Semester Status      : %-39s |%n",
                    totalWeightedGradePoints, (allPassed ? GREEN + BOLD + "PASSED" + RESET : RED + BOLD + "RE-APPEAR (RA/SA/AB)" + RESET));
            System.out.printf("| SGPA                   : %-15.2f | Equivalent Percentage: %-38.2f%% |%n",
                    sgpa, equivPct);
            System.out.println("+=========================================================================================================+");

            System.out.println(BLUE + "Note on Marks & Weightage:");
            System.out.println("  * THEORY: CIA 40% + ESE 60% | Min ESE: 27/60 | Pass Min: 50 | * PRACTICAL: CIA 60% + ESE 40% | Min ESE: 20/40");
            System.out.println("  * EMBEDDED: CIA 50% + ESE 50% | Min ESE: 22.5/50 | SA: Attendance Shortage (<65%) | AB: Absent");
            System.out.println("  * Equivalent Percentage = SGPA * 10 (as per Anna University / KEC Autonomous Formula)" + RESET);

        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    /**
     * Formal Attendance Defaulter Report
     */
    private static void generateAttendanceDefaulterReport() {
        System.out.println("\n" + BOLD + ">>> Attendance Defaulter Report <<<" + RESET);
        int sem = readIntChoice("Enter Semester to Filter (or 0 for All Semesters): ", 0, 8);

        try {
            List<EvaluationResult> defaulters = academicDAO.getAttendanceDefaulters(sem == 0 ? null : sem);

            System.out.println("\n" + RED + BOLD);
            System.out.println("======================================================================================================");
            System.out.println("                     KEC R2024 - ATTENDANCE DEFAULTER REPORT (GRADE: SA)                              ");
            System.out.println("               [Rule: Attendance < 65% or 65-74.9% without Condonation -> Barred from ESE]           ");
            System.out.println("======================================================================================================" + RESET);

            if (defaulters.isEmpty()) {
                System.out.println(GREEN + "  No attendance defaulters found! All enrolled students meet eligibility criteria." + RESET);
            } else {
                System.out.printf("%-10s | %-20s | %-10s | %-25s | %-12s | %-8s | %-6s | %-20s%n",
                        "ROLL NO", "NAME", "COURSE", "COURSE TITLE", "CLASSES", "PCT", "GRADE", "REMARKS");
                System.out.println("------------------------------------------------------------------------------------------------------");
                for (EvaluationResult d : defaulters) {
                    System.out.printf("%-10s | %-20s | %-10s | %-25s | %3d / %-3d    | %5.1f%% | %-6s | %-20s%n",
                            d.getRollNo(),
                            truncate(d.getStudentName(), 20),
                            d.getCourseCode(),
                            truncate(d.getCourseTitle(), 25),
                            d.getAttendedClasses(),
                            d.getTotalClasses(),
                            d.getAttendancePercentage(),
                            RED + BOLD + d.getLetterGrade() + RESET,
                            d.getRemarks());
                }
                System.out.println("------------------------------------------------------------------------------------------------------");
                System.out.println(YELLOW + "Total Defaulters Flagged with Grade 'SA': " + defaulters.size() + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    /**
     * Formal Class Performance & Result Summary Output
     */
    private static void generateClassPerformanceSummary() {
        System.out.println("\n" + BOLD + ">>> Class Performance & Result Summary <<<" + RESET);
        System.out.print("Enter Department (or 'ALL' for College Overall): ");
        String dept = scanner.nextLine().trim();
        int sem = readIntChoice("Enter Semester (1-8): ", 1, 8);

        try {
            ClassPerformanceSummary s = academicDAO.getClassPerformanceSummary(dept, sem);

            System.out.println("\n" + CYAN + BOLD);
            System.out.println("=====================================================================================================");
            System.out.println("                      KEC R2024 - CLASS PERFORMANCE & RESULT SUMMARY                                  ");
            System.out.printf("               Department: %-25s | Semester: %-4d                     %n",
                    s.getDepartment().toUpperCase(), s.getSemester());
            System.out.println("=====================================================================================================" + RESET);

            System.out.printf("  Total Students Evaluated  : %d%n", s.getTotalStudents());
            System.out.printf("  Total Students Passed     : %s%d%s%n", GREEN + BOLD, s.getTotalPassedStudents(), RESET);
            System.out.printf("  Total Students Failed     : %s%d%s%n", (s.getTotalFailedStudents() > 0 ? RED + BOLD : ""), s.getTotalFailedStudents(), RESET);
            System.out.printf("    - Attendance Defaulters (SA) : %d%n", s.getTotalDefaultersSA());
            System.out.printf("    - Absent in ESE (AB)         : %d%n", s.getTotalAbsenteesAB());
            System.out.printf("  Overall Pass Percentage   : %s%.2f%%%s%n", BOLD + PURPLE, s.getOverallPassPercentage(), RESET);
            System.out.printf("  Class Average SGPA        : %.2f / 10.00%n", s.getAverageSGPA());
            System.out.printf("  Equivalent Class Average  : %.2f%%%n", s.getAverageSGPA() * 10.0);
            System.out.printf("  Highest SGPA in Class     : %.2f%n", s.getHighestSGPA());
            System.out.printf("  Lowest SGPA in Class      : %.2f%n", s.getLowestSGPA());

            System.out.println("\n" + BOLD + "Course-wise Performance Breakdown:" + RESET);
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("%-12s | %-15s | %-15s | %-15s%n", "COURSE CODE", "TOTAL ENROLLED", "PASSED COUNT", "PASS PERCENTAGE");
            System.out.println("--------------------------------------------------------------------------");
            for (Map.Entry<String, Integer> entry : s.getCourseTotalCount().entrySet()) {
                String cCode = entry.getKey();
                int totalEnr = entry.getValue();
                int passed = s.getCoursePassCount().getOrDefault(cCode, 0);
                double passPct = totalEnr > 0 ? ((passed * 100.0) / totalEnr) : 0.0;
                System.out.printf("%-12s | %-15d | %-15d | %6.2f%%%n", cCode, totalEnr, passed, passPct);
            }
            System.out.println("--------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println(RED + "[!] Database Error: " + e.getMessage() + RESET);
        }
    }

    // =========================================================================
    // 6. KEC R2024 REGULATIONS & EVALUATION RULES REFERENCE
    // =========================================================================

    private static void displayEvaluationRulesGuide() {
        System.out.println("\n" + CYAN + BOLD + "=========================================================================================");
        System.out.println("              KEC REGULATION 2024 (R2024) - ACADEMIC EVALUATION SPECIFICATIONS           ");
        System.out.println("=========================================================================================" + RESET);

        System.out.println(BOLD + "\n1. COURSE TYPE WEIGHTAGES & PASSING MINIMUM:" + RESET);
        System.out.println("  +------------+---------------+---------------+--------------------+---------------------+");
        System.out.println("  | Course Type| CIA Weightage | ESE Weightage | ESE Min. Threshold | Overall Pass Minimum|");
        System.out.println("  +------------+---------------+---------------+--------------------+---------------------+");
        System.out.println("  | THEORY     |      40%      |      60%      |    45% (27.0 / 60) |       50 Marks      |");
        System.out.println("  | PRACTICAL  |      60%      |      40%      |    50% (20.0 / 40) |       50 Marks      |");
        System.out.println("  | EMBEDDED   |      50%      |      50%      |    45% (22.5 / 50) |       50 Marks      |");
        System.out.println("  +------------+---------------+---------------+--------------------+---------------------+");

        System.out.println(BOLD + "\n2. ATTENDANCE ELIGIBILITY RULES:" + RESET);
        System.out.println("  - >= 75.0%        : Fully ELIGIBLE to write End Semester Examination (ESE).");
        System.out.println("  - 65.0% to 74.9%  : CONDITIONALLY ELIGIBLE (Requires Medical Condonation approval).");
        System.out.println("  - < 65.0%         : BARRED from ESE. Flagged with Grade 'SA' (Shortage of Attendance).");
        System.out.println("                      Grade Point = 0. Must re-register for the course.");

        System.out.println(BOLD + "\n3. 10-POINT LETTER GRADING SCALE:" + RESET);
        System.out.println("  +--------------+---------------+-------------+------------------------------------+");
        System.out.println("  | Total Marks  | Letter Grade  | Grade Point | Description                        |");
        System.out.println("  +--------------+---------------+-------------+------------------------------------+");
        System.out.println("  | 91 - 100     |       O       |     10      | Outstanding                        |");
        System.out.println("  | 81 - 90      |      A+       |      9      | Excellent                          |");
        System.out.println("  | 71 - 80      |       A       |      8      | Very Good                          |");
        System.out.println("  | 61 - 70      |      B+       |      7      | Good                               |");
        System.out.println("  | 50 - 60      |       B       |      6      | Average / Pass                     |");
        System.out.println("  | < 50         |      RA       |      0      | Re-Appearance (Failed)             |");
        System.out.println("  | Shortage     |      SA       |      0      | Shortage of Attendance (<65%)      |");
        System.out.println("  | Absent       |      AB       |      0      | Absent in ESE                      |");
        System.out.println("  +--------------+---------------+-------------+------------------------------------+");

        System.out.println(BOLD + "\n4. SGPA & PERCENTAGE FORMULAS:" + RESET);
        System.out.println("  * SGPA = Sum(Course Credits * Grade Points) / Sum(Total Enrolled Credits)");
        System.out.println("  * Equivalent Percentage = SGPA * 10");
        System.out.println("=========================================================================================");
    }

    // =========================================================================
    // 7. DATABASE CONFIGURATION & UTILITIES
    // =========================================================================

    private static void handleDatabaseConfiguration() {
        System.out.println("\n" + CYAN + BOLD + "--- [ 7. Database Status & Configuration ] ---" + RESET);
        System.out.println("  Active Database Engine : " + GREEN + BOLD + DBConnection.getActiveDatabaseType() + RESET);
        System.out.println("  1. Test Connection");
        System.out.println("  2. Reload / Reset Demonstration Dataset from kec_schema.sql");
        System.out.println("  3. Return to Main Menu");
        int choice = readIntChoice("Select option (1-3): ", 1, 3);

        switch (choice) {
            case 1:
                try {
                    java.sql.Connection c = DBConnection.getConnection();
                    System.out.println(GREEN + "[+] Connection is ACTIVE! Database: " + DBConnection.getActiveDatabaseType() + RESET);
                    System.out.println("    Catalog/Product: " + c.getMetaData().getDatabaseProductName() + " " + c.getMetaData().getDatabaseProductVersion());
                } catch (Exception e) {
                    System.out.println(RED + "[!] Connection Test Failed: " + e.getMessage() + RESET);
                }
                break;
            case 2:
                System.out.print("Are you sure you want to re-seed demo data? This resets initial records (Y/N): ");
                String conf = scanner.nextLine().trim();
                if (conf.equalsIgnoreCase("Y")) {
                    academicDAO.resetAndSeedDemoData();
                }
                break;
            case 3:
                break;
        }
    }

    // =========================================================================
    // HELPER INPUT UTILITIES
    // =========================================================================

    private static int readIntChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.println(RED + "[!] Please enter a number between " + min + " and " + max + "." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "[!] Invalid input. Please enter a valid number." + RESET);
            }
        }
    }

    private static double readDoubleScore(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val >= 0.0 && val <= 100.0) {
                    return val;
                }
                System.out.println(RED + "[!] Marks must be between 0.0 and 100.0." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "[!] Invalid score. Please enter a numerical value (0-100)." + RESET);
            }
        }
    }

    private static String truncate(String str, int maxLen) {
        if (str == null) return "";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 3) + "...";
    }
}

