package service;

import model.Product;
import model.CartItem;
import java.util.*;

/**
 * Service for managing shopping cart operations
 */
public class CartService {
    private static CartService instance;
    private List<CartItem> cartItems;
    
    private CartService() {
        cartItems = new ArrayList<>();
    }
    
    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
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
    }
    
    /**
     * Remove product from cart
     */
    public void removeFromCart(Product product) {
        cartItems.removeIf(item -> item.getProduct().getName().equals(product.getName()));
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
        }
    }
    
    /**
     * Clear all items from cart
     */
    public void clearCart() {
        cartItems.clear();
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
}