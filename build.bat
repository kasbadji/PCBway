@echo off
echo =================================================
echo       PCBway Application Build Script
echo =================================================
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

echo Checking for MongoDB dependencies...
if exist "lib\*.jar" (
    echo MongoDB JAR files found - compiling with database support...
    javac -cp "lib/*" -d bin src/config/*.java src/database/*.java src/model/*.java src/service/*.java src/UI/*.java src/styles/*.java src/App.java
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo ✓ Compilation successful with MongoDB support!
        echo.
        echo To run: java -cp "bin;lib/*" App
    ) else (
        echo ✗ Compilation failed!
    )
) else (
    echo No MongoDB JAR files found - compiling without database support...
    javac -d bin src/config/*.java src/model/*.java src/service/UserService.java src/service/ProductService.java src/service/OrderService.java src/service/ModelService.java src/UI/*.java src/styles/*.java src/App.java
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo ✓ Compilation successful with in-memory storage!
        echo.
        echo To run: java -cp bin App
        echo.
        echo Note: To enable MongoDB support, run download-mongodb-deps.bat first
    ) else (
        echo ✗ Compilation failed!
    )
)

echo.
echo =================================================
pause