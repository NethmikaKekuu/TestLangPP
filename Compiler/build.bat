@echo off
REM ============================================
REM TestLangPP Compiler - Build Script
REM Generates Lexer, Parser, and compiles all sources
REM ============================================

echo ========================================
echo TestLangPP Compiler - Build
echo ========================================
echo.

REM Step 1: Check required directories
echo [Step 1/6] Checking directories...
if not exist "lib\" (
    echo ERROR: lib directory not found!
    pause
    exit /b 1
)
if not exist "src\" (
    echo ERROR: src directory not found!
    pause
    exit /b 1
)
if not exist "output\" mkdir output
echo   ✓ Directories OK
echo.

REM Step 2: Clean previous builds
echo [Step 2/6] Cleaning previous builds...
if exist "src\Lexer.java" del "src\Lexer.java"
if exist "src\parser.java" del "src\parser.java"
if exist "src\sym.java" del "src\sym.java"
del /Q src\*.class 2>nul
del /Q src\AST\*.class 2>nul
del /Q output\*.class 2>nul
echo   ✓ Clean complete
echo.

REM Step 3: Generate Lexer from lexer.flex
echo [Step 3/6] Generating Lexer...
java -jar lib\jflex-full-1.9.1.jar -d src src\lexer.flex
if errorlevel 1 (
    echo   ✗ ERROR: Lexer generation failed!
    pause
    exit /b 1
)
echo   ✓ Lexer.java generated
echo.

REM Step 4: Generate Parser from parser.cup
echo [Step 4/6] Generating Parser...
java -jar lib\java-cup-11b.jar -destdir src -parser parser src\parser.cup
if errorlevel 1 (
    echo   ✗ ERROR: Parser generation failed!
    pause
    exit /b 1
)
echo   ✓ parser.java and sym.java generated
echo.

REM Step 5: Compile AST classes first
echo [Step 5/6] Compiling AST classes...
javac -cp "lib\java-cup-11b-runtime.jar" -d src src\AST\*.java
if errorlevel 1 (
    echo   ✗ ERROR: AST compilation failed!
    pause
    exit /b 1
)
echo   ✓ AST classes compiled
echo.

REM Step 6: Compile main classes
echo [Step 6/6] Compiling main classes...
javac -cp "src;lib\java-cup-11b-runtime.jar" -d src src\Lexer.java src\parser.java src\sym.java src\TestParser.java
if errorlevel 1 (
    echo   ✗ ERROR: Main classes compilation failed!
    pause
    exit /b 1
)
echo   ✓ All classes compiled
echo.

REM Verify build
echo ========================================
echo Verifying build...
if exist "src\Lexer.class" (
    if exist "src\parser.class" (
        if exist "src\TestParser.class" (
            echo ✓ BUILD SUCCESSFUL!
            echo ========================================
            echo.
            echo Ready to compile test files.
            echo.
            echo Usage:
            echo   compile.bat examples\given.test
            echo.
        ) else (
            echo ✗ ERROR: TestParser.class not found!
            pause
            exit /b 1
        )
    ) else (
        echo ✗ ERROR: parser.class not found!
        pause
        exit /b 1
    )
) else (
    echo ✗ ERROR: Lexer.class not found!
    pause
    exit /b 1
)

pause