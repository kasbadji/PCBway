package service;

import model.User;
import java.util.List;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService {
    private static UserService instance;
    private List<User> localUsers = new ArrayList<>();
    private User currentUser = null;
    private boolean useDatabase = false;

    private UserService() {
        checkMongoConnection();
        initializeDefaultUsers();
    }
    
    private void checkMongoConnection() {
        try {
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
            
            if (database != null) {
                useDatabase = true;
                System.out.println("✓ UserService initialized with MongoDB");
            }
        } catch (Exception e) {
            System.err.println("MongoDB not available for UserService, using local storage");
            useDatabase = false;
        }
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private void initializeDefaultUsers() {
        // Check if users already exist
        if (getUserByEmail("test@email.com") == null) {
            User testUser = new User("test@email.com", hashPassword("password"));
            testUser.setFullname("Test User");
            registerUser(testUser);
        }
    }

    // Legacy methods for compatibility
    public void register(String email, String password) {
        User user = new User(email, password);
        registerUser(user);
    }

    public boolean login(String email, String password) {
        User user = authenticateUser(email, password);
        return user != null;
    }

    public boolean registerUser(User user) {
        // Check if user already exists
        if (getUserByEmail(user.getEmail()) != null) {
            return false;
        }

        user.setPassword(hashPassword(user.getPassword()));
        
        if (useDatabase) {
            try {
                saveUserToMongoDB(user);
                System.out.println("✓ User registered in MongoDB: " + user.getEmail());
                return true;
            } catch (Exception e) {
                System.err.println("Failed to save user to MongoDB, using local storage: " + e.getMessage());
                localUsers.add(user);
                return true;
            }
        } else {
            localUsers.add(user);
            System.out.println("✓ User registered locally: " + user.getEmail());
            return true;
        }
    }
    
    private void saveUserToMongoDB(User user) throws Exception {
        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        
        Class<?> documentClass = Class.forName("org.bson.Document");
        Object userDoc = documentClass.getConstructor().newInstance();
        
        // Add user fields
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(userDoc, "email", user.getEmail());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(userDoc, "password", user.getPassword());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(userDoc, "fullname", user.getFullname());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(userDoc, "address", user.getAddress());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(userDoc, "phonenumber", user.getPhonenumber());
        
        // Insert into MongoDB
        Boolean success = (Boolean) dbManagerClass.getMethod("insertDocument", String.class, Object.class)
            .invoke(dbManager, "users", userDoc);
            
        if (!success) {
            throw new Exception("MongoDB insertion failed");
        }
    }

    public User authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && verifyPassword(password, user.getPassword())) {
            currentUser = user;
            System.out.println("✓ User authenticated: " + email);
            return user;
        }
        return null;
    }

    public User getUserByEmail(String email) {
        if (useDatabase) {
            try {
                User mongoUser = getUserFromMongoDB(email);
                if (mongoUser != null) {
                    return mongoUser;
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch user from MongoDB: " + e.getMessage());
            }
        }
        
        // Check local users
        for (User user : localUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    
    private User getUserFromMongoDB(String email) throws Exception {
        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
        
        // Use proper MongoDB API
        Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
        Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
            .invoke(database, "users");
            
        Class<?> filtersClass = Class.forName("com.mongodb.client.model.Filters");
        Object filter = filtersClass.getMethod("eq", String.class, Object.class)
            .invoke(null, "email", email);
            
        Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
        Object findIterable = mongoCollectionClass.getMethod("find", Class.forName("org.bson.conversions.Bson"))
            .invoke(collection, filter);
        
        Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
        Object firstDoc = findIterableClass.getMethod("first").invoke(findIterable);
        
        if (firstDoc != null) {
            return convertDocumentToUser(firstDoc);
        }
        return null;
    }
    
    private User convertDocumentToUser(Object doc) {
        try {
            Class<?> documentClass = Class.forName("org.bson.Document");
            
            String email = (String) documentClass.getMethod("get", Object.class).invoke(doc, "email");
            String password = (String) documentClass.getMethod("get", Object.class).invoke(doc, "password");
            String fullname = (String) documentClass.getMethod("get", Object.class).invoke(doc, "fullname");
            String address = (String) documentClass.getMethod("get", Object.class).invoke(doc, "address");
            String phonenumber = (String) documentClass.getMethod("get", Object.class).invoke(doc, "phonenumber");
            
            User user = new User(email, password);
            if (fullname != null) user.setFullname(fullname);
            if (address != null) user.setAddress(address);
            if (phonenumber != null) user.setPhonenumber(phonenumber);
            
            return user;
        } catch (Exception e) {
            System.err.println("Error converting document to user: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUser(User user) {
        if (useDatabase) {
            try {
                updateUserInMongoDB(user);
                System.out.println("✓ User updated in MongoDB: " + user.getEmail());
                return true;
            } catch (Exception e) {
                System.err.println("Failed to update user in MongoDB: " + e.getMessage());
                return updateUserLocally(user);
            }
        } else {
            return updateUserLocally(user);
        }
    }
    
    private void updateUserInMongoDB(User user) throws Exception {
        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
        
        Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
        Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
            .invoke(database, "users");
            
        Class<?> filtersClass = Class.forName("com.mongodb.client.model.Filters");
        Object filter = filtersClass.getMethod("eq", String.class, Object.class)
            .invoke(null, "email", user.getEmail());
            
        Class<?> documentClass = Class.forName("org.bson.Document");
        Object updateDoc = documentClass.getConstructor().newInstance();
        
        Object setDoc = documentClass.getConstructor().newInstance();
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(setDoc, "fullname", user.getFullname());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(setDoc, "address", user.getAddress());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(setDoc, "phonenumber", user.getPhonenumber());
            
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(updateDoc, "$set", setDoc);
        
        Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
        mongoCollectionClass.getMethod("updateOne", Class.forName("org.bson.conversions.Bson"), Class.forName("org.bson.conversions.Bson"))
            .invoke(collection, filter, updateDoc);
    }
    
    private boolean updateUserLocally(User updatedUser) {
        for (int i = 0; i < localUsers.size(); i++) {
            User user = localUsers.get(i);
            if (user.getEmail().equals(updatedUser.getEmail())) {
                localUsers.set(i, updatedUser);
                if (currentUser != null && currentUser.getEmail().equals(updatedUser.getEmail())) {
                    currentUser = updatedUser;
                }
                System.out.println("✓ User updated locally: " + updatedUser.getEmail());
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        
        if (useDatabase) {
            try {
                allUsers = getAllUsersFromMongoDB();
            } catch (Exception e) {
                System.err.println("Failed to fetch all users from MongoDB: " + e.getMessage());
                allUsers.addAll(localUsers);
            }
        } else {
            allUsers.addAll(localUsers);
        }
        
        return allUsers;
    }
    
    private List<User> getAllUsersFromMongoDB() throws Exception {
        List<User> users = new ArrayList<>();
        
        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
        
        Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
        Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
            .invoke(database, "users");
            
        Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
        Object cursor = mongoCollectionClass.getMethod("find").invoke(collection);
        
        Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
        Object iterator = findIterableClass.getMethod("iterator").invoke(cursor);
        
        Class<?> iteratorClass = Class.forName("com.mongodb.client.MongoCursor");
        while ((Boolean) iteratorClass.getMethod("hasNext").invoke(iterator)) {
            Object doc = iteratorClass.getMethod("next").invoke(iterator);
            User user = convertDocumentToUser(doc);
            if (user != null) {
                users.add(user);
            }
        }
        
        return users;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
        System.out.println("✓ User logged out");
    }

    // Legacy update methods for compatibility
    public boolean updateFullName(String newFullName) {
        if (currentUser == null) return false;
        
        if (newFullName != null && !newFullName.trim().isEmpty()) {
            currentUser.setFullname(newFullName.trim());
            return updateUser(currentUser);
        }
        return false;
    }

    public boolean updateAddress(String newAddress) {
        if (currentUser == null) return false;
        
        currentUser.setAddress(newAddress != null ? newAddress.trim() : "");
        return updateUser(currentUser);
    }

    public boolean updatePhoneNumber(String newPhoneNumber) {
        if (currentUser == null) return false;
        
        if (newPhoneNumber != null) {
            currentUser.setPhonenumber(newPhoneNumber.trim());
            return updateUser(currentUser);
        }
        return false;
    }

    public boolean updatePassword(String currentPassword, String newPassword) {
        if (currentUser == null) return false;
        
        if (!verifyPassword(currentPassword, currentUser.getPassword())) {
            return false; 
        }
        
        if (newPassword != null && newPassword.length() >= 6) {
            currentUser.setPassword(hashPassword(newPassword));
            return updateUser(currentUser);
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password; // Fallback to plain text
        }
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        return hashPassword(inputPassword).equals(storedPassword);
    }
}
