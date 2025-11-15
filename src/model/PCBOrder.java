package model;

import java.io.File;

/**
 * Represents a PCB printing order with all specifications
 */
public class PCBOrder {
    private File uploadedFile;
    private String fileName;
    private String material;
    private int layers;
    private int quantity;
    private double pricePerPiece;
    private double totalPrice;
    private String surfaceFinish;
    private String solderMask;
    
    
    public static final String[] MATERIALS = {
        "FR4 Standard", "FR4 High-TG", "Rogers 4003C", "Rogers 4350B", "Aluminum"
    };
    
    
    public static final int[] LAYER_OPTIONS = {2, 4, 6, 8, 10};
    
    public static final String[] SURFACE_FINISHES = {
        "HASL Lead-free", "ENIG", "OSP", "Immersion Silver", "Immersion Tin"
    };
    

    public static final String[] SOLDER_MASK_COLORS = {
        "Green", "Blue", "Red", "Black", "White", "Yellow"
    };
    
    public PCBOrder() {
        this.quantity = 5; 
        this.material = MATERIALS[0]; 
        this.layers = 2; 
        this.surfaceFinish = SURFACE_FINISHES[0];
        this.solderMask = SOLDER_MASK_COLORS[0]; 
        calculatePrice();
    }
    
    public void calculatePrice() {
        double basePrice = 5.0; 
        
        switch (material) {
            case "FR4 Standard":
                basePrice += 0.0;
                break;
            case "FR4 High-TG":
                basePrice += 2.0;
                break;
            case "Rogers 4003C":
                basePrice += 15.0;
                break;
            case "Rogers 4350B":
                basePrice += 12.0;
                break;
            case "Aluminum":
                basePrice += 8.0;
                break;
        }
        
        basePrice += (layers - 2) * 3.0; 
        

        switch (surfaceFinish) {
            case "HASL Lead-free":
                basePrice += 0.0;
                break;
            case "ENIG":
                basePrice += 4.0;
                break;
            case "OSP":
                basePrice += 1.0;
                break;
            case "Immersion Silver":
                basePrice += 3.0;
                break;
            case "Immersion Tin":
                basePrice += 2.5;
                break;
        }
        

        double discount = 1.0;
        if (quantity >= 100) discount = 0.7;
        else if (quantity >= 50) discount = 0.8;
        else if (quantity >= 20) discount = 0.9;
        else if (quantity >= 10) discount = 0.95;
        
        this.pricePerPiece = basePrice * discount;
        this.totalPrice = this.pricePerPiece * quantity;
    }
    

    public File getUploadedFile() { return uploadedFile; }
    public void setUploadedFile(File uploadedFile) { 
        this.uploadedFile = uploadedFile;
        this.fileName = uploadedFile != null ? uploadedFile.getName() : null;
    }
    
    public String getFileName() { return fileName; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { 
        this.material = material;
        calculatePrice();
    }
    
    public int getLayers() { return layers; }
    public void setLayers(int layers) { 
        this.layers = layers;
        calculatePrice();
    }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = Math.max(1, quantity);
        calculatePrice();
    }
    
    public double getPricePerPiece() { return pricePerPiece; }
    public double getTotalPrice() { return totalPrice; }
    
    public String getSurfaceFinish() { return surfaceFinish; }
    public void setSurfaceFinish(String surfaceFinish) { 
        this.surfaceFinish = surfaceFinish;
        calculatePrice();
    }
    
    public String getSolderMask() { return solderMask; }
    public void setSolderMask(String solderMask) { this.solderMask = solderMask; }
    
    public boolean isValid() {
        return uploadedFile != null && fileName != null;
    }
    
    public String getOrderSummary() {
        return String.format(
            "PCB Order Summary:\n" +
            "File: %s\n" +
            "Material: %s\n" +
            "Layers: %d\n" +
            "Surface Finish: %s\n" +
            "Solder Mask: %s\n" +
            "Quantity: %d pieces\n" +
            "Price per piece: $%.2f\n" +
            "Total Price: $%.2f",
            fileName, material, layers, surfaceFinish, solderMask, 
            quantity, pricePerPiece, totalPrice
        );
    }
}