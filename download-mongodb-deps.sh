#!/bin/sh

echo "Downloading MongoDB Java Driver dependencies..."
echo

mkdir -p lib
cd lib

echo "Downloading MongoDB Driver Sync..."
wget -q https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/4.11.1/mongodb-driver-sync-4.11.1.jar

echo "Downloading MongoDB Driver Core..."
wget -q https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/4.11.1/mongodb-driver-core-4.11.1.jar

echo "Downloading BSON Library..."
wget -q https://repo1.maven.org/maven2/org/mongodb/bson/4.11.1/bson-4.11.1.jar

cd ..

echo
echo "MongoDB dependencies downloaded successfully!"
