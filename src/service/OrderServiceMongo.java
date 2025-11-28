package service;

import model.Order;
import model.CartItem;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class OrderServiceMongo {
    private static OrderServiceMongo instance;
    private List<Order> localOrders = new ArrayList<>();
    private boolean useDatabase = false;

    private OrderServiceMongo() {
        checkMongoConnection();
    }
    
    private void checkMongoConnection() {
        try {
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
            
            if (database != null) {
                useDatabase = true;
                System.out.println("✓ OrderServiceMongo initialized with MongoDB");
            }
        } catch (Exception e) {
            System.err.println("MongoDB not available for OrderService, using local storage");
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
        
        // Save to MongoDB if available
        if (useDatabase) {
            try {
                saveOrderToMongoDB(order);
                System.out.println("✓ Order saved to MongoDB: " + order.getOrderId());
            } catch (Exception e) {
                System.err.println("Failed to save to MongoDB, using local storage: " + e.getMessage());
                localOrders.add(order);
            }
        } else {
            localOrders.add(order);
            System.out.println("✓ Order saved locally: " + order.getOrderId());
        }
        
        return order;
    }
    
    private void saveOrderToMongoDB(Order order) throws Exception {
        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        
        Class<?> documentClass = Class.forName("org.bson.Document");
        Object orderDoc = documentClass.getConstructor().newInstance();
        
        // Add order fields
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "orderId", order.getOrderId());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "userEmail", order.getUserEmail());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "subtotal", order.getSubtotal());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "shipping", order.getShipping());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "total", order.getTotal());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "orderDate", order.getOrderDate().toString());
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "status", order.getStatus());
        
        // Add items
        List<Object> itemsDocs = new ArrayList<>();
        for (Order.OrderItem item : order.getItems()) {
            Object itemDoc = documentClass.getConstructor().newInstance();
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(itemDoc, "productName", item.getProductName());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(itemDoc, "price", item.getPrice());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(itemDoc, "quantity", item.getQuantity());
            itemsDocs.add(itemDoc);
        }
        documentClass.getMethod("put", Object.class, Object.class)
            .invoke(orderDoc, "items", itemsDocs);
        
        // Add payment info
        if (order.getPaymentInfo() != null) {
            Object paymentDoc = documentClass.getConstructor().newInstance();
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(paymentDoc, "cardHolderName", order.getPaymentInfo().getCardHolderName());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(paymentDoc, "cardType", order.getPaymentInfo().getCardType());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(paymentDoc, "lastFourDigits", order.getPaymentInfo().getLastFourDigits());
            documentClass.getMethod("put", Object.class, Object.class)
                .invoke(orderDoc, "paymentInfo", paymentDoc);
        }
        
        // Insert into MongoDB
        Boolean success = (Boolean) dbManagerClass.getMethod("insertDocument", String.class, Object.class)
            .invoke(dbManager, "orders", orderDoc);
            
        if (!success) {
            throw new Exception("MongoDB insertion failed");
        }
    }

    public List<Order> getOrdersByUser(String userEmail) {
        List<Order> userOrders = new ArrayList<>();
        
        if (useDatabase) {
            try {
                userOrders = getOrdersFromMongoDB(userEmail);
            } catch (Exception e) {
                System.err.println("Failed to fetch from MongoDB, using local orders: " + e.getMessage());
                userOrders = getLocalOrdersByUser(userEmail);
            }
        } else {
            userOrders = getLocalOrdersByUser(userEmail);
        }
        
        return userOrders;
    }
    
    private List<Order> getOrdersFromMongoDB(String userEmail) throws Exception {
        List<Order> orders = new ArrayList<>();
        
        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
        
        Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
        Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
            .invoke(database, "orders");
            
        Class<?> filtersClass = Class.forName("com.mongodb.client.model.Filters");
        Object filter = filtersClass.getMethod("eq", String.class, Object.class)
            .invoke(null, "userEmail", userEmail);
            
        Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
        Object cursor = mongoCollectionClass.getMethod("find", Class.forName("org.bson.conversions.Bson"))
            .invoke(collection, filter);
        
        Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
        Object iterator = findIterableClass.getMethod("iterator").invoke(cursor);
        
        Class<?> iteratorClass = Class.forName("com.mongodb.client.MongoCursor");
        while ((Boolean) iteratorClass.getMethod("hasNext").invoke(iterator)) {
            Object doc = iteratorClass.getMethod("next").invoke(iterator);
            Order order = convertDocumentToOrder(doc);
            if (order != null) {
                orders.add(order);
            }
        }
        
        return orders;
    }
    
    private Order convertDocumentToOrder(Object doc) {
        try {
            Class<?> documentClass = Class.forName("org.bson.Document");
            
            String orderId = (String) documentClass.getMethod("get", Object.class).invoke(doc, "orderId");
            String userEmail = (String) documentClass.getMethod("get", Object.class).invoke(doc, "userEmail");
            String status = (String) documentClass.getMethod("get", Object.class).invoke(doc, "status");
            
            // Get financial data
            Double subtotal = (Double) documentClass.getMethod("get", Object.class).invoke(doc, "subtotal");
            Double shipping = (Double) documentClass.getMethod("get", Object.class).invoke(doc, "shipping");
            Double total = (Double) documentClass.getMethod("get", Object.class).invoke(doc, "total");
            
            // Parse items array
            java.util.List<?> itemsArray = (java.util.List<?>) documentClass.getMethod("get", Object.class).invoke(doc, "items");
            java.util.List<CartItem> cartItems = new ArrayList<>();
            
            if (itemsArray != null) {
                for (Object itemObj : itemsArray) {
                    try {
                        String productName = (String) documentClass.getMethod("get", Object.class).invoke(itemObj, "productName");
                        Double price = (Double) documentClass.getMethod("get", Object.class).invoke(itemObj, "price");
                        Integer quantity = (Integer) documentClass.getMethod("get", Object.class).invoke(itemObj, "quantity");
                        
                        if (productName != null && price != null && quantity != null) {
                            // Create a minimal product for the cart item
                            model.Product product = new model.Product(productName, "", price, "");
                            CartItem cartItem = new CartItem(product, quantity);
                            cartItems.add(cartItem);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing order item: " + e.getMessage());
                    }
                }
            }
            
            // Create order with items
            Order order = new Order(userEmail, cartItems);
            if (orderId != null) order.setOrderId(orderId);
            if (status != null) order.setStatus(status);
            
            // Set financial data if available
            if (subtotal != null && shipping != null && total != null) {
                // Set financial data directly
                order.setSubtotal(subtotal);
                order.setShipping(shipping);
                order.setTotal(total);
            }
            
            // Parse payment info
            Object paymentInfoObj = documentClass.getMethod("get", Object.class).invoke(doc, "paymentInfo");
            if (paymentInfoObj != null) {
                try {
                    String cardHolderName = (String) documentClass.getMethod("get", Object.class).invoke(paymentInfoObj, "cardHolderName");
                    String cardType = (String) documentClass.getMethod("get", Object.class).invoke(paymentInfoObj, "cardType");
                    String lastFourDigits = (String) documentClass.getMethod("get", Object.class).invoke(paymentInfoObj, "lastFourDigits");
                    
                    if (cardHolderName != null && cardType != null && lastFourDigits != null) {
                        Order.PaymentInfo paymentInfo = new Order.PaymentInfo(cardHolderName, cardType, lastFourDigits);
                        order.setPaymentInfo(paymentInfo);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing payment info: " + e.getMessage());
                }
            }
            
            return order;
        } catch (Exception e) {
            System.err.println("Error converting document to order: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private List<Order> getLocalOrdersByUser(String userEmail) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : localOrders) {
            if (order.getUserEmail().equals(userEmail)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }

    public Order getOrderById(String orderId) {
        if (useDatabase) {
            try {
                return getOrderFromMongoDBById(orderId);
            } catch (Exception e) {
                System.err.println("Failed to fetch from MongoDB: " + e.getMessage());
            }
        }
        
        // Check local orders
        for (Order order : localOrders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }
    
    private Order getOrderFromMongoDBById(String orderId) throws Exception {
        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
        
        Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
        Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
            .invoke(database, "orders");
            
        Class<?> filtersClass = Class.forName("com.mongodb.client.model.Filters");
        Object filter = filtersClass.getMethod("eq", String.class, Object.class)
            .invoke(null, "orderId", orderId);
            
        Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
        Object findIterable = mongoCollectionClass.getMethod("find", Class.forName("org.bson.conversions.Bson"))
            .invoke(collection, filter);
        
        Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
        Object firstDoc = findIterableClass.getMethod("first").invoke(findIterable);
        
        return firstDoc != null ? convertDocumentToOrder(firstDoc) : null;
    }
    
    public List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        
        if (useDatabase) {
            try {
                // Get all orders from MongoDB
                Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
                Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
                Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);
                
                Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
                Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
                    .invoke(database, "orders");
                    
                Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
                Object cursor = mongoCollectionClass.getMethod("find").invoke(collection);
                
                Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
                Object iterator = findIterableClass.getMethod("iterator").invoke(cursor);
                
                Class<?> iteratorClass = Class.forName("com.mongodb.client.MongoCursor");
                while ((Boolean) iteratorClass.getMethod("hasNext").invoke(iterator)) {
                    Object doc = iteratorClass.getMethod("next").invoke(iterator);
                    Order order = convertDocumentToOrder(doc);
                    if (order != null) {
                        allOrders.add(order);
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch all orders from MongoDB: " + e.getMessage());
                allOrders.addAll(localOrders);
            }
        } else {
            allOrders.addAll(localOrders);
        }
        
        return allOrders;
    }
}
