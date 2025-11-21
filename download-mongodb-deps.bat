@echo off
echo Downloading MongoDB Java Driver dependencies...
echo.

REM Create lib directory if it doesn't exist
if not exist "lib" mkdir lib

cd lib

echo Downloading MongoDB Driver Sync...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/4.11.1/mongodb-driver-sync-4.11.1.jar' -OutFile 'mongodb-driver-sync-4.11.1.jar'"

echo Downloading MongoDB Driver Core...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/4.11.1/mongodb-driver-core-4.11.1.jar' -OutFile 'mongodb-driver-core-4.11.1.jar'"

echo Downloading BSON Library...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mongodb/bson/4.11.1/bson-4.11.1.jar' -OutFile 'bson-4.11.1.jar'"

cd ..

echo.
echo MongoDB dependencies downloaded successfully!
echo.
echo To compile with MongoDB support:
echo javac -cp "lib/*" -d bin src/database/*.java src/model/*.java src/service/*.java src/UI/*.java src/styles/*.java src/App.java
echo.
echo To run with MongoDB support:
echo java -cp "bin;lib/*" App
echo.
pause