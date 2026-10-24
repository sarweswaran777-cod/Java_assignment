import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * AcademicDAO: Handles Course Enrollments, Attendance Tracking, 
 * Phased Incremental Marks Entry, and Academic Evaluation Analytics.
 */
public class AcademicDAO {

    // ==========================================
    // 1. COURSE ENROLLMENT & ATTENDANCE
    // ==========================================

    public int enrollStudent(String rollNo, String courseCode, int semester, 
                             int totalClasses, int attendedClasses, boolean isCondoned) throws SQLException {
        String insertEnrollSql = "INSERT INTO enrollments (roll_no, course_code, semester, total_classes, attended_classes, is_condoned) "
                               + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertEnrollSql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, rollNo.trim().toUpperCase());
            ps.setString(2, courseCode.trim().toUpperCase());
            ps.setInt(3, semester);
            ps.setInt(4, totalClasses);
            ps.setInt(5, attendedClasses);
            ps.setBoolean(6, isCondoned);
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int enrollmentId = rs.getInt(1);
                        initializeEmptyMarksRecord(conn, enrollmentId);
                        return enrollmentId;
                    }
                }
            }
        }
        return -1;
    }

    private void initializeEmptyMarksRecord(Connection conn, int enrollmentId) {
        String sql = "INSERT INTO assessment_marks (enrollment_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ps.executeUpdate();
        } catch (SQLException ignored) {
            // Might already exist
        }
    }

    public boolean updateAttendance(int enrollmentId, int totalClasses, int attendedClasses, boolean isCondoned) throws SQLException {
        String sql = "UPDATE enrollments SET total_classes = ?, attended_classes = ?, is_condoned = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, totalClasses);
            ps.setInt(2, attendedClasses);
            ps.setBoolean(3, isCondoned);
            ps.setInt(4, enrollmentId);
            return ps.executeUpdate() > 0;
        }
    }

    // ==========================================
    // 2. INCREMENTAL MARKS ENTRY (PHASE 1, 2, 3)
    // ==========================================

    /**
     * Phase 1 (Mid-Semester): Record CIA Test 1 (0-100) and CIA Test 2 (0-100)
     */
    public boolean recordPhase1Marks(int enrollmentId, Double cia1, Double cia2) throws SQLException {
        ensureMarksRecordExists(enrollmentId);
        String sql = "UPDATE assessment_marks SET cia1 = ?, cia2 = ?, updated_at = CURRENT_TIMESTAMP WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (cia1 != null) ps.setDouble(1, cia1); else ps.setNull(1, java.sql.Types.DECIMAL);
            if (cia2 != null) ps.setDouble(2, cia2); else ps.setNull(2, java.sql.Types.DECIMAL);
            ps.setInt(3, enrollmentId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Phase 2 (End of Semester - CIA): Record Assignments / Quizzes / Lab Evaluations (0-100)
     */
    public boolean recordPhase2Marks(int enrollmentId, Double assignmentQuiz) throws SQLException {
        ensureMarksRecordExists(enrollmentId);
        String sql = "UPDATE assessment_marks SET assignment_quiz = ?, updated_at = CURRENT_TIMESTAMP WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (assignmentQuiz != null) ps.setDouble(1, assignmentQuiz); else ps.setNull(1, java.sql.Types.DECIMAL);
            ps.setInt(2, enrollmentId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Phase 3 (Post Semester): Record End Semester Examination (ESE) Marks (0-100) and Absence (AB)
     */
    public boolean recordPhase3Marks(int enrollmentId, Double eseMarks, boolean isAbsent) throws SQLException {
        ensureMarksRecordExists(enrollmentId);
        String sql = "UPDATE assessment_marks SET ese_marks = ?, is_absent = ?, updated_at = CURRENT_TIMESTAMP WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (eseMarks != null) ps.setDouble(1, eseMarks); else ps.setNull(1, java.sql.Types.DECIMAL);
            ps.setBoolean(2, isAbsent);
            ps.setInt(3, enrollmentId);
            return ps.executeUpdate() > 0;
        }
    }

    private void ensureMarksRecordExists(int enrollmentId) {
        String checkSql = "SELECT mark_id FROM assessment_marks WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, enrollmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    initializeEmptyMarksRecord(conn, enrollmentId);
                }
            }
        } catch (SQLException ignored) {
        }
    }

    // ==========================================
    // 3. ENROLLMENT & MARKS QUERIES
    // ==========================================

    public Enrollment getEnrollmentById(int enrollmentId) throws SQLException {
        String sql = "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, e.attended_classes, e.is_condoned, "
                   + "s.name AS student_name, s.department, c.course_title, c.course_type, c.credits "
                   + "FROM enrollments e "
                   + "JOIN students s ON e.roll_no = s.roll_no "
                   + "JOIN courses c ON e.course_code = c.course_code "
                   + "WHERE e.enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEnrollment(rs);
                }
            }
        }
        return null;
    }

    public Enrollment getEnrollment(String rollNo, String courseCode, int semester) throws SQLException {
        String sql = "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, e.attended_classes, e.is_condoned, "
                   + "s.name AS student_name, s.department, c.course_title, c.course_type, c.credits "
                   + "FROM enrollments e "
                   + "JOIN students s ON e.roll_no = s.roll_no "
                   + "JOIN courses c ON e.course_code = c.course_code "
                   + "WHERE e.roll_no = ? AND e.course_code = ? AND e.semester = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNo.trim().toUpperCase());
            ps.setString(2, courseCode.trim().toUpperCase());
            ps.setInt(3, semester);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEnrollment(rs);
                }
            }
        }
        return null;
    }

    public List<Enrollment> getEnrollmentsByStudent(String rollNo, int semester) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, e.attended_classes, e.is_condoned, "
                   + "s.name AS student_name, s.department, c.course_title, c.course_type, c.credits "
                   + "FROM enrollments e "
                   + "JOIN students s ON e.roll_no = s.roll_no "
                   + "JOIN courses c ON e.course_code = c.course_code "
                   + "WHERE e.roll_no = ? AND e.semester = ? "
                   + "ORDER BY c.course_code ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNo.trim().toUpperCase());
            ps.setInt(2, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEnrollment(rs));
                }
            }
        }
        return list;
    }

    public List<Enrollment> getAllEnrollments(Integer semester) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, e.attended_classes, e.is_condoned, "
            + "s.name AS student_name, s.department, c.course_title, c.course_type, c.credits "
            + "FROM enrollments e "
            + "JOIN students s ON e.roll_no = s.roll_no "
            + "JOIN courses c ON e.course_code = c.course_code "
        );
        if (semester != null) {
            sql.append("WHERE e.semester = ? ");
        }
        sql.append("ORDER BY e.roll_no ASC, c.course_code ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (semester != null) {
                ps.setInt(1, semester);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEnrollment(rs));
                }
            }
        }
        return list;
    }

    public AssessmentMarks getMarksByEnrollmentId(int enrollmentId) throws SQLException {
        String sql = "SELECT mark_id, enrollment_id, cia1, cia2, assignment_quiz, ese_marks, is_absent "
                   + "FROM assessment_marks WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AssessmentMarks m = new AssessmentMarks();
                    m.setMarkId(rs.getInt("mark_id"));
                    m.setEnrollmentId(rs.getInt("enrollment_id"));
                    
                    double c1 = rs.getDouble("cia1");
                    m.setCia1(rs.wasNull() ? null : c1);
                    
                    double c2 = rs.getDouble("cia2");
                    m.setCia2(rs.wasNull() ? null : c2);
                    
                    double aq = rs.getDouble("assignment_quiz");
                    m.setAssignmentQuiz(rs.wasNull() ? null : aq);
                    
                    double ese = rs.getDouble("ese_marks");
                    m.setEseMarks(rs.wasNull() ? null : ese);
                    
                    m.setAbsent(rs.getBoolean("is_absent"));
                    return m;
                }
            }
        }
        return null;
    }

    private Enrollment mapEnrollment(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment();
        e.setEnrollmentId(rs.getInt("enrollment_id"));
        e.setRollNo(rs.getString("roll_no"));
        e.setCourseCode(rs.getString("course_code"));
        e.setSemester(rs.getInt("semester"));
        e.setTotalClasses(rs.getInt("total_classes"));
        e.setAttendedClasses(rs.getInt("attended_classes"));
        e.setCondoned(rs.getBoolean("is_condoned"));
        e.setStudentName(rs.getString("student_name"));
        e.setDepartment(rs.getString("department"));
        e.setCourseTitle(rs.getString("course_title"));
        e.setCourseType(rs.getString("course_type"));
        e.setCredits(rs.getInt("credits"));
        return e;
    }

    // ==========================================
    // 4. REPORTS & EVALUATION ENGINE AGGREGATION
    // ==========================================

    /**
     * Report 1: Student Semester Mark Sheet
     * Retrieves all course evaluation results for a student in a semester
     */
    public List<EvaluationResult> getStudentSemesterEvaluation(String rollNo, int semester) throws SQLException {
        List<Enrollment> enrollments = getEnrollmentsByStudent(rollNo, semester);
        List<EvaluationResult> results = new ArrayList<>();

        for (Enrollment en : enrollments) {
            AssessmentMarks marks = getMarksByEnrollmentId(en.getEnrollmentId());
            EvaluationResult eval = EvaluationEngine.evaluateCourse(en, marks);
            results.add(eval);
        }
        return results;
    }

    /**
     * Report 2: Attendance Defaulter Report
     * Finds students with attendance < 65% (Barred, SA) or 65-74.9% not condoned
     */
    public List<EvaluationResult> getAttendanceDefaulters(Integer semester) throws SQLException {
        List<Enrollment> enrollments = getAllEnrollments(semester);
        List<EvaluationResult> defaulters = new ArrayList<>();

        for (Enrollment en : enrollments) {
            double attPct = en.getAttendancePercentage();
            if (attPct < 65.0 || (attPct < 75.0 && !en.isCondoned())) {
                AssessmentMarks marks = getMarksByEnrollmentId(en.getEnrollmentId());
                EvaluationResult eval = EvaluationEngine.evaluateCourse(en, marks);
                defaulters.add(eval);
            }
        }
        return defaulters;
    }

    /**
     * Report 3: Class Performance & Result Summary
     * Department-wise & semester-wise pass/fail counts, average SGPA, course statistics
     */
    public ClassPerformanceSummary getClassPerformanceSummary(String department, int semester) throws SQLException {
        ClassPerformanceSummary summary = new ClassPerformanceSummary();
        summary.setDepartment(department);
        summary.setSemester(semester);

        // Fetch students in department
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = (department == null || department.equalsIgnoreCase("ALL"))
                ? studentDAO.getAllStudents()
                : studentDAO.getStudentsByDepartment(department);

        Set<String> uniqueStudents = new HashSet<>();
        int passedCount = 0;
        int failedCount = 0;
        int defaulterCount = 0;
        int absentCount = 0;
        double sumSGPA = 0.0;
        double maxSGPA = 0.0;
        double minSGPA = 10.0;
        int validSgpaCount = 0;

        for (Student s : students) {
            List<EvaluationResult> evalList = getStudentSemesterEvaluation(s.getRollNo(), semester);
            if (evalList.isEmpty()) {
                continue; // Student has no enrollments in this semester
            }

            uniqueStudents.add(s.getRollNo());
            boolean studentPassedAll = true;

            for (EvaluationResult r : evalList) {
                String code = r.getCourseCode();
                summary.getCourseTotalCount().put(code, summary.getCourseTotalCount().getOrDefault(code, 0) + 1);

                if ("PASS".equals(r.getResultStatus())) {
                    summary.getCoursePassCount().put(code, summary.getCoursePassCount().getOrDefault(code, 0) + 1);
                } else {
                    studentPassedAll = false;
                    if ("SA".equals(r.getLetterGrade())) {
                        defaulterCount++;
                    } else if ("AB".equals(r.getLetterGrade())) {
                        absentCount++;
                    }
                }
            }

            double sgpa = EvaluationEngine.calculateSGPA(evalList);
            sumSGPA += sgpa;
            validSgpaCount++;
            if (sgpa > maxSGPA) maxSGPA = sgpa;
            if (sgpa < minSGPA) minSGPA = sgpa;

            if (studentPassedAll) {
                passedCount++;
            } else {
                failedCount++;
            }
        }

        summary.setTotalStudents(uniqueStudents.size());
        summary.setTotalPassedStudents(passedCount);
        summary.setTotalFailedStudents(failedCount);
        summary.setTotalDefaultersSA(defaulterCount);
        summary.setTotalAbsenteesAB(absentCount);

        if (uniqueStudents.size() > 0) {
            double passPct = ((double) passedCount / uniqueStudents.size()) * 100.0;
            summary.setOverallPassPercentage(Math.round(passPct * 100.0) / 100.0);
        } else {
            summary.setOverallPassPercentage(0.0);
        }

        if (validSgpaCount > 0) {
            double avgSgpa = sumSGPA / validSgpaCount;
            summary.setAverageSGPA(Math.round(avgSgpa * 100.0) / 100.0);
            summary.setHighestSGPA(maxSGPA);
            summary.setLowestSGPA(minSGPA == 10.0 && uniqueStudents.isEmpty() ? 0.0 : minSGPA);
        } else {
            summary.setAverageSGPA(0.0);
            summary.setHighestSGPA(0.0);
            summary.setLowestSGPA(0.0);
        }

        return summary;
    }

    /**
     * Resets and re-seeds database with demonstration dataset.
     */
    public void resetAndSeedDemoData() {
        try (Connection conn = DBConnection.getConnection()) {
            DBConnection.executeSqlScript(conn, "kec_schema.sql");
            System.out.println("[+] Database re-seeded with demonstration records.");
        } catch (SQLException e) {
            System.err.println("[!] Reset error: " + e.getMessage());
        }
    }
}

