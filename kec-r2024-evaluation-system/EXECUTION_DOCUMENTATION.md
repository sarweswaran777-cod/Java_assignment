# Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
## Student Academic & Continuous Evaluation System using Java JDBC
### Lab & Project Execution Documentation

---

### System Specifications & Evaluation Rules (KEC R2024)
- **THEORY Courses**: CIA Weightage = 40% | ESE Weightage = 60% | ESE Minimum Threshold = 45% (27/60) | Overall Pass Minimum = 50 Marks
- **PRACTICAL Courses**: CIA Weightage = 60% | ESE Weightage = 40% | ESE Minimum Threshold = 50% (20/40) | Overall Pass Minimum = 50 Marks
- **EMBEDDED Courses**: CIA Weightage = 50% | ESE Weightage = 50% | ESE Minimum Threshold = 45% (22.5/50) | Overall Pass Minimum = 50 Marks
- **Attendance Filter**:
  - $\ge 75\%$: Eligible to sit for End Semester Examination (ESE).
  - $65\% \text{ to } 74.9\%$: Conditionally eligible (Requires Medical Condonation approval).
  - $< 65\%$: Barred from ESE. Flagged with Grade `SA` (Shortage of Attendance, 0 Grade Points).
- **10-Point Letter Grading Scale**:
  - `O`  : 91 - 100 (Grade Point: 10)
  - `A+` : 81 - 90  (Grade Point: 9)
  - `A`  : 71 - 80  (Grade Point: 8)
  - `B+` : 61 - 70  (Grade Point: 7)
  - `B`  : 50 - 60  (Grade Point: 6)
  - `RA` : Re-Appearance / Failed ($<50$ aggregate or failed ESE threshold) (Grade Point: 0)
  - `SA` : Shortage of Attendance ($<65\%$) (Grade Point: 0)
  - `AB` : Absent in ESE (Grade Point: 0)
- **SGPA & Percentage**:
  $$\text{SGPA} = \frac{\sum (\text{Course Credits} \times \text{Grade Points Earned})}{\sum \text{Total Enrolled Credits}}$$
  $$\text{Equivalent Percentage} = \text{SGPA} \times 10$$

---

## 1. Execution Proof: Adding a Student & Course

### A. Adding a New Student (`24ITR005` - Kavitha M)
```
--- [ 1. Student Registration & Directory ] ---
  1. Register New Student
  2. Search Student by Roll Number
  3. View All Registered Students
  4. Return to Main Menu
Select option (1-4): 1

>>> Register New Student <<<
Enter Roll Number (e.g., 24ITR005, 24CSR051): 24ITR005
Enter Student Full Name: Kavitha M
Enter Department (e.g., Information Technology, CSE, ECE, MECH): Information Technology
Enter Current Semester (1-8): 3
[+] Student registered successfully: [24ITR005] Kavitha M            | Dept: Information Technology    | Sem: 3
```

### B. Adding a New Course (`24ITT34` - Operating Systems)
```
--- [ 2. Course Catalog Management ] ---
  1. Add New Course
  2. View All Courses
  3. Filter Courses by Semester
  4. Return to Main Menu
Select option (1-4): 1

>>> Add New Course to Catalog <<<
Enter Course Code (e.g., 24ITT31, 24ITP31, 24ITE31): 24ITT34
Enter Course Title: Operating Systems
Enter Course Credits (1-6): 3
Select Course Type:
  1. THEORY    (CIA: 40% | ESE: 60% | ESE Min: 27/60)
  2. PRACTICAL (CIA: 60% | ESE: 40% | ESE Min: 20/40)
  3. EMBEDDED  (CIA: 50% | ESE: 50% | ESE Min: 22.5/50)
Enter course type (1-3): 1
Enter Semester Offered (1-8): 3
[+] Course successfully added to catalog: [24ITT34] Operating Systems                   | Credits: 3 | Type: THEORY     | Sem: 3
```

### C. Course Catalog Verification
```
=============================== KEC COURSE CATALOG (5) ===============================
CODE       | TITLE                                    | CREDITS | TYPE         | SEM  
----------------------------------------------------------------------------------------
24ITE31    | Object Oriented Programming with Java    | 4       | EMBEDDED     | Sem 3  
24ITP31    | Data Structures Laboratory               | 2       | PRACTICAL    | Sem 3  
24ITT31    | Data Structures and Algorithms           | 3       | THEORY       | Sem 3  
24ITT34    | Operating Systems                        | 3       | THEORY       | Sem 3  
24MAT31    | Discrete Mathematics and Linear Algebra  | 4       | THEORY       | Sem 3  
----------------------------------------------------------------------------------------
```

