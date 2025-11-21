package service;

import model.Order;
import model.CartItem;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class OrderServiceMongo {
    private static OrderServiceMongo instance;
    private Object ordersCollection;
    private boolean useDatabase = false;

    private OrderServiceMongo() {
        checkMongoAvailability();
    }
    
    private void checkMongoAvailability() {
        try {
            Class.forName("com.mongodb.client.MongoClient");
            Class.forName("org.bson.Document");
            
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
            
            if (database != null) {
                ordersCollection = database.getClass().getMethod("getCollection", String.class)
                    .invoke(database, "orders");
                useDatabase = true;
                System.out.println("✓ OrderServiceMongo initialized with MongoDB");
            }
        } catch (Exception e) {
            System.err.println("MongoDB not available for OrderService, using in-memory storage");
            useDatabase = false;
        }
    }

    public static OrderServiceMongo getInstance() {
        if (instance == null) {
            instance = new OrderServiceMongo();
        }
        return instance;
    }

    public Order createOrder(String userEmail, List<CartItem> cartItems, 
                            String cardHolderName, String cardType, String cardNumber) {
        Order order = new Order(userEmail, cartItems);
        order.setOrderId(UUID.randomUUID().toString());
        
        String lastFour = cardNumber.length() >= 4 
            ? cardNumber.substring(cardNumber.length() - 4) 
            : cardNumber;
        
        Order.PaymentInfo paymentInfo = new Order.PaymentInfo(
            cardHolderName, 
            cardType, 
            lastFour
        );
        order.setPaymentInfo(paymentInfo);
        order.setStatus("Confirmed");
        
        if (useDatabase && ordersCollection != null) {
            try {
                Class<?> documentClass = Class.forName("org.bson.Document");
                
                Object orderDoc = documentClass.getConstructor(String.class, Object.class)
                    .newInstance("orderId", order.getOrderId());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "userEmail", order.getUserEmail());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "subtotal", order.getSubtotal());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "shipping", order.getShipping());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "total", order.getTotal());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "orderDate", order.getOrderDate());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "status", order.getStatus());
                
                List<Object> itemsDocs = new ArrayList<>();
                for (Order.OrderItem item : order.getItems()) {
                    Object itemDoc = documentClass.getConstructor(String.class, Object.class)
                        .newInstance("productName", item.getProductName());
                    documentClass.getMethod("append", String.class, Object.class)
                        .invoke(itemDoc, "price", item.getPrice());
                    documentClass.getMethod("append", String.class, Object.class)
                        .invoke(itemDoc, "quantity", item.getQuantity());
                    itemsDocs.add(itemDoc);
                }
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "items", itemsDocs);
                
                Object paymentDoc = documentClass.getConstructor(String.class, Object.class)
                    .newInstance("cardHolderName", paymentInfo.getCardHolderName());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(paymentDoc, "cardType", paymentInfo.getCardType());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(paymentDoc, "lastFourDigits", paymentInfo.getLastFourDigits());
                documentClass.getMethod("append", String.class, Object.class)
                    .invoke(orderDoc, "paymentInfo", paymentDoc);
                
                ordersCollection.getClass().getMethod("insertOne", Object.class)
                    .invoke(ordersCollection, orderDoc);
                System.out.println("✓ Order saved to MongoDB: " + order.getOrderId());
            } catch (Exception e) {
                System.err.println("Failed to save order to MongoDB: " + e.getMessage());
            }
        }
        
        return order;
    }

    public List<Order> getOrdersByUser(String userEmail) {
        return new ArrayList<>();
    }

    public Order getOrderById(String orderId) {
        return null;
    }
}
