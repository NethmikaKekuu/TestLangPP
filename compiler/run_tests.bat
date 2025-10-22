@echo off
echo Compiling test file...
java -cp ".;java-cup-11b-runtime.jar" TestParser ..\given.test

echo.
echo Compiling generated tests...
javac -cp ".;junit-platform-console-standalone-1.9.3.jar" GeneratedTests.java

echo.
echo Running tests (make sure backend is running on port 8080)...
java -cp ".;java-cup-11b-runtime.jar;junit-platform-console-standalone-1.9.3.jar" org.junit.platform.console.ConsoleLauncher -c GeneratedTests

pause