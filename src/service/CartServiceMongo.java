package service;

import model.Product;
import model.CartItem;
import java.util.*;

/**
 * Service for managing shopping cart operations with MongoDB persistence
 */
public class CartServiceMongo {
    private static CartServiceMongo instance;
    private List<CartItem> cartItems;
    private boolean useDatabase = false;
    private String currentUserEmail = null;

    private CartServiceMongo() {
        cartItems = new ArrayList<>();
        checkMongoConnection();
    }

    private void checkMongoConnection() {
        try {
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);

            if (database != null) {
                useDatabase = true;
                System.out.println("✓ CartServiceMongo initialized with MongoDB");
            }
        } catch (Exception e) {
            System.err.println("MongoDB not available for CartService, using local storage");
            useDatabase = false;
        }
    }

    public static CartServiceMongo getInstance() {
        if (instance == null) {
            instance = new CartServiceMongo();
        }
        return instance;
    }

    /**
     * Set the current user for cart operations
     */
    public void setCurrentUser(String userEmail) {
        this.currentUserEmail = userEmail;
        loadCartFromMongo();
    }

    /**
     * Add product to cart or increase quantity if already exists
     */
    public void addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }

        // Check if product already in cart
        CartItem existingItem = findCartItem(product);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            cartItems.add(new CartItem(product, quantity));
        }

        // Save to MongoDB if available
        if (useDatabase && currentUserEmail != null) {
            try {
                saveCartToMongo();
                System.out.println("✓ Cart item saved to MongoDB");
            } catch (Exception e) {
                System.err.println("Failed to save cart to MongoDB: " + e.getMessage());
            }
        }
    }

    /**
     * Remove product from cart
     */
    public void removeFromCart(Product product) {
        cartItems.removeIf(item -> item.getProduct().getName().equals(product.getName()));

        if (useDatabase && currentUserEmail != null) {
            try {
                saveCartToMongo();
                System.out.println("✓ Cart updated in MongoDB");
            } catch (Exception e) {
                System.err.println("Failed to update cart in MongoDB: " + e.getMessage());
            }
        }
    }

    /**
     * Update quantity of product in cart
     */
    public void updateQuantity(Product product, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(product);
            return;
        }

        CartItem item = findCartItem(product);
        if (item != null) {
            item.setQuantity(newQuantity);

            if (useDatabase && currentUserEmail != null) {
                try {
                    saveCartToMongo();
                    System.out.println("✓ Cart updated in MongoDB");
                } catch (Exception e) {
                    System.err.println("Failed to update cart in MongoDB: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Clear all items from cart
     */
    public void clearCart() {
        cartItems.clear();

        if (useDatabase && currentUserEmail != null) {
            try {
                saveCartToMongo();
                System.out.println("✓ Cart cleared in MongoDB");
            } catch (Exception e) {
                System.err.println("Failed to clear cart in MongoDB: " + e.getMessage());
            }
        }
    }

    /**
     * Get all items in cart
     */
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    /**
     * Get total number of items in cart
     */
    public int getTotalItemCount() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    /**
     * Get total price of all items in cart
     */
    public double getTotalPrice() {
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    /**
     * Check if cart is empty
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Find cart item by product
     */
    private CartItem findCartItem(Product product) {
        return cartItems.stream()
                .filter(item -> item.getProduct().getName().equals(product.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get quantity of specific product in cart
     */
    public int getProductQuantity(Product product) {
        CartItem item = findCartItem(product);
        return item != null ? item.getQuantity() : 0;
    }

    /**
     * Save cart to MongoDB
     */
    private void saveCartToMongo() throws Exception {
        if (currentUserEmail == null) {
            throw new Exception("No user email set");
        }

        Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
        Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
        Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);

        Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
        Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
                .invoke(database, "carts");

        // First, delete existing cart for this user
        Class<?> filtersClass = Class.forName("com.mongodb.client.model.Filters");
        Object filter = filtersClass.getMethod("eq", String.class, Object.class)
                .invoke(null, "userEmail", currentUserEmail);

        Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
        mongoCollectionClass.getMethod("deleteMany", Class.forName("org.bson.conversions.Bson"))
                .invoke(collection, filter);

        // If cart is empty, we're done (just deleted the cart)
        if (cartItems.isEmpty()) {
            return;
        }

        // Create cart document
        Class<?> documentClass = Class.forName("org.bson.Document");
        Object cartDoc = documentClass.getConstructor().newInstance();

        documentClass.getMethod("put", Object.class, Object.class)
                .invoke(cartDoc, "userEmail", currentUserEmail);

        // Add items array
        List<Object> itemsDocs = new ArrayList<>();
        for (CartItem item : cartItems) {
            Object itemDoc = documentClass.getConstructor().newInstance();
            documentClass.getMethod("put", Object.class, Object.class)
                    .invoke(itemDoc, "productName", item.getProduct().getName());
            documentClass.getMethod("put", Object.class, Object.class)
                    .invoke(itemDoc, "imagePath", item.getProduct().getImagePath());
            documentClass.getMethod("put", Object.class, Object.class)
                    .invoke(itemDoc, "price", item.getProduct().getPrice());
            documentClass.getMethod("put", Object.class, Object.class)
                    .invoke(itemDoc, "description", item.getProduct().getDescription());
            documentClass.getMethod("put", Object.class, Object.class)
                    .invoke(itemDoc, "quantity", item.getQuantity());
            itemsDocs.add(itemDoc);
        }
        documentClass.getMethod("put", Object.class, Object.class)
                .invoke(cartDoc, "items", itemsDocs);

        // Insert into MongoDB
        mongoCollectionClass.getMethod("insertOne", Object.class)
                .invoke(collection, cartDoc);
    }

    /**
     * Load cart from MongoDB
     */
    private void loadCartFromMongo() {
        if (!useDatabase || currentUserEmail == null) {
            return;
        }

        try {
            Class<?> dbManagerClass = Class.forName("database.DatabaseManager");
            Object dbManager = dbManagerClass.getMethod("getInstance").invoke(null);
            Object database = dbManagerClass.getMethod("getDatabase").invoke(dbManager);

            Class<?> mongoDatabaseClass = Class.forName("com.mongodb.client.MongoDatabase");
            Object collection = mongoDatabaseClass.getMethod("getCollection", String.class)
                    .invoke(database, "carts");

            Class<?> filtersClass = Class.forName("com.mongodb.client.model.Filters");
            Object filter = filtersClass.getMethod("eq", String.class, Object.class)
                    .invoke(null, "userEmail", currentUserEmail);

            Class<?> mongoCollectionClass = Class.forName("com.mongodb.client.MongoCollection");
            Object findIterable = mongoCollectionClass.getMethod("find", Class.forName("org.bson.conversions.Bson"))
                    .invoke(collection, filter);

            Class<?> findIterableClass = Class.forName("com.mongodb.client.FindIterable");
            Object doc = findIterableClass.getMethod("first").invoke(findIterable);

            if (doc != null) {
                cartItems.clear();
                Class<?> documentClass = Class.forName("org.bson.Document");

                List<?> itemsArray = (List<?>) documentClass.getMethod("get", Object.class).invoke(doc, "items");
                if (itemsArray != null) {
                    for (Object itemObj : itemsArray) {
                        String productName = (String) documentClass.getMethod("get", Object.class).invoke(itemObj,
                                "productName");
                        String imagePath = (String) documentClass.getMethod("get", Object.class).invoke(itemObj,
                                "imagePath");
                        Double price = (Double) documentClass.getMethod("get", Object.class).invoke(itemObj, "price");
                        String description = (String) documentClass.getMethod("get", Object.class).invoke(itemObj,
                                "description");
                        Integer quantity = (Integer) documentClass.getMethod("get", Object.class).invoke(itemObj,
                                "quantity");

                        if (productName != null && price != null && quantity != null) {
                            Product product = new Product(productName, imagePath != null ? imagePath : "", price,
                                    description != null ? description : "");
                            cartItems.add(new CartItem(product, quantity));
                        }
                    }
                }
                System.out.println("✓ Cart loaded from MongoDB: " + cartItems.size() + " items");
            }
        } catch (Exception e) {
            System.err.println("Failed to load cart from MongoDB: " + e.getMessage());
        }
    }
}
