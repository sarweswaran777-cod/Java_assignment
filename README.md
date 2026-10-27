# Kongu Engineering College (Autonomous), Perundurai - 638060
## Regulation 2024 (R2024) Student Academic & Continuous Evaluation System
### Developed using Java JDBC & Relational Database

---

### Project Overview
A database-driven Java application utilizing JDBC to manage student records, perform incremental marks entry (Continuous Internal Assessments and End Semester Examinations), enforce attendance eligibility rules, and generate formal academic performance reports according to **Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)**.

---

### Key Features & Specifications

1. **Student & Course Catalog Management**:
   - Register students with Roll Number (e.g. `24ITR001`), Name, Department, and Semester.
   - Course catalog supporting three course types: `THEORY`, `PRACTICAL`, and `EMBEDDED`.
   - Course enrollment and continuous attendance tracking (total classes held vs. attended).

2. **Phased Incremental Marks Entry ("Then and Now")**:
   - **Phase 1 (Mid-Semester)**: CIA Test 1 (0–100) and CIA Test 2 (0–100).
   - **Phase 2 (End of Semester - CIA)**: Continuous Assignments / Quizzes / Lab Evaluations (0–100).
   - **Phase 3 (Post Semester)**: End Semester Examination (ESE) Marks (0–100) or Mark Absence (AB).

3. **KEC R2024 Weightage & Evaluation Engine**:
   | Course Type | CIA Weightage | ESE Weightage | ESE Minimum Threshold | Overall Pass Minimum |
   |:---|:---:|:---:|:---:|:---:|
   | **THEORY** | 40% | 60% | 45% (27.0 / 60) | 50 Marks |
   | **PRACTICAL** | 60% | 40% | 50% (20.0 / 40) | 50 Marks |
   | **EMBEDDED** | 50% | 50% | 45% (22.5 / 50) | 50 Marks |

4. **Attendance Filter & Eligibility Enforcement**:
   - $\ge 75\%$: Eligible to sit for ESE.
   - $65\% \text{ to } 74.9\%$: Conditionally eligible (Requires Medical Condonation approval).
   - $< 65\%$: Barred from ESE. Flagged with Grade `SA` (Shortage of Attendance, 0 Grade Points).

5. **10-Point Grading System**:
   - `O` (91 - 100) : 10 Grade Points
   - `A+` (81 - 90) : 9 Grade Points
   - `A` (71 - 80) : 8 Grade Points
   - `B+` (61 - 70) : 7 Grade Points
   - `B` (50 - 60) : 6 Grade Points
   - `RA` (Failed $<50$ or failed ESE threshold) : 0 Grade Points
   - `SA` (Shortage of Attendance) : 0 Grade Points
   - `AB` (Absent in ESE) : 0 Grade Points

6. **SGPA & Equivalent Percentage Formulas**:
   - $\text{SGPA} = \frac{\sum (\text{Course Credits} \times \text{Grade Points Earned})}{\sum \text{Total Enrolled Credits}}$
   - $\text{Equivalent Percentage} = \text{SGPA} \times 10$

7. **Reports Generated**:
   - **Student Semester Mark Sheet**: Official grade statement for a given Roll Number showing internal scores, external scores, total, final letter grade, pass/fail status, SGPA, and equivalent percentage.
   - **Attendance Defaulter Report**: List of all students with attendance $<65\%$ or non-condoned attendance flagged with grade `SA`.
   - **Class Performance & Result Summary**: Department-wise pass/fail counts, class average SGPA, and course pass rates.

---

### Project Structure

```
kec-r2024-evaluation-system/
├── kec_schema.sql                  # Database Schema DDL & initial DML seed
├── db.properties                   # Database configuration (H2 / MySQL)
├── compile.bat                     # Windows Batch script to compile sources
├── run.bat                         # Windows Batch script to launch CLI
├── README.md                       # Comprehensive project documentation
├── EXECUTION_DOCUMENTATION.md      # Execution proofs and sample outputs
├── lib/                            # JDBC Drivers
│   ├── h2-2.2.224.jar              # Embedded zero-config file DB driver
│   └── mysql-connector-j-8.3.0.jar # MySQL Connector/J driver
├── bin/                            # Compiled .class files
└── src/                            # Java source code
    ├── DBConnection.java           # JDBC Connection Manager (H2/MySQL dual mode)
    ├── StudentDAO.java             # Student & Course CRUD operations
    ├── AcademicDAO.java            # Enrollment, Attendance & Marks CRUD
    ├── EvaluationEngine.java       # KEC R2024 weightages & grade calculations
    ├── EvaluationResult.java       # Academic result DTO
    ├── ClassPerformanceSummary.java# Department analytics DTO
    ├── Student.java                # Student Model
    ├── Course.java                 # Course Model
    ├── Enrollment.java             # Enrollment & Attendance Model
    ├── AssessmentMarks.java        # Phased Marks Model
    └── MainApp.java                # Menu-driven console interface
```

---

### Database Configuration (Dual-Mode Support)

The application supports both **Embedded H2** (zero configuration, works out of the box) and **MySQL Server** (standard college lab setup) via [db.properties](file:///C:/Users/SARVESH/.gemini/antigravity/scratch/kec-r2024-evaluation-system/db.properties):

```properties
# db.type can be 'h2' or 'mysql'
db.type=h2

# H2 Configuration (Default, file-based)
h2.url=jdbc:h2:./kec_eval_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE
h2.user=sa
h2.password=

# MySQL Configuration (To use XAMPP / MySQL, set db.type=mysql)
mysql.url=jdbc:mysql://localhost:3306/kec_eval_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata
mysql.user=root
mysql.password=root
```

If `db.type=mysql` is chosen but MySQL Server is offline, `DBConnection.java` gracefully warns and falls back to Embedded H2 to prevent crashes.

---

### Compilation & Execution Instructions

#### 1. Quick Launch (Windows Batch):
Simply double click or execute:
```cmd
run.bat
```
(If not compiled yet, `run.bat` automatically calls `compile.bat` first).

#### 2. Manual Command Line:
From inside `kec-r2024-evaluation-system`:
```cmd
# Compile
javac -encoding UTF-8 -cp "lib/*" -d bin src/*.java

# Run
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" MainApp
```

#### 3. In PowerShell:
```powershell
Set-Location "C:\Users\SARVESH\.gemini\antigravity\scratch\kec-r2024-evaluation-system"
javac -encoding UTF-8 -cp "lib/*" -d bin src/*.java
java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" MainApp
```

---

### Demonstration Dataset Included in Schema

1. **Students**:
   - `24ITR001` - Aravind Kumar S (Sem 3 IT)
   - `24ITR002` - Bhavani Shankar M (Sem 3 IT)
   - `24ITR003` - Chandru V (Sem 3 IT) - *Defaulter in 24ITT31 ($56\%$ Attendance $\rightarrow$ SA)*
   - `24ITR004` - Deepika R (Sem 3 IT) - *Absent in ESE $\rightarrow$ AB*
   - `24CSR050` - Gokulnath P (Sem 3 CSE)
   - `24ITR005` - Kavitha M (Sem 3 IT)

2. **Courses**:
   - `24ITT31` - Data Structures and Algorithms (THEORY, 3 Credits)
   - `24ITP31` - Data Structures Laboratory (PRACTICAL, 2 Credits)
   - `24ITE31` - Object Oriented Programming with Java (EMBEDDED, 4 Credits)
   - `24MAT31` - Discrete Mathematics and Linear Algebra (THEORY, 4 Credits)
   - `24ITT34` - Operating Systems (THEORY, 3 Credits)

