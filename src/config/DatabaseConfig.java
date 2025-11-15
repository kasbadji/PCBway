package config;

/**
 * Configuration class for database settings
 */
public class DatabaseConfig {
    // MongoDB Configuration
    public static final String MONGODB_URI = "mongodb://127.0.0.1:27017/PcbWay?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.5.8";
    public static final String DATABASE_NAME = "PCBway";
    
    // Collection names
    public static final String USERS_COLLECTION = "users";
    public static final String PRODUCTS_COLLECTION = "products";
    public static final String ORDERS_COLLECTION = "orders";
    public static final String MODELS_COLLECTION = "models";
    
    // Connection settings
    public static final int CONNECTION_TIMEOUT_MS = 2000;
    public static final int SERVER_SELECTION_TIMEOUT_MS = 2000;
    
    // Application settings
    public static final boolean USE_DATABASE = true; // Set to false for in-memory storage
    public static final boolean CREATE_INDEXES_ON_STARTUP = true;
    public static final boolean LOG_DATABASE_OPERATIONS = true;
    
    /**
     * Get the full MongoDB connection string
     */
    public static String getMongoConnectionString() {
        return MONGODB_URI;
    }
    
    /**
     * Check if database usage is enabled
     */
    public static boolean isDatabaseEnabled() {
        return USE_DATABASE;
    }
}