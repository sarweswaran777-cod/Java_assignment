import java.util.HashMap;
import java.util.Map;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * DTO: Class Performance & Result Analytics Summary
 */
public class ClassPerformanceSummary {
    private String department;
    private int semester;
    private int totalStudents;
    private int totalPassedStudents;
    private int totalFailedStudents;
    private int totalDefaultersSA;
    private int totalAbsenteesAB;
    private double overallPassPercentage;
    private double averageSGPA;
    private double highestSGPA;
    private double lowestSGPA;

    // Course code -> Pass count / Total count
    private Map<String, Integer> coursePassCount = new HashMap<>();
    private Map<String, Integer> courseTotalCount = new HashMap<>();

    public ClassPerformanceSummary() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalPassedStudents() {
        return totalPassedStudents;
    }

    public void setTotalPassedStudents(int totalPassedStudents) {
        this.totalPassedStudents = totalPassedStudents;
    }

    public int getTotalFailedStudents() {
        return totalFailedStudents;
    }

    public void setTotalFailedStudents(int totalFailedStudents) {
        this.totalFailedStudents = totalFailedStudents;
    }

    public int getTotalDefaultersSA() {
        return totalDefaultersSA;
    }

    public void setTotalDefaultersSA(int totalDefaultersSA) {
        this.totalDefaultersSA = totalDefaultersSA;
    }

    public int getTotalAbsenteesAB() {
        return totalAbsenteesAB;
    }

    public void setTotalAbsenteesAB(int totalAbsenteesAB) {
        this.totalAbsenteesAB = totalAbsenteesAB;
    }

    public double getOverallPassPercentage() {
        return overallPassPercentage;
    }

    public void setOverallPassPercentage(double overallPassPercentage) {
        this.overallPassPercentage = overallPassPercentage;
    }

    public double getAverageSGPA() {
        return averageSGPA;
    }

    public void setAverageSGPA(double averageSGPA) {
        this.averageSGPA = averageSGPA;
    }

    public double getHighestSGPA() {
        return highestSGPA;
    }

    public void setHighestSGPA(double highestSGPA) {
        this.highestSGPA = highestSGPA;
    }

    public double getLowestSGPA() {
        return lowestSGPA;
    }

    public void setLowestSGPA(double lowestSGPA) {
        this.lowestSGPA = lowestSGPA;
    }

    public Map<String, Integer> getCoursePassCount() {
        return coursePassCount;
    }

    public Map<String, Integer> getCourseTotalCount() {
        return courseTotalCount;
    }
}

