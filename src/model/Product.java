package model;

public class Product {
    private String name; 
    private String imagePath;
    private double price;
    private String description;

    public Product(String name, String imagePath, double price, String description) {
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
        this.description = description;
    }


    public String getName() { return name; }
    public String getImagePath() { return imagePath; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
}