---

## 2. Execution Proof: Incremental Marks Entry ("Then and Now")

The system supports phased incremental entry matching the semester timeline:
- **Phase 1 (Mid-Semester)**: CIA Test 1 (0–100) and CIA Test 2 (0–100)
- **Phase 2 (End of Semester - CIA)**: Assignments / Quizzes / Lab Evaluations (0–100)
- **Phase 3 (Post Semester)**: End Semester Examination (ESE) Marks (0–100) and Absence (AB)

### Phase 1: Recording CIA 1 & CIA 2
```
--- [ 4. Phased Incremental Marks Entry ("Then and Now") ] ---
  1. Phase 1 (Mid-Semester): Record CIA Test 1 & CIA Test 2 (0-100)
  2. Phase 2 (End of Semester - CIA): Record Assignments / Quizzes / Lab (0-100)
  3. Phase 3 (Post Semester): Record End Semester Exam (ESE) Marks (0-100) / Absence (AB)
  4. View Incremental Assessment Ledger
  5. Return to Main Menu
Select option (1-5): 1

>>> Phase 1 (Mid-Semester Assessment): CIA Test 1 & CIA Test 2 <<<
Enter Student Roll Number: 24ITR005
Enter Course Code: 24ITT31
Enter Semester: 3
Current Status -> CIA 1: Not Entered, CIA 2: Not Entered
Enter CIA Test 1 Marks (0 - 100): 86.0
Enter CIA Test 2 Marks (0 - 100): 90.0
[+] Phase 1 Marks successfully recorded for 24ITR005 in 24ITT31!
    CIA Test Average: 88.00 / 100
```

### Phase 2: Recording Continuous Assignments / Quizzes
```
>>> Phase 2 (End of Semester - CIA Component): Assignments / Quizzes / Lab Evaluations <<<
Enter Student Roll Number: 24ITR005
Enter Course Code: 24ITT31
Enter Semester: 3
Current Phase 1 -> CIA 1: 86.0, CIA 2: 90.0 | Current Phase 2: --
Enter Assignment / Quiz / Lab Evaluation Score (0 - 100): 94.0
[+] Phase 2 Marks successfully recorded!
    Aggregated Raw CIA (out of 100): 91.00
```

### Phase 3: Recording End Semester Examination (ESE) Marks
```
>>> Phase 3 (Post Semester Examination): End Semester Examination (ESE) <<<
Enter Student Roll Number: 24ITR005
Enter Course Code: 24ITT31
Enter Semester: 3
Was the student ABSENT in the End Semester Examination? (Y/N): N
Enter Raw End Semester Exam (ESE) Marks (0 - 100): 88.0
[+] Phase 3 ESE Marks recorded successfully!
    ESE Scaled (THEORY): 52.8 | Total Converted: 89/100 | Grade: A+ | Result: PASS
```

---

## 3. Execution Proof: Generated Official Semester Grade Sheet with SGPA

