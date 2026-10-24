/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * DTO: Calculated Course Evaluation Result
 */
public class EvaluationResult {
    private int enrollmentId;
    private String rollNo;
    private String studentName;
    private String department;
    private String courseCode;
    private String courseTitle;
    private String courseType; // THEORY, PRACTICAL, EMBEDDED
    private int credits;
    private int semester;

    // Attendance
    private int totalClasses;
    private int attendedClasses;
    private double attendancePercentage;
    private boolean isCondoned;
    private boolean eligibleForESE;

    // Assessment Raw Marks
    private Double cia1;
    private Double cia2;
    private Double assignmentQuiz;
    private Double rawCiaAverage; // Out of 100
    private Double rawEseMarks;
    private boolean isAbsent;

    // Scaled Marks (According to KEC R2024 weightage)
    private double ciaWeightagePct; // 40%, 60%, 50%
    private double eseWeightagePct; // 60%, 40%, 50%
    private double scaledCia;       // Max 40, 60, or 50
    private double scaledEse;       // Max 60, 40, or 50
    private double eseMinThreshold; // 27/60 (45%), 20/40 (50%), 22.5/50 (45%)
    private double totalMarks;      // Out of 100

    // Grade and Status
    private String letterGrade;     // O, A+, A, B+, B, RA, SA, AB
    private int gradePoints;        // 10, 9, 8, 7, 6, 0
    private String resultStatus;    // PASS, FAIL
    private String remarks;

    public EvaluationResult() {
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public boolean isCondoned() {
        return isCondoned;
    }

    public void setCondoned(boolean condoned) {
        isCondoned = condoned;
    }

    public boolean isEligibleForESE() {
        return eligibleForESE;
    }

    public void setEligibleForESE(boolean eligibleForESE) {
        this.eligibleForESE = eligibleForESE;
    }

    public Double getCia1() {
        return cia1;
    }

    public void setCia1(Double cia1) {
        this.cia1 = cia1;
    }

    public Double getCia2() {
        return cia2;
    }

    public void setCia2(Double cia2) {
        this.cia2 = cia2;
    }

    public Double getAssignmentQuiz() {
        return assignmentQuiz;
    }

    public void setAssignmentQuiz(Double assignmentQuiz) {
        this.assignmentQuiz = assignmentQuiz;
    }

    public Double getRawCiaAverage() {
        return rawCiaAverage;
    }

    public void setRawCiaAverage(Double rawCiaAverage) {
        this.rawCiaAverage = rawCiaAverage;
    }

    public Double getRawEseMarks() {
        return rawEseMarks;
    }

    public void setRawEseMarks(Double rawEseMarks) {
        this.rawEseMarks = rawEseMarks;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    public double getCiaWeightagePct() {
        return ciaWeightagePct;
    }

    public void setCiaWeightagePct(double ciaWeightagePct) {
        this.ciaWeightagePct = ciaWeightagePct;
    }

    public double getEseWeightagePct() {
        return eseWeightagePct;
    }

    public void setEseWeightagePct(double eseWeightagePct) {
        this.eseWeightagePct = eseWeightagePct;
    }

    public double getScaledCia() {
        return scaledCia;
    }

    public void setScaledCia(double scaledCia) {
        this.scaledCia = scaledCia;
    }

    public double getScaledEse() {
        return scaledEse;
    }

    public void setScaledEse(double scaledEse) {
        this.scaledEse = scaledEse;
    }

    public double getEseMinThreshold() {
        return eseMinThreshold;
    }

    public void setEseMinThreshold(double eseMinThreshold) {
        this.eseMinThreshold = eseMinThreshold;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public int getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(int gradePoints) {
        this.gradePoints = gradePoints;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

