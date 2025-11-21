package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private String userEmail;
    private List<OrderItem> items;
    private double subtotal;
    private double shipping;
    private double total;
    private Date orderDate;
    private String status;
    private PaymentInfo paymentInfo;

    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = new Date();
        this.status = "Pending";
        this.shipping = 4.0;
    }

    public Order(String userEmail, List<CartItem> cartItems) {
        this.userEmail = userEmail;
        this.items = new ArrayList<>();
        this.orderDate = new Date();
        this.status = "Pending";
        this.shipping = 4.0;
        
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity()
            );
            this.items.add(orderItem);
        }
        
        calculateTotals();
    }

    private void calculateTotals() {
        this.subtotal = 0;
        for (OrderItem item : items) {
            this.subtotal += item.getPrice() * item.getQuantity();
        }
        this.total = this.subtotal + this.shipping;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { 
        this.items = items;
        calculateTotals();
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getShipping() { return shipping; }
    public void setShipping(double shipping) { 
        this.shipping = shipping;
        this.total = this.subtotal + this.shipping;
    }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public PaymentInfo getPaymentInfo() { return paymentInfo; }
    public void setPaymentInfo(PaymentInfo paymentInfo) { this.paymentInfo = paymentInfo; }

    public static class OrderItem {
        private String productName;
        private double price;
        private int quantity;

        public OrderItem() {}

        public OrderItem(String productName, double price, int quantity) {
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class PaymentInfo {
        private String cardHolderName;
        private String cardType;
        private String lastFourDigits;

        public PaymentInfo() {}

        public PaymentInfo(String cardHolderName, String cardType, String lastFourDigits) {
            this.cardHolderName = cardHolderName;
            this.cardType = cardType;
            this.lastFourDigits = lastFourDigits;
        }

        public String getCardHolderName() { return cardHolderName; }
        public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

        public String getCardType() { return cardType; }
        public void setCardType(String cardType) { this.cardType = cardType; }

        public String getLastFourDigits() { return lastFourDigits; }
        public void setLastFourDigits(String lastFourDigits) { this.lastFourDigits = lastFourDigits; }
    }
}
