@echo off
REM PROJECTEND - SPRING BOOT APPLICATION
REM Script chạy ứng dụng Spring Boot với auto-kill port cũ cho Windows

setlocal enabledelayedexpansion

echo 🚀 === PROJECTEND SPRING BOOT APPLICATION ===
echo 📅 Starting at %date% %time%
echo.

REM Check if running on Windows
echo ℹ️  Detected OS: Windows
echo.

REM Kill existing process on port 8080
echo ℹ️  Checking for existing processes on port 8080...

REM Find process using port 8080
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do (
    set EXISTING_PID=%%a
)

if defined EXISTING_PID (
    echo ⚠️  Found existing process on port 8080 ^(PID: !EXISTING_PID!^)
    echo ℹ️  Killing existing process...
    taskkill /PID !EXISTING_PID! /F >nul 2>&1
    timeout /t 2 /nobreak >nul
    echo ✅ Successfully killed existing process
) else (
    echo ✅ Port 8080 is available
)
echo.

REM Check Java installation
echo ℹ️  Checking Java installation...
java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Java is installed
    java -version 2>&1 | findstr "version"

    REM Check Java version (basic check)
    for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr "version"') do (
        set JAVA_VERSION_STRING=%%i
    )
    set JAVA_VERSION_STRING=!JAVA_VERSION_STRING:"=!

    REM Extract major version
    for /f "tokens=1 delims=." %%j in ("!JAVA_VERSION_STRING!") do (
        set JAVA_MAJOR=%%j
    )

    if !JAVA_MAJOR! geq 17 (
        echo ✅ Java version is compatible ^(17+^)
    ) else (
        echo ⚠️  Java version might be too old. Recommended: Java 17+
    )
) else (
    echo ❌ Java is not installed or not in PATH!
    echo ℹ️  Please install Java 17+ from: https://adoptium.net/
    echo ℹ️  Or from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)
echo.

REM Check Maven installation
echo ℹ️  Checking Maven installation...
mvn -version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Maven is installed
    mvn -version 2>&1 | findstr "Apache Maven"
) else (
    echo ❌ Maven is not installed or not in PATH!
    echo ℹ️  Please install Maven from: https://maven.apache.org/download.cgi
    echo ℹ️  Or use Maven Wrapper: mvnw.cmd instead of mvn
    pause
    exit /b 1
)
echo.

REM Check if pom.xml exists
if not exist "pom.xml" (
    echo ❌ pom.xml not found! Make sure you're in the project root directory.
    pause
    exit /b 1
)

echo ✅ All prerequisites are satisfied
echo.

echo ℹ️  === STEP 1: CLEANING PREVIOUS BUILD ===
call mvn clean -q

if %errorlevel% neq 0 (
    echo ❌ Maven clean failed!
    pause
    exit /b 1
)
echo ✅ Clean completed successfully
echo.

echo ℹ️  === STEP 2: COMPILING PROJECT ===
call mvn compile -q

if %errorlevel% neq 0 (
    echo ❌ Compilation failed! Please check your code for errors.
    pause
    exit /b 1
)
echo ✅ Compilation completed successfully
echo.

echo ℹ️  === STEP 3: STARTING SPRING BOOT APPLICATION ===
echo 🌐 Application will be available at: http://localhost:8080
echo 📱 Main pages:
echo    • Home: http://localhost:8080/
echo    • Cart: http://localhost:8080/giohang
echo    • Products: http://localhost:8080/sanpham
echo    • Login: http://localhost:8080/login
echo.
echo ⚠️  Press Ctrl+C to stop the application
echo.

REM Start the application
call mvn spring-boot:run
start http://localhost:8080

REM This will run when application stops
echo.
echo ℹ️  Application stopped.
echo ℹ️  Goodbye! 👋
pause