### Roll Number: `24ITR001` (Aravind Kumar S) - Outstanding Performance
```
+=========================================================================================================+
|                                  KONGU ENGINEERING COLLEGE (AUTONOMOUS)                                 |
|                                    AFFILIATED TO ANNA UNIVERSITY                                        |
|                                 OFFICIAL SEMESTER GRADE STATEMENT                                       |
|                                  REGULATION 2024 (R2024) B.E. / B.TECH                                  |
+=========================================================================================================+
| Roll Number : 24ITR001                  | Name       : Aravind Kumar S                            |
| Department  : Information Technology    | Semester   : Sem 3                                       |
+---------------------------------------------------------------------------------------------------------+
| CODE      | COURSE TITLE             | TYPE     | CRD  | ATT %  | CIA(W)  | ESE(W)  | TOTAL | GRADE | GP   | RESULT |
+---------------------------------------------------------------------------------------------------------+
| 24ITE31   | Object Oriented Progr... | EMBEDDED | 4    |  91.7% | 45.8    | 45.0    | 91    | O     | 10   | PASS   |
| 24ITP31   | Data Structures Labor... | PRACTICAL | 2    |  93.3% | 55.8    | 36.8    | 93    | O     | 10   | PASS   |
| 24ITT31   | Data Structures and A... | THEORY   | 3    |  92.0% | 37.9    | 56.4    | 94    | O     | 10   | PASS   |
| 24MAT31   | Discrete Mathematics ... | THEORY   | 4    |  90.0% | 35.3    | 51.6    | 87    | A+    | 9    | PASS   |
+=========================================================================================================+
| Total Credits Enrolled : 13              | Total Credits Earned : 13                                      |
| Grade Points Achieved  : 126             | Semester Status      : PASSED                                  |
| SGPA                   : 9.69            | Equivalent Percentage: 96.90 %                                 |
+=========================================================================================================+
Note on Marks & Weightage:
  * THEORY: CIA 40% + ESE 60% | Min ESE: 27/60 | Pass Min: 50 | * PRACTICAL: CIA 60% + ESE 40% | Min ESE: 20/40
  * EMBEDDED: CIA 50% + ESE 50% | Min ESE: 22.5/50 | SA: Attendance Shortage (<65%) | AB: Absent
  * Equivalent Percentage = SGPA * 10 (as per Anna University / KEC Autonomous Formula)
```

### SGPA & Percentage Calculation Proof:
- **Credits & Grade Points Breakdown**:
  - `24ITE31` : $4 \text{ credits} \times 10 = 40$
  - `24ITP31` : $2 \text{ credits} \times 10 = 20$
  - `24ITT31` : $3 \text{ credits} \times 10 = 30$
  - `24MAT31` : $4 \text{ credits} \times 9 = 36$
- **Total Credits Enrolled**: $4 + 2 + 3 + 4 = 13$
- **Total Weighted Grade Points**: $40 + 20 + 30 + 36 = 126$
$$\text{SGPA} = \frac{126}{13} \approx 9.6923 \implies \mathbf{9.69}$$
$$\text{Equivalent Percentage} = 9.69 \times 10 = \mathbf{96.90\%}$$

---

## 4. Execution Proof: Attendance Defaulter Report (Grade: SA)

When student attendance drops below 65%, the student is barred from ESE and awarded grade `SA` (0 Grade Points):
```
======================================================================================================
                     KEC R2024 - ATTENDANCE DEFAULTER REPORT (GRADE: SA)                              
               [Rule: Attendance < 65% or 65-74.9% without Condonation -> Barred from ESE]           
======================================================================================================
ROLL NO    | NAME                 | COURSE     | COURSE TITLE              | CLASSES      | PCT      | GRADE  | REMARKS             
------------------------------------------------------------------------------------------------------
24ITR003   | Chandru V            | 24ITT31    | Data Structures and Al... |  28 / 50     |  56.0% | SA | Attendance Shortage (56.0% < 65%) - Barred from ESE
------------------------------------------------------------------------------------------------------
Total Defaulters Flagged with Grade 'SA': 1
```

---

## 5. Execution Proof: Class Performance & Result Analytics Summary

```
=====================================================================================================
                      KEC R2024 - CLASS PERFORMANCE & RESULT SUMMARY                                  
               Department: INFORMATION TECHNOLOGY    | Semester: 3                        
=====================================================================================================
  Total Students Evaluated  : 4
  Total Students Passed     : 2
  Total Students Failed     : 2
    - Attendance Defaulters (SA) : 1
    - Absent in ESE (AB)         : 1
  Overall Pass Percentage   : 50.00%
  Class Average SGPA        : 6.93 / 10.00
  Equivalent Class Average  : 69.30%
  Highest SGPA in Class     : 9.69
  Lowest SGPA in Class      : 4.57

Course-wise Performance Breakdown:
--------------------------------------------------------------------------
COURSE CODE  | TOTAL ENROLLED  | PASSED COUNT    | PASS PERCENTAGE
--------------------------------------------------------------------------
24MAT31      | 1               | 1               | 100.00%
24ITE31      | 4               | 4               | 100.00%
24ITT31      | 4               | 2               |  50.00%
24ITP31      | 3               | 3               | 100.00%
--------------------------------------------------------------------------
```

