Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "Launching Kongu Engineering College (R2024) Evaluation System" -ForegroundColor Cyan
Write-Host "=====================================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin\MainApp.class")) {
    Write-Host "Binaries not found. Compiling Java source files..." -ForegroundColor Yellow
    if (-not (Test-Path "bin")) {
        New-Item -ItemType Directory -Path "bin" | Out-Null
    }
    javac -encoding UTF-8 -cp "lib/*" -d bin src/*.java
}

java "-Dfile.encoding=UTF-8" -cp "bin;lib/*" MainApp

