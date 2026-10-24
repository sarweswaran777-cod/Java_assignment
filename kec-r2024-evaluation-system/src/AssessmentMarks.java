/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * Entity: Assessment Marks
 * Phased Incremental Data:
 * - Phase 1: CIA 1 (0-100), CIA 2 (0-100)
 * - Phase 2: Assignment / Quiz / Lab Evaluation (0-100)
 * - Phase 3: ESE Marks (0-100), isAbsent (AB)
 */
public class AssessmentMarks {
    private int markId;
    private int enrollmentId;

    // Phase 1 (Mid-Semester)
    private Double cia1;
    private Double cia2;

    // Phase 2 (End of Semester - CIA)
    private Double assignmentQuiz;

    // Phase 3 (Post-Semester - ESE)
    private Double eseMarks;
    private boolean isAbsent;

    public AssessmentMarks() {
    }

    public AssessmentMarks(int markId, int enrollmentId, Double cia1, Double cia2, 
                           Double assignmentQuiz, Double eseMarks, boolean isAbsent) {
        this.markId = markId;
        this.enrollmentId = enrollmentId;
        this.cia1 = cia1;
        this.cia2 = cia2;
        this.assignmentQuiz = assignmentQuiz;
        this.eseMarks = eseMarks;
        this.isAbsent = isAbsent;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
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

    public Double getEseMarks() {
        return eseMarks;
    }

    public void setEseMarks(Double eseMarks) {
        this.eseMarks = eseMarks;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }
}

