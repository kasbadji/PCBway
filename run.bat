@echo off
echo =================================================
echo        PCBway Application Runner
echo =================================================
echo.

if not exist "bin\App.class" (
    echo Application not compiled! Running build script first...
    call build.bat
    echo.
)

echo Starting PCBway Application...
echo.

if exist "lib\*.jar" (
    echo Running with MongoDB support...
    java -cp "bin;lib/*" App
) else (
    echo Running with in-memory storage...
    java -cp bin App
)

echo.
echo Application closed.
pause