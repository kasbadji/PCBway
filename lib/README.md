# MongoDB Integration Instructions for PCBway

## Required Dependencies

To use MongoDB with this project, you need to download the following JAR files and place them in the `lib/` directory:

### 1. MongoDB Java Driver
Download from: https://mvnrepository.com/artifact/org.mongodb/mongodb-driver-sync
- mongodb-driver-sync-4.11.1.jar
- mongodb-driver-core-4.11.1.jar
- bson-4.11.1.jar

### 2. Alternative: Use Maven or Gradle

#### Maven (pom.xml):
```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.11.1</version>
</dependency>
```

#### Gradle (build.gradle):
```gradle
implementation 'org.mongodb:mongodb-driver-sync:4.11.1'
```

## Compilation Instructions

### With JAR files in lib/:
```bash
javac -cp "lib/*" -d bin src/database/*.java src/model/*.java src/service/*.java src/UI/*.java src/styles/*.java src/App.java
```

### Running:
```bash
java -cp "bin;lib/*" App
```

## Connection String
The project is configured to connect to:
```
mongodb://127.0.0.1:27017/Jobme?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.5.8
```

Database name: `PCBway`

## Collections Used:
- users
- products  
- orders
- models