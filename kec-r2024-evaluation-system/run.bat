@echo off
echo =====================================================================
echo Launching Kongu Engineering College (R2024) Evaluation System
echo =====================================================================
if not exist bin\MainApp.class (
    echo Binaries not found. Running compilation first...
    call compile.bat
)

java -Dfile.encoding=UTF-8 -cp "bin;lib/*" MainApp
pause

