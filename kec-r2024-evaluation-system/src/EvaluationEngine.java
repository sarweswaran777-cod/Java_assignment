import java.util.List;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
 * Evaluation Rules & Weightage Engine
 *
 * Weightage Rules:
 * - THEORY:    CIA = 40% | ESE = 60% | ESE Min = 45% (27.0/60)  | Overall Pass Min = 50 Marks
 * - PRACTICAL: CIA = 60% | ESE = 40% | ESE Min = 50% (20.0/40)  | Overall Pass Min = 50 Marks
 * - EMBEDDED:  CIA = 50% | ESE = 50% | ESE Min = 45% (22.5/50)  | Overall Pass Min = 50 Marks
 *
 * Attendance Rules:
 * - >= 75%: Eligible for ESE
 * - 65% to 74.9%: Conditionally Eligible (Requires Medical Condonation approval)
 * - < 65%: Barred from ESE, Grade awarded = SA (0 Grade Points)
 *
 * 10-Point Grading Scale:
 * - O   (91 - 100) -> Grade Point: 10
 * - A+  (81 - 90)  -> Grade Point: 9
 * - A   (71 - 80)  -> Grade Point: 8
 * - B+  (61 - 70)  -> Grade Point: 7
 * - B   (50 - 60)  -> Grade Point: 6
 * - RA  (< 50 or failed ESE min) -> Grade Point: 0
 * - SA  (Shortage of Attendance) -> Grade Point: 0
 * - AB  (Absent in ESE) -> Grade Point: 0
 */
public class EvaluationEngine {

    /**
     * Evaluates a single course enrollment under KEC R2024 rules.
     */
    public static EvaluationResult evaluateCourse(Enrollment enrollment, AssessmentMarks marks) {
        EvaluationResult result = new EvaluationResult();
        
        result.setEnrollmentId(enrollment.getEnrollmentId());
        result.setRollNo(enrollment.getRollNo());
        result.setStudentName(enrollment.getStudentName());
        result.setDepartment(enrollment.getDepartment());
        result.setCourseCode(enrollment.getCourseCode());
        result.setCourseTitle(enrollment.getCourseTitle());
        result.setCourseType(enrollment.getCourseType() != null ? enrollment.getCourseType() : "THEORY");
        result.setCredits(enrollment.getCredits());
        result.setSemester(enrollment.getSemester());

        // Attendance
        int total = enrollment.getTotalClasses();
        int attended = enrollment.getAttendedClasses();
        double attPct = (total > 0) ? ((attended * 100.0) / total) : 100.0;
        result.setTotalClasses(total);
        result.setAttendedClasses(attended);
        result.setAttendancePercentage(attPct);
        result.setCondoned(enrollment.isCondoned());

        // Weightage configuration by Course Type
        String type = result.getCourseType().toUpperCase();
        double ciaWeight;
        double eseWeight;
        double eseMin;

        switch (type) {
            case "PRACTICAL":
                ciaWeight = 0.60;
                eseWeight = 0.40;
                eseMin = 20.0; // 50% of 40 marks
                break;
            case "EMBEDDED":
                ciaWeight = 0.50;
                eseWeight = 0.50;
                eseMin = 22.5; // 45% of 50 marks
                break;
            case "THEORY":
            default:
                ciaWeight = 0.40;
                eseWeight = 0.60;
                eseMin = 27.0; // 45% of 60 marks
                break;
        }

        result.setCiaWeightagePct(ciaWeight * 100.0);
        result.setEseWeightagePct(eseWeight * 100.0);
        result.setEseMinThreshold(eseMin);

        // Check Attendance Filter
        boolean isEligible = false;
        if (attPct >= 75.0) {
            isEligible = true;
        } else if (attPct >= 65.0 && enrollment.isCondoned()) {
            isEligible = true;
        }
        result.setEligibleForESE(isEligible);

        // Populate Raw Marks
        if (marks != null) {
            result.setCia1(marks.getCia1());
            result.setCia2(marks.getCia2());
            result.setAssignmentQuiz(marks.getAssignmentQuiz());
            result.setRawEseMarks(marks.getEseMarks());
            result.setAbsent(marks.isAbsent());
        }

        // 1. Attendance Shortage check (<65% or 65-74.9% without condonation)
        if (!isEligible) {
            result.setScaledCia(0.0);
            result.setScaledEse(0.0);
            result.setTotalMarks(0.0);
            result.setLetterGrade("SA");
            result.setGradePoints(0);
            result.setResultStatus("FAIL");
            if (attPct < 65.0) {
                result.setRemarks(String.format("Attendance Shortage (%.1f%% < 65%%) - Barred from ESE", attPct));
            } else {
                result.setRemarks(String.format("Attendance %.1f%% (Requires Medical Condonation)", attPct));
            }
            return result;
        }

        // 2. Absence in ESE
        if (marks != null && marks.isAbsent()) {
            // Student attended CIA but was absent in ESE
            double rawCia = computeRawCia(marks);
            result.setRawCiaAverage(rawCia);
            result.setScaledCia(Math.round(rawCia * ciaWeight * 10.0) / 10.0);
            result.setScaledEse(0.0);
            result.setTotalMarks(result.getScaledCia());
            result.setLetterGrade("AB");
            result.setGradePoints(0);
            result.setResultStatus("FAIL");
            result.setRemarks("Absent in End Semester Examination (AB)");
            return result;
        }

        // 3. Regular Evaluation
        if (marks == null || (marks.getCia1() == null && marks.getCia2() == null && marks.getEseMarks() == null)) {
            result.setLetterGrade("--");
            result.setGradePoints(0);
            result.setResultStatus("PENDING");
            result.setRemarks("Marks Entry Incomplete");
            return result;
        }

        double rawCia = computeRawCia(marks);
        result.setRawCiaAverage(rawCia);
        double scaledCia = rawCia * ciaWeight;
        result.setScaledCia(Math.round(scaledCia * 10.0) / 10.0);

        if (marks.getEseMarks() == null) {
            result.setScaledEse(0.0);
            result.setTotalMarks(result.getScaledCia());
            result.setLetterGrade("--");
            result.setGradePoints(0);
            result.setResultStatus("PENDING");
            result.setRemarks("ESE Marks Pending");
            return result;
        }

        double rawEse = marks.getEseMarks();
        double scaledEse = rawEse * eseWeight;
        result.setScaledEse(Math.round(scaledEse * 10.0) / 10.0);

        double totalMarksCalc = Math.round(result.getScaledCia() + result.getScaledEse());
        result.setTotalMarks(totalMarksCalc);

        // Check ESE Minimum Threshold
        if (result.getScaledEse() < eseMin) {
            result.setLetterGrade("RA");
            result.setGradePoints(0);
            result.setResultStatus("FAIL");
            result.setRemarks(String.format("Failed ESE Minimum Threshold (%.1f / %.1f)", 
                    result.getScaledEse(), eseMin));
            return result;
        }

        // Check Overall Pass Minimum (>= 50)
        if (totalMarksCalc < 50.0) {
            result.setLetterGrade("RA");
            result.setGradePoints(0);
            result.setResultStatus("FAIL");
            result.setRemarks(String.format("Failed Aggregate Pass Mark (Total: %.0f < 50)", totalMarksCalc));
            return result;
        }

        // Passed - Determine 10-Point Grade
        result.setResultStatus("PASS");
        if (totalMarksCalc >= 91.0) {
            result.setLetterGrade("O");
            result.setGradePoints(10);
            result.setRemarks("Outstanding");
        } else if (totalMarksCalc >= 81.0) {
            result.setLetterGrade("A+");
            result.setGradePoints(9);
            result.setRemarks("Excellent");
        } else if (totalMarksCalc >= 71.0) {
            result.setLetterGrade("A");
            result.setGradePoints(8);
            result.setRemarks("Very Good");
        } else if (totalMarksCalc >= 61.0) {
            result.setLetterGrade("B+");
            result.setGradePoints(7);
            result.setRemarks("Good");
        } else {
            result.setLetterGrade("B");
            result.setGradePoints(6);
            result.setRemarks("Average / Pass");
        }

        return result;
    }

