package service;

import model.Product;
import java.util.ArrayList;

public class ProductService {
    private static ProductService instance;
    private ArrayList<Product> products = new ArrayList<>();

    public ProductService() {
    
        products.add(new Product("Solder Kit", "images/solder.png", 8.50, "Complete soldering kit"));
        
        
        products.add(new Product("Arduino Uno R3", "images/arduino-uno.jpg", 25.99, 
            "Microcontroller board based on ATmega328P chip. Perfect for beginners and prototyping."));
        
        products.add(new Product("Raspberry Pi 4 Model B", "images/raspberry-pi4.jpg", 75.00,
            "Single-board computer with ARM Cortex-A72 CPU, 4GB RAM. Ideal for IoT projects."));
        
        products.add(new Product("ESP32 Development Board", "images/esp32.jpg", 12.50,
            "WiFi & Bluetooth enabled microcontroller. Great for wireless projects and IoT."));
        

        products.add(new Product("LED Strip 5m RGB", "images/led-strip.jpg", 18.99,
            "Addressable RGB LED strip, 5 meters, 300 LEDs. Works with Arduino/Pi."));
        
        products.add(new Product("Breadboard Kit", "images/breadboard-kit.jpg", 15.50,
            "Complete breadboard kit with jumper wires and components for prototyping."));
        
        products.add(new Product("Servo Motor SG90", "images/servo-sg90.jpg", 8.99,
            "Micro servo motor, 180-degree rotation. Perfect for robotics projects."));
        
        products.add(new Product("Ultrasonic Sensor HC-SR04", "images/ultrasonic-hcsr04.jpg", 6.99,
            "Distance measurement sensor with 2-400cm range. Easy Arduino integration."));
        
        products.add(new Product("Resistor Kit 600pcs", "images/resistor-kit.jpg", 9.99,
            "Complete resistor kit with 20 different values from 10Ω to 1MΩ."));
        
        products.add(new Product("OLED Display 0.96\" I2C", "images/oled-display.jpg", 12.99,
            "Small OLED display, 128x64 pixels, I2C interface. Perfect for status displays."));
        
        products.add(new Product("DC Motor Driver L298N", "images/l298n-driver.jpg", 7.50,
            "Dual H-bridge motor driver. Control 2 DC motors or 1 stepper motor."));
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public ArrayList<Product> getAllProducts() {
        return products;
    }
    
    public ArrayList<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        
        ArrayList<Product> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(lowerQuery) ||
                product.getDescription().toLowerCase().contains(lowerQuery)) {
                results.add(product);
            }
        }
        
        return results;
    }
    
    public Product findProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}
