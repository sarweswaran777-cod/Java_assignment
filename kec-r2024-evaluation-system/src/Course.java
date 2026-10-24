/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * Entity: Course
 * Types: THEORY, PRACTICAL, EMBEDDED
 */
public class Course {
    private String courseCode;
    private String courseTitle;
    private int credits;
    private String courseType; // THEORY, PRACTICAL, EMBEDDED
    private int semester;

    public Course() {
    }

    public Course(String courseCode, String courseTitle, int credits, String courseType, int semester) {
        this.courseCode = courseCode.trim().toUpperCase();
        this.courseTitle = courseTitle.trim();
        this.credits = credits;
        this.courseType = courseType.trim().toUpperCase();
        this.semester = semester;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode.trim().toUpperCase();
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle.trim();
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType.trim().toUpperCase();
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-35s | Credits: %d | Type: %-10s | Sem: %d",
                courseCode, courseTitle, credits, courseType, semester);
    }
}

