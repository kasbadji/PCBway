#!/bin/bash
echo "================================================="
echo "        Building PCBway Application"
echo "================================================="
echo ""

# Create bin directory if it doesn't exist
mkdir -p bin

# Check if MongoDB JARs are available
if ls lib/*.jar 1> /dev/null 2>&1; then
    echo "Building with MongoDB support..."
    javac -cp "lib/*" -d bin \
        src/config/*.java \
        src/database/*.java \
        src/model/*.java \
        src/service/*.java \
        src/UI/*.java \
        src/styles/*.java \
        src/App.java
    
    if [ $? -eq 0 ]; then
        echo "\u2713 Build successful with MongoDB support"
    else
        echo "\u2717 Build failed"
        exit 1
    fi
else
    echo "Building with in-memory storage (no MongoDB JARs found)..."
    javac -d bin \
        src/config/*.java \
        src/model/*.java \
        src/service/UserService.java \
        src/service/ProductService.java \
        src/service/OrderService.java \
        src/service/ModelService.java \
        src/UI/*.java \
        src/styles/*.java \
        src/App.java
    
    if [ $? -eq 0 ]; then
        echo "\u2713 Build successful with in-memory storage"
    else
        echo "\u2717 Build failed"
        exit 1
    fi
fi
