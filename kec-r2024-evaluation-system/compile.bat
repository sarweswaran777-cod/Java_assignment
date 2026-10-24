@echo off
echo =====================================================================
echo Compiling Kongu Engineering College (R2024) Academic System
echo =====================================================================
if not exist bin mkdir bin

javac -encoding UTF-8 -cp "lib/*" -d bin src/*.java
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed with error code %errorlevel%!
    pause
    exit /b %errorlevel%
)

echo.
echo [SUCCESS] All Java source files compiled successfully into bin\ directory.

