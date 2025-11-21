package service;

import model.User;

/**
 * User Service with MongoDB integration capability
 * Currently uses in-memory storage, can be switched to MongoDB when JAR files are available
 */
public class UserServiceMongo extends UserService {
    private Object dbManager; // Using Object to avoid compile-time dependency
    private boolean useDatabase = false; // Will be set to true when MongoDB is available
    
    public UserServiceMongo() {
        super();
        checkMongoAvailability();
    }
    
    /**
     * Check if MongoDB libraries are available and initialize connection
     */
    private void checkMongoAvailability() {
        try {
            // Check if MongoDB classes are available
            Class.forName("com.mongodb.client.MongoClient");
            Class.forName("org.bson.Document");
            Class.forName("database.DatabaseManager");
            
            // If we reach here, MongoDB is available - initialize using reflection
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            
            // Test connection
            boolean connected = (Boolean) dbManager.getClass().getMethod("isMongoAvailable").invoke(dbManager);
            if (connected) {
                useDatabase = true;
                System.out.println("Connected to MongoDB for user management");
            } else {
                useDatabase = false;
                System.out.println("MongoDB connection failed, using in-memory storage");
            }
        } catch (Exception e) {
            System.out.println("MongoDB libraries not available, using in-memory storage");
            useDatabase = false;
        }
    }

    @Override
    public void register(String email, String password) {
        if (useDatabase) {
            registerToDatabase(email, password);
        } else {
            super.register(email, password); // Call parent class method
        }
    }
    
    /**
     * Register user to MongoDB database
     */
    private void registerToDatabase(String email, String password) {
        try {
            Object usersCollection = dbManager.getClass().getMethod("getUsersCollection").invoke(dbManager);
            if (usersCollection == null) {
                // Fallback to in-memory
                super.register(email, password);
                return;
            }
            
            Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
            Class<?> documentClass = Class.forName("org.bson.Document");
            Class<?> bsonClass = Class.forName("org.bson.conversions.Bson");
            Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
            
            // Check if user already exists using MongoCollection interface
            Object emailFilter = documentClass.getConstructor(String.class, Object.class).newInstance("email", email);
            Object findResult = mongoCollectionClass.getMethod("find", bsonClass).invoke(usersCollection, emailFilter);
            Object existingUser = findIterableClass.getMethod("first").invoke(findResult);
            
            if (existingUser != null) {
                System.out.println("User with email " + email + " already exists");
                return;
            }
            
            // Create new user document
            Object userDoc = documentClass.getConstructor(String.class, Object.class).newInstance("email", email);
            userDoc.getClass().getMethod("append", String.class, Object.class).invoke(userDoc, "password", password);
            userDoc.getClass().getMethod("append", String.class, Object.class).invoke(userDoc, "fullname", "");
            userDoc.getClass().getMethod("append", String.class, Object.class).invoke(userDoc, "address", "");
            userDoc.getClass().getMethod("append", String.class, Object.class).invoke(userDoc, "phonenumber", "");
            userDoc.getClass().getMethod("append", String.class, Object.class).invoke(userDoc, "createdAt", new java.util.Date());
            
            // Insert user using MongoCollection interface
            mongoCollectionClass.getMethod("insertOne", Object.class).invoke(usersCollection, userDoc);
            System.out.println("User registered successfully in database: " + email);
        } catch (Exception e) {
            System.err.println("Error registering user in database: " + e.getMessage());
            // Fallback to in-memory
            super.register(email, password);
        }
    }

    @Override
    public boolean login(String email, String password) {
        if (useDatabase) {
            return loginFromDatabase(email, password);
        } else {
            return super.login(email, password); // Call parent class method
        }
    }
    
