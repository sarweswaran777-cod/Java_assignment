/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * Entity: Enrollment & Attendance Record
 */
public class Enrollment {
    private int enrollmentId;
    private String rollNo;
    private String courseCode;
    private int semester;
    private int totalClasses;
    private int attendedClasses;
    private boolean isCondoned; // Medical condonation for 65.0% - 74.9%

    // Joined fields for display
    private String studentName;
    private String department;
    private String courseTitle;
    private String courseType;
    private int credits;

    public Enrollment() {
    }

    public Enrollment(int enrollmentId, String rollNo, String courseCode, int semester, 
                      int totalClasses, int attendedClasses, boolean isCondoned) {
        this.enrollmentId = enrollmentId;
        this.rollNo = rollNo;
        this.courseCode = courseCode;
        this.semester = semester;
        this.totalClasses = totalClasses;
        this.attendedClasses = attendedClasses;
        this.isCondoned = isCondoned;
    }

    public double getAttendancePercentage() {
        if (totalClasses <= 0) {
            return 100.0;
        }
        return (attendedClasses * 100.0) / totalClasses;
    }

    /**
     * Attendance Eligibility Status according to KEC R2024:
     * >= 75%: Eligible
     * 65% - 74.9%: Conditionally Eligible (Requires Condonation approval)
     * < 65%: SA (Shortage of Attendance, Barred from ESE)
     */
    public String getEligibilityStatus() {
        double pct = getAttendancePercentage();
        if (pct >= 75.0) {
            return "ELIGIBLE";
        } else if (pct >= 65.0) {
            return isCondoned ? "CONDONED (ELIGIBLE)" : "CONDITIONAL (CONDONATION REQUIRED)";
        } else {
            return "BARRED (SHORTAGE - SA)";
        }
    }

    public boolean isEligibleForESE() {
        double pct = getAttendancePercentage();
        if (pct >= 75.0) return true;
        if (pct >= 65.0 && isCondoned) return true;
        return false;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(int attendedClasses) {
        this.attendedClasses = attendedClasses;
    }

    public boolean isCondoned() {
        return isCondoned;
    }

    public void setCondoned(boolean condoned) {
        isCondoned = condoned;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}

