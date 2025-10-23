@echo off
REM ============================================
REM TestLangPP Compiler - Full Build Script
REM Generates Lexer, Parser, and compiles all sources
REM Stores all .class files in ClassLib
REM ============================================

SETLOCAL

REM -------------------------------
REM Configuration Paths
REM -------------------------------
set LIB_DIR=lib
set SRC_DIR=src
set OUTPUT_DIR=output
set CLASS_DIR=ClassLib
set JFLEX_JAR=%LIB_DIR%\jflex-full-1.9.1.jar
set CUP_JAR=%LIB_DIR%\java-cup-11b.jar
set CUP_RUNTIME=%LIB_DIR%\java-cup-11b-runtime.jar

echo ============================================
echo TestLangPP Compiler - Full Build
echo ============================================
echo.

REM -------------------------------
REM Step 1: Check directories
REM -------------------------------
echo [Step 1/6] Checking directories...
if not exist "%LIB_DIR%\" (
    echo ERROR: Library directory "%LIB_DIR%" not found!
    pause
    exit /b 1
)
if not exist "%SRC_DIR%\" (
    echo ERROR: Source directory "%SRC_DIR%" not found!
    pause
    exit /b 1
)
if not exist "%CLASS_DIR%\" mkdir "%CLASS_DIR%"
if not exist "%CLASS_DIR%\AST\" mkdir "%CLASS_DIR%\AST"
if not exist "%OUTPUT_DIR%\" mkdir "%OUTPUT_DIR%"
echo Directories OK
echo.

REM -------------------------------
REM Step 2: Clean previous builds
REM -------------------------------
echo [Step 2/6] Cleaning previous builds...
if exist "%SRC_DIR%\Lexer.java" del "%SRC_DIR%\Lexer.java"
if exist "%SRC_DIR%\Parser.java" del "%SRC_DIR%\Parser.java"
if exist "%SRC_DIR%\sym.java" del "%SRC_DIR%\sym.java"
if exist "%CLASS_DIR%\*.class" del /Q "%CLASS_DIR%\*.class"
if exist "%CLASS_DIR%\AST\*.class" del /Q "%CLASS_DIR%\AST\*.class"
echo Clean complete
echo.

REM -------------------------------
REM Step 3: Generate Lexer
REM -------------------------------
echo [Step 3/6] Generating Lexer...
java -jar "%JFLEX_JAR%" -d "%SRC_DIR%" "%SRC_DIR%\lexer.flex"
if errorlevel 1 (
    echo ERROR: Lexer generation failed!
    pause
    exit /b 1
)
echo Lexer.java generated
echo.

REM -------------------------------
REM Step 4: Generate Parser
REM -------------------------------
echo [Step 4/6] Generating Parser...
java -jar "%CUP_JAR%" -destdir "%SRC_DIR%" -parser Parser -symbols sym "%SRC_DIR%\parser.cup"
if errorlevel 1 (
    echo ERROR: Parser generation failed!
    pause
    exit /b 1
)
echo Parser.java and sym.java generated
echo.

REM -------------------------------
REM Step 5: Compile AST classes
REM -------------------------------
echo [Step 5/6] Compiling AST classes...
javac -d "%CLASS_DIR%" -cp "%CUP_RUNTIME%" "%SRC_DIR%\AST\*.java"
if errorlevel 1 (
    echo ERROR: AST compilation failed!
    pause
    exit /b 1
)
echo AST classes compiled
echo.

REM -------------------------------
REM Step 6: Compile generated + main classes
REM -------------------------------
echo [Step 6/6] Compiling main classes...
javac -cp "%CLASS_DIR%;%CUP_RUNTIME%" -d "%CLASS_DIR%" "%SRC_DIR%\*.java"
if errorlevel 1 (
    echo ERROR: Main classes compilation failed!
    pause
    exit /b 1
)
echo All classes compiled
echo.

echo ============================================
echo BUILD SUCCESSFUL!
echo ============================================
echo.
echo To run the compiler:
echo   java -cp "%CLASS_DIR%;%CUP_RUNTIME%" TestParser examples\given.test
echo.

pause
ENDLOCAL