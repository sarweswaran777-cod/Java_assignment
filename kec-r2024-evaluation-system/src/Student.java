/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Student Academic & Continuous Evaluation System
 *
 * Entity: Student
 */
public class Student {
    private String rollNo;
    private String name;
    private String department;
    private int currentSemester;

    public Student() {
    }

    public Student(String rollNo, String name, String department, int currentSemester) {
        this.rollNo = rollNo;
        this.name = name;
        this.department = department;
        this.currentSemester = currentSemester;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-20s | Dept: %-25s | Sem: %d", 
                rollNo, name, department, currentSemester);
    }
}

