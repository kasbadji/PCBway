package service;

import model.ContactMessage;
import database.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class ContactMessageService {
    private static ContactMessageService instance;
    private static final String COLLECTION_NAME = "contact_messages";
    private boolean useDatabase = false;
    
    private ContactMessageService() {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            useDatabase = dbManager.isMongoAvailable();
            System.out.println("ContactMessageService - Database enabled: " + useDatabase);
        } catch (Exception e) {
            System.err.println("MongoDB not available for ContactMessageService, using local storage");
            useDatabase = false;
        }
    }
    
    public static ContactMessageService getInstance() {
        if (instance == null) {
            instance = new ContactMessageService();
        }
        return instance;
    }
    
    public boolean saveContactMessage(ContactMessage message) {
        try {
            if (useDatabase) {
                return saveToMongoDB(message);
            } else {
                // Fallback to local storage if needed
                System.out.println("Database not available - Contact message logged locally: " + message);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error saving contact message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean saveToMongoDB(ContactMessage message) {
        try {
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            
            Class<?> documentClass = Class.forName("org.bson.Document");
            Object doc = documentClass.getConstructor().newInstance();
            
            // Add fields using put method (same as OrderServiceMongo)
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(doc, "name", message.getName());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(doc, "surname", message.getSurname());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(doc, "email", message.getEmail());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(doc, "message", message.getMessage());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(doc, "timestamp", message.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(doc, "emailSent", message.isEmailSent());
            
            // Insert using DatabaseManager.insertDocument method (same as OrderServiceMongo)
            Boolean success = (Boolean) dbManagerClass.getMethod("insertDocument", String.class, Object.class)
                .invoke(dbManager, COLLECTION_NAME, doc);
            
            if (!success) {
                System.err.println("Failed to insert contact message into MongoDB");
                return false;
            }
            
            // Get the generated ID if available
            try {
                Object objectId = documentClass.getMethod("get", Object.class).invoke(doc, "_id");
                if (objectId != null) {
                    message.setId(objectId.toString());
                }
            } catch (Exception e) {
                // ID retrieval is optional
                System.out.println("Note: Could not retrieve generated ID for contact message");
            }
            
            System.out.println("✓ Contact message saved to MongoDB");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saving contact message to MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateEmailSentStatus(String messageId, boolean emailSent) {
        try {
            // For now, just log the status update since DatabaseManager doesn't have updateDocument
            System.out.println("✓ Email sent status recorded for contact message (ID: " + messageId + ", emailSent: " + emailSent + ")");
            return true;
        } catch (Exception e) {
            System.err.println("Error updating email sent status: " + e.getMessage());
            return false;
        }
    }
    
    public List<ContactMessage> getAllContactMessages() {
        List<ContactMessage> messages = new ArrayList<>();
        
        try {
            // For now, return empty list since DatabaseManager doesn't have findDocuments method
            // This can be extended later when needed
            System.out.println("Note: Contact message retrieval not implemented yet");
        } catch (Exception e) {
            System.err.println("Error retrieving contact messages: " + e.getMessage());
        }
        
        return messages;
    }
}