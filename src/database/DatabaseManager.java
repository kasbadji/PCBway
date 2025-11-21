package database;


public class DatabaseManager {
    private static DatabaseManager instance;
    private Object mongoClient; // Using Object to avoid compile-time dependency
    private Object database;
    private boolean mongoAvailable = false;
    
    // MongoDB connection string
    private static final String MONGODB_URI = "mongodb://127.0.0.1:27017/PcbWay?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.5.8";
    private static final String DATABASE_NAME = "PCBway";
    
    private DatabaseManager() {
        checkMongoAvailability();
        if (mongoAvailable) {
            connect();
        }
    }
    
    /**
     * Check if MongoDB classes are available in classpath
     */
    private void checkMongoAvailability() {
        try {
            Class.forName("com.mongodb.client.MongoClient");
            Class.forName("org.bson.Document");
            mongoAvailable = true;
            System.out.println("MongoDB libraries detected - database support enabled");
        } catch (ClassNotFoundException e) {
            mongoAvailable = false;
            System.out.println("MongoDB libraries not found - using in-memory storage");
        }
    }
    
    /**
     * Get singleton instance of DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Connect to MongoDB (only if libraries are available)
     */
    private void connect() {
        if (!mongoAvailable) {
            System.out.println("Cannot connect - MongoDB libraries not available");
            return;
        }
        
        try {
            // Use reflection to create MongoDB connection to avoid compile-time dependencies
            Class<?> mongoClientsClass = Class.forName("com.mongodb.client.MongoClients");
            Class<?> mongoClientSettingsClass = Class.forName("com.mongodb.MongoClientSettings");
            Class<?> connectionStringClass = Class.forName("com.mongodb.ConnectionString");
            
            // Create connection string
            Object connectionString = connectionStringClass.getConstructor(String.class).newInstance(MONGODB_URI);
            
            // Create client settings
            Object settingsBuilder = mongoClientSettingsClass.getMethod("builder").invoke(null);
            settingsBuilder = settingsBuilder.getClass().getMethod("applyConnectionString", connectionStringClass).invoke(settingsBuilder, connectionString);
            Object settings = settingsBuilder.getClass().getMethod("build").invoke(settingsBuilder);
            
            // Create client
            mongoClient = mongoClientsClass.getMethod("create", mongoClientSettingsClass).invoke(null, settings);
            
            // Get database
            database = mongoClient.getClass().getMethod("getDatabase", String.class).invoke(mongoClient, DATABASE_NAME);
            
            System.out.println("Successfully connected to MongoDB database: " + DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            mongoAvailable = false;
            e.printStackTrace();
        }
    }
    
    /**
     * Get the database instance
     */
    public Object getDatabase() {
        return database;
    }
    
    /**
     * Check if MongoDB is available and connected
     */
    public boolean isMongoAvailable() {
        return mongoAvailable && database != null;
    }
    
    /**
     * Get users collection
     */
    public Object getUsersCollection() {
        if (!isMongoAvailable()) return null;
        try {
            return database.getClass().getMethod("getCollection", String.class).invoke(database, "users");
        } catch (Exception e) {
            System.err.println("Error getting users collection: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get products collection
     */
    public Object getProductsCollection() {
        if (!isMongoAvailable()) return null;
        try {
            return database.getClass().getMethod("getCollection", String.class).invoke(database, "products");
        } catch (Exception e) {
            System.err.println("Error getting products collection: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get orders collection
     */
    public Object getOrdersCollection() {
        if (!isMongoAvailable()) return null;
        try {
            return database.getClass().getMethod("getCollection", String.class).invoke(database, "orders");
        } catch (Exception e) {
            System.err.println("Error getting orders collection: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get models collection
     */
    public Object getModelsCollection() {
        if (!isMongoAvailable()) return null;
        try {
            return database.getClass().getMethod("getCollection", String.class).invoke(database, "models");
        } catch (Exception e) {
            System.err.println("Error getting models collection: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Test the connection
     */
    public boolean testConnection() {
        if (!isMongoAvailable()) return false;
        
        try {
            // Create a simple ping document using reflection
            Class<?> documentClass = Class.forName("org.bson.Document");
            Object pingDoc = documentClass.getConstructor(String.class, Object.class).newInstance("ping", 1);
            
            // Run ping command
            Object result = database.getClass().getMethod("runCommand", documentClass).invoke(database, pingDoc);
            System.out.println("✓ MongoDB ping successful: " + result);
            return true;
        } catch (Exception e) {
            System.err.println("Connection test failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            
            // Try a simpler test - just list collection names
            try {
                database.getClass().getMethod("listCollectionNames").invoke(database);
                System.out.println("✓ MongoDB connection verified via collection listing");
                return true;
            } catch (Exception e2) {
                System.err.println("Alternative connection test also failed: " + e2.getMessage());
                return false;
            }
        }
    }
    
    /**
     * Close the connection
     */
    public void close() {
        if (mongoClient != null) {
            try {
                mongoClient.getClass().getMethod("close").invoke(mongoClient);
                System.out.println("MongoDB connection closed");
            } catch (Exception e) {
                System.err.println("Error closing MongoDB connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Create indexes for better performance
     */
    public void createIndexes() {
        if (!isMongoAvailable()) {
            System.out.println("MongoDB not available - cannot create indexes");
            return;
        }
        
        try {
            Class<?> indexesClass = Class.forName("com.mongodb.client.model.Indexes");
            
            // Create index on users email field using Indexes.ascending
            Object emailIndex = indexesClass.getMethod("ascending", String.class).invoke(null, "email");
            Object usersCollection = getUsersCollection();
            if (usersCollection != null) {
                try {
                    usersCollection.getClass().getMethod("createIndex", Object.class).invoke(usersCollection, emailIndex);
                    System.out.println("✓ Created email index on users collection");
                } catch (Exception e) {
                    System.out.println("Note: Users email index may already exist");
                }
            }
            
            // Create index on products name field  
            Object nameIndex = indexesClass.getMethod("ascending", String.class).invoke(null, "name");
            Object productsCollection = getProductsCollection();
            if (productsCollection != null) {
                try {
                    productsCollection.getClass().getMethod("createIndex", Object.class).invoke(productsCollection, nameIndex);
                    System.out.println("✓ Created name index on products collection");
                } catch (Exception e) {
                    System.out.println("Note: Products name index may already exist");
                }
            }
            
            System.out.println("Database indexes creation completed");
        } catch (Exception e) {
            System.out.println("Note: Index creation skipped - " + e.getMessage());
        }
    }
}