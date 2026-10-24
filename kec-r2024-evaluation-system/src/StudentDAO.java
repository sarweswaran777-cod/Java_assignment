import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * StudentDAO: Handles CRUD operations for Students and Course Catalog
 */
public class StudentDAO {

    // ==========================================
    // 1. STUDENT MANAGEMENT
    // ==========================================

    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (roll_no, name, department, current_semester) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getRollNo().trim().toUpperCase());
            ps.setString(2, student.getName().trim());
            ps.setString(3, student.getDepartment().trim());
            ps.setInt(4, student.getCurrentSemester());
            return ps.executeUpdate() > 0;
        }
    }

    public Student getStudentByRollNo(String rollNo) throws SQLException {
        String sql = "SELECT roll_no, name, department, current_semester FROM students WHERE roll_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNo.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getString("roll_no"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getInt("current_semester")
                    );
                }
            }
        }
        return null;
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT roll_no, name, department, current_semester FROM students ORDER BY roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("current_semester")
                ));
            }
        }
        return list;
    }

    public List<Student> getStudentsByDepartment(String dept) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT roll_no, name, department, current_semester FROM students WHERE UPPER(department) LIKE ? ORDER BY roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + dept.trim().toUpperCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(
                            rs.getString("roll_no"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getInt("current_semester")
                    ));
                }
            }
        }
        return list;
    }

    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, department = ?, current_semester = ? WHERE roll_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName().trim());
            ps.setString(2, student.getDepartment().trim());
            ps.setInt(3, student.getCurrentSemester());
            ps.setString(4, student.getRollNo().trim().toUpperCase());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteStudent(String rollNo) throws SQLException {
        String sql = "DELETE FROM students WHERE roll_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNo.trim().toUpperCase());
            return ps.executeUpdate() > 0;
        }
    }

    // ==========================================
    // 2. COURSE CATALOG MANAGEMENT
    // ==========================================

    public boolean addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (course_code, course_title, credits, course_type, semester) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseCode().trim().toUpperCase());
            ps.setString(2, course.getCourseTitle().trim());
            ps.setInt(3, course.getCredits());
            ps.setString(4, course.getCourseType().trim().toUpperCase());
            ps.setInt(5, course.getSemester());
            return ps.executeUpdate() > 0;
        }
    }

    public Course getCourseByCode(String courseCode) throws SQLException {
        String sql = "SELECT course_code, course_title, credits, course_type, semester FROM courses WHERE course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseCode.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getString("course_code"),
                            rs.getString("course_title"),
                            rs.getInt("credits"),
                            rs.getString("course_type"),
                            rs.getInt("semester")
                    );
                }
            }
        }
        return null;
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_code, course_title, credits, course_type, semester FROM courses ORDER BY semester ASC, course_code ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                        rs.getString("course_code"),
                        rs.getString("course_title"),
                        rs.getInt("credits"),
                        rs.getString("course_type"),
                        rs.getInt("semester")
                ));
            }
        }
        return list;
    }

    public List<Course> getCoursesBySemester(int semester) throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_code, course_title, credits, course_type, semester FROM courses WHERE semester = ? ORDER BY course_code ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Course(
                            rs.getString("course_code"),
                            rs.getString("course_title"),
                            rs.getInt("credits"),
                            rs.getString("course_type"),
                            rs.getInt("semester")
                    ));
                }
            }
        }
        return list;
    }

    public boolean deleteCourse(String courseCode) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseCode.trim().toUpperCase());
            return ps.executeUpdate() > 0;
        }
    }
}

