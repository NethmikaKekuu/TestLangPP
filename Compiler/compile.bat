@echo off
REM ============================================
REM TestLangPP Compiler - Compile and Run Tests
REM ============================================

if "%1"=="" (
    echo Usage: compile.bat ^<test-file^>
    echo.
    echo Examples:
    echo   compile.bat examples\given.test
    echo   compile.bat mytests\api.test
    echo.
    pause
    exit /b 1
)

echo ========================================
echo TestLangPP - Compile Test File
echo ========================================
echo.
echo Input: %1
echo.

REM Check if TestParser is compiled
if not exist "src\TestParser.class" (
    echo TestParser not found. Building first...
    echo.
    call build.bat
    if errorlevel 1 (
        echo.
        echo ✗ Build failed! Cannot continue.
        pause
        exit /b 1
    )
    echo.
)

REM Check if input file exists
if not exist "%1" (
    echo ✗ ERROR: Input file not found: %1
    pause
    exit /b 1
)

REM Run the compiler
echo ========================================
echo [1/3] Parsing and generating code...
echo ========================================
echo.
java -cp "src;lib\java-cup-11b-runtime.jar" TestParser %1
if errorlevel 1 (
    echo.
    echo ✗ ERROR: Compilation failed!
    echo Check your test file syntax.
    pause
    exit /b 1
)

echo.
echo ========================================
echo [2/3] Compiling generated tests...
echo ========================================
echo.

REM Check if output was generated
if not exist "output\GeneratedTests.java" (
    echo ✗ ERROR: GeneratedTests.java was not created!
    pause
    exit /b 1
)

REM Compile the generated tests
javac -cp "lib\junit-platform-console-standalone-1.9.3.jar" -d output output\GeneratedTests.java
if errorlevel 1 (
    echo.
    echo ✗ ERROR: Failed to compile generated tests!
    pause
    exit /b 1
)
echo ✓ Generated tests compiled successfully
echo.

echo ========================================
echo [3/3] Running tests...
echo ========================================
echo.
echo NOTE: Make sure your API server is running!
echo       Default: http://localhost:8080
echo ----------------------------------------
echo.

REM Run the tests
java -jar lib\junit-platform-console-standalone-1.9.3.jar ^
     --class-path output ^
     --scan-classpath ^
     --disable-banner

echo.
echo ========================================
echo Test execution complete!
echo ========================================
echo.
echo Generated test file: output\GeneratedTests.java
echo.
pause