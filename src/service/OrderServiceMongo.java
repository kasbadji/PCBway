package service;

import model.Order;
import model.CartItem;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class OrderServiceMongo {
    private static OrderServiceMongo instance;
    private List<Order> localOrders = new ArrayList<>();

    private OrderServiceMongo() {
        System.out.println("✓ OrderServiceMongo initialized (local storage)");
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
        
        // Save locally
        localOrders.add(order);
        System.out.println("✓ Order saved locally: " + order.getOrderId());
        
        return order;
    }

    public List<Order> getOrdersByUser(String userEmail) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : localOrders) {
            if (order.getUserEmail().equals(userEmail)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }

    public Order getOrderById(String orderId) {
        for (Order order : localOrders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(localOrders);
    }
}