    /**
     * Login using MongoDB database
     */
    private boolean loginFromDatabase(String email, String password) {
        try {
            Object usersCollection = dbManager.getClass().getMethod("getUsersCollection").invoke(dbManager);
            if (usersCollection == null) {
                return super.login(email, password); // Fallback to in-memory
            }
            
            Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
            Class<?> documentClass = Class.forName("org.bson.Document");
            Class<?> bsonClass = Class.forName("org.bson.conversions.Bson");
            Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
            
            // Create a simple query document with email and password
            Object query = documentClass.getConstructor().newInstance();
            query.getClass().getMethod("put", String.class, Object.class).invoke(query, "email", email);
            query.getClass().getMethod("put", String.class, Object.class).invoke(query, "password", password);
            
            // Find user using MongoCollection interface
            Object findResult = mongoCollectionClass.getMethod("find", bsonClass).invoke(usersCollection, query);
            Object userDoc = findIterableClass.getMethod("first").invoke(findResult);
            
            if (userDoc != null) {
                // Convert Document to User object using reflection to access currentUser
                User user = new User(email, password);
                
                try {
                    String fullname = (String) userDoc.getClass().getMethod("get", String.class).invoke(userDoc, "fullname");
                    String address = (String) userDoc.getClass().getMethod("get", String.class).invoke(userDoc, "address");
                    String phonenumber = (String) userDoc.getClass().getMethod("get", String.class).invoke(userDoc, "phonenumber");
                    
                    if (fullname != null && !fullname.isEmpty()) user.setFullname(fullname);
                    if (address != null && !address.isEmpty()) user.setAddress(address);
                    if (phonenumber != null && !phonenumber.isEmpty()) user.setPhonenumber(phonenumber);
                } catch (Exception e) {
                    // If field access fails, just use basic user info
                    System.out.println("Note: Could not load user profile fields, using basic login");
                }
                
                // Use reflection to set currentUser in parent class
                setCurrentUser(user);
                System.out.println("User logged in successfully: " + email);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error during database login: " + e.getMessage());
            // Fallback to in-memory
            return super.login(email, password);
        }
    }
    
    /**
     * Set current user using reflection (since currentUser field is private in parent)
     */
    private void setCurrentUser(User user) {
        try {
            java.lang.reflect.Field field = UserService.class.getDeclaredField("currentUser");
            field.setAccessible(true);
            field.set(this, user);
        } catch (Exception e) {
            // If reflection fails, fallback to login which sets the current user
            super.login(user.getEmail(), user.getPassword());
        }
    }
    
    /**
     * Get current user using reflection (since currentUser field is private in parent)
     */
    private User getCurrentUserInternal() {
        try {
            java.lang.reflect.Field field = UserService.class.getDeclaredField("currentUser");
            field.setAccessible(true);
            return (User) field.get(this);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public boolean updateFullName(String newFullName) {
        boolean success = super.updateFullName(newFullName);
        
        if (success && useDatabase) {
            updateUserInDatabase("fullname", newFullName.trim());
        }
        
        return success;
    }

    @Override
    public boolean updateAddress(String newAddress) {
        boolean success = super.updateAddress(newAddress);
        
        if (success && useDatabase) {
            String address = newAddress != null ? newAddress.trim() : "";
            updateUserInDatabase("address", address);
        }
        
        return success;
    }

    @Override
    public boolean updatePhoneNumber(String newPhoneNumber) {
        boolean success = super.updatePhoneNumber(newPhoneNumber);
        
        if (success && useDatabase) {
            String phone = newPhoneNumber != null ? newPhoneNumber.trim() : "";
            updateUserInDatabase("phonenumber", phone);
        }
        
        return success;
    }
    
    @Override
    public boolean updatePassword(String currentPassword, String newPassword) {
        boolean success = super.updatePassword(currentPassword, newPassword);
        
        if (success && useDatabase) {
            updateUserInDatabase("password", newPassword);
        }
        
        return success;
    }
    
    /**
     * Update user field in MongoDB database
     */
    private boolean updateUserInDatabase(String field, String value) {
        try {
            Object usersCollection = dbManager.getClass().getMethod("getUsersCollection").invoke(dbManager);
            if (usersCollection == null) return false;
            
            User currentUser = getCurrentUserInternal();
            if (currentUser == null) return false;
            
            Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
            Class<?> documentClass = Class.forName("org.bson.Document");
            Class<?> bsonClass = Class.forName("org.bson.conversions.Bson");
            
            // Create filter for current user using simple Document
            Object emailFilter = documentClass.getConstructor(String.class, Object.class).newInstance("email", currentUser.getEmail());
            
            // Create update document
            Object setDoc = documentClass.getConstructor(String.class, Object.class).newInstance(field, value);
            Object updateDoc = documentClass.getConstructor(String.class, Object.class).newInstance("$set", setDoc);
            
            // Update user using MongoCollection interface
            mongoCollectionClass.getMethod("updateOne", bsonClass, bsonClass).invoke(usersCollection, emailFilter, updateDoc);
            System.out.println("User " + field + " updated successfully in database");
            return true;
        } catch (Exception e) {
            System.err.println("Error updating user in database: " + e.getMessage());
            return false;
        }
    }
}