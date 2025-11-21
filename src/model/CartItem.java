package model;


public class CartItem {
    private Product product;
    private int quantity;
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = Math.max(1, quantity); // Minimum quantity is 1
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }
    
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
    
    public void incrementQuantity() {
        quantity++;
    }
    
    public void decrementQuantity() {
        if (quantity > 1) {
            quantity--;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CartItem cartItem = (CartItem) obj;
        return product.getName().equals(cartItem.product.getName());
    }
    
    @Override
    public int hashCode() {
        return product.getName().hashCode();
    }
}