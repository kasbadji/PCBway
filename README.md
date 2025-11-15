# PCBway - Electronics & PCB Ordering Platform

A Java Swing application for electronics purchasing and PCB printing services with optional MongoDB database integration.

## Features

- **User Authentication**: Login/Signup system with profile management
- **Electronics Store**: Browse and purchase electronic components
- **Shopping Cart**: Add/remove items with quantity management
- **PCB Printing**: Upload PCB files, configure specifications, and order custom PCBs
- **Database Support**: Optional MongoDB integration for data persistence
- **Modern UI**: Rounded buttons and clean interface design

## Prerequisites

- **Java 8 or higher** installed on your system
- **Command prompt or PowerShell** (Windows)
- **MongoDB** (optional - for database features)

## Quick Start

### Option 1: Run with Pre-built Scripts (Recommended)

1. **Clone or download** the project
2. **Navigate** to the project directory:
   ```cmd
   cd PCBway
   ```
3. **Run the application**:
   ```cmd
   run.bat
   ```

The script will automatically:
- Build the project if needed
- Start the application with appropriate database support
- Handle both MongoDB and in-memory storage modes

### Option 2: Manual Build and Run

#### For In-Memory Storage (No Database)
```cmd
# Compile the application
javac -d bin src/model/*.java src/service/UserService.java src/service/ProductService.java src/service/CartService.java src/UI/*.java src/styles/*.java src/database/*.java src/config/*.java src/App.java

# Run the application
java -cp bin App
```

#### For MongoDB Database Support
```cmd
# First, ensure MongoDB dependencies are available
download-mongodb-deps.bat

# Compile with MongoDB support
javac -cp "lib/*" -d bin src/config/*.java src/database/*.java src/model/*.java src/service/*.java src/UI/*.java src/styles/*.java src/App.java

# Run with MongoDB support
java -cp "bin;lib/*" App
```

## Project Structure

```
PCBway/
├── src/                          # Source code
│   ├── UI/                      # User interface components
│   │   ├── App.java            # Main application entry point
│   │   ├── LoginFrame.java     # Login screen
│   │   ├── SignupFrame.java    # Registration screen
│   │   ├── ServicesFrame.java  # Main services page
│   │   ├── ElectronicsFrame.java # Electronics store
│   │   ├── CartFrame.java      # Shopping cart
│   │   ├── PcbPrinting.java    # PCB ordering interface
│   │   └── ContactFrame.java   # Contact page
│   ├── model/                   # Data models
│   │   ├── User.java           # User data model
│   │   ├── Product.java        # Product data model
│   │   ├── CartItem.java       # Shopping cart item
│   │   └── PCBOrder.java       # PCB order specifications
│   ├── service/                 # Business logic
│   │   ├── UserService.java    # User management (in-memory)
│   │   ├── UserServiceMongo.java # User management (MongoDB)
│   │   ├── ProductService.java  # Product catalog management
│   │   └── CartService.java     # Shopping cart operations
│   ├── styles/                  # UI styling components
│   │   ├── RoundedButton.java   # Custom rounded button component
│   │   └── RoundedBorder.java   # Custom rounded border styling
│   ├── database/               # Database connectivity
│   │   └── DatabaseManager.java # MongoDB connection manager
│   └── config/                 # Configuration settings
│       └── DatabaseConfig.java # Database configuration
├── lib/                        # External dependencies
│   ├── mongodb-driver-sync-4.11.1.jar
│   ├── mongodb-driver-core-4.11.1.jar
│   └── bson-4.11.1.jar
├── images/                     # Application images
├── bin/                        # Compiled output (auto-generated)
├── build.bat                   # Build script
├── run.bat                     # Run script
└── download-mongodb-deps.bat   # MongoDB dependency downloader
```

## Database Setup (Optional)

### For MongoDB Integration:

1. **Install MongoDB** on your system
2. **Start MongoDB** service:
   ```cmd
   mongod
   ```
3. **Run the application** with `run.bat` - it will automatically detect and use MongoDB

### Default Database Configuration:
- **Connection**: `mongodb://127.0.0.1:27017/PCBway`
- **Database Name**: `PCBway`
- **Collections**: `users`, `products`, `orders`

**Note**: If MongoDB is not available, the application will automatically fall back to in-memory storage.

## Usage

1. **Start Application**: Run `run.bat` or compile and run manually
2. **Create Account**: Click "Sign up here" on the login screen
3. **Browse Products**: Navigate to Electronics section
4. **Add to Cart**: Select products and quantities
5. **Order PCBs**: Use PCB Printing to upload files and configure specifications
6. **Checkout**: Review cart and complete orders

## Features Overview

### Authentication System
- User registration and login
- Profile management (name, address, phone)
- Password security
- Session management

### Electronics Store
- Product catalog with 14+ electronic components
- Search functionality
- Add to cart with quantity selection
- Price display and calculations

### Shopping Cart
- View all selected items
- Modify quantities (+/- buttons)
- Remove individual items
- Clear entire cart
- Total price calculation

### PCB Printing Service
- File upload for PCB designs
- Material selection (FR4, Rogers, Aluminum)
- Layer configuration (2-10 layers)
- Surface finish options (HASL, ENIG, OSP, etc.)
- Quantity-based pricing with discounts
- Order summary and cart integration

## Development Notes

### Building from Source
The project uses standard Java compilation with optional MongoDB dependencies. The build system automatically detects available libraries and compiles accordingly.

### Dependencies
- **Core**: Java Swing (built-in)
- **Database**: MongoDB Java Driver 4.11.1 (optional)
- **UI**: Custom rounded components

### Architecture
- **MVC Pattern**: Separation of UI, business logic, and data models
- **Service Layer**: Abstracted business operations
- **Database Abstraction**: Supports both MongoDB and in-memory storage
- **Reflection-based Loading**: MongoDB features load dynamically

## Troubleshooting

### Common Issues:

1. **Compilation Fails**:
   ```cmd
   # Ensure Java is installed
   java -version
   
   # Try manual compilation step by step
   build.bat
   ```

2. **MongoDB Connection Issues**:
   ```cmd
   # Check if MongoDB is running
   mongo --eval "db.runCommand('ping')"
   
   # Verify connection string in config/DatabaseConfig.java
   ```

3. **UI Not Displaying Correctly**:
   - Ensure you're running on a system with GUI support
   - Try running with `java -Djava.awt.headless=false -cp bin App`

4. **File Upload Issues**:
   - Ensure the application has read permissions for selected files
   - Check that file formats are supported (.gbr files for PCB)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly (both with and without MongoDB)
5. Submit a pull request

## License

This project is part of an educational assignment for GUI development with Java Swing.

---

**Note**: This application demonstrates modern Java Swing development with optional database integration and clean UI design principles.
