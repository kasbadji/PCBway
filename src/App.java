import UI.LoginFrame;
import config.DatabaseConfig;
import service.UserService;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        // Display startup information
        System.out.println("=== PCBway Application Starting ===");
        System.out.println("MongoDB URI: " + DatabaseConfig.getMongoConnectionString());
        System.out.println("Database: " + DatabaseConfig.DATABASE_NAME);
        System.out.println("Database Enabled: " + DatabaseConfig.isDatabaseEnabled());
        
        // Attempt to test database connection (will work when MongoDB JARs are available)
        try {
            // Use reflection to avoid compile-time dependency on DatabaseManager
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            
            Boolean connected = (Boolean) dbManager.getClass().getMethod("testConnection").invoke(dbManager);
            if (connected != null && connected) {
                System.out.println("✓ MongoDB connection successful");
                if (DatabaseConfig.CREATE_INDEXES_ON_STARTUP) {
                    dbManager.getClass().getMethod("createIndexes").invoke(dbManager);
                }
            } else {
                System.out.println("✗ MongoDB connection failed - using in-memory storage");
            }
            System.out.println("Note: To enable MongoDB, add JAR files to lib/ directory");
        } catch (Exception e) {
            System.out.println("MongoDB not available: " + e.getMessage());
            System.out.println("Running with in-memory storage");
        }
        
        System.out.println("====================================");
        
        // Test database operations
        testDatabaseOperations();
        
        // Start the GUI application
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    
    /**
     * Test database operations to verify MongoDB is working
     */
    private static void testDatabaseOperations() {
        System.out.println("=== Testing Database Operations ===");
        try {
            UserService userService = UserService.getInstance();
            
            // Test user registration
            System.out.println("Testing user registration...");
            userService.register("test@pcbway.com", "password123");
            System.out.println("✓ User registration completed");
            
            // Test user login
            System.out.println("Testing user login...");
            boolean loggedIn = userService.login("test@pcbway.com", "password123");
            if (loggedIn) {
                System.out.println("✓ User login successful");
                // Get current user
                try {
                    Object currentUser = userService.getCurrentUser();
                    if (currentUser != null) {
                        System.out.println("Current user logged in successfully");
                    }
                } catch (Exception e) {
                    System.out.println("User is logged in (unable to get user details)");
                }
            } else {
                System.out.println("✗ User login failed");
            }
            
        } catch (Exception e) {
            System.err.println("Database test error: " + e.getMessage());
        }
        System.out.println("====================================");
    }
}