    /**
     * Calculates raw CIA average out of 100:
     * - Phase 1: Average of CIA1 and CIA2 tests (50% weight)
     * - Phase 2: Assignment / Quiz / Lab Evaluation (50% weight)
     */
    private static double computeRawCia(AssessmentMarks marks) {
        double testScore = 0.0;
        int testCount = 0;
        if (marks.getCia1() != null) {
            testScore += marks.getCia1();
            testCount++;
        }
        if (marks.getCia2() != null) {
            testScore += marks.getCia2();
            testCount++;
        }
        double testAvg = testCount > 0 ? (testScore / testCount) : 0.0;

        double assignment = marks.getAssignmentQuiz() != null ? marks.getAssignmentQuiz() : 0.0;

        // If assignment is not yet recorded, use testAvg; otherwise blend 50-50
        if (marks.getAssignmentQuiz() == null) {
            return testAvg;
        }
        return (testAvg * 0.50) + (assignment * 0.50);
    }

    /**
     * SGPA = Sum(Course Credits * Grade Points) / Sum(Total Enrolled Credits)
     */
    public static double calculateSGPA(List<EvaluationResult> results) {
        int totalCredits = 0;
        int totalGradePointsTimesCredits = 0;

        for (EvaluationResult r : results) {
            totalCredits += r.getCredits();
            totalGradePointsTimesCredits += (r.getCredits() * r.getGradePoints());
        }

        if (totalCredits == 0) {
            return 0.0;
        }

        double sgpa = (double) totalGradePointsTimesCredits / totalCredits;
        return Math.round(sgpa * 100.0) / 100.0; // 2 decimal places
    }

    /**
     * Percentage Equivalent = SGPA * 10
     */
    public static double calculateEquivalentPercentage(double sgpa) {
        return Math.round(sgpa * 10.0 * 100.0) / 100.0;
    }
}
