#!/bin/bash
echo "================================================="
echo "        PCBway Application Runner"
echo "================================================="
echo ""

if [ ! -f "bin/App.class" ]; then
    echo "Application not compiled! Running build script first..."
    ./build.sh
    echo ""
fi

if ls lib/*.jar 1> /dev/null 2>&1; then
    echo "Running with MongoDB support..."
    java -cp "bin:lib/*" App
else
    echo "Running with in-memory storage..."
    java -cp bin App
fi

echo ""
echo "Application closed."
