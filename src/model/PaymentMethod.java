package model;

public class PaymentMethod {
    private String id;
    private String cardHolderName;
    private String cardType; // Visa, MasterCard, Amex, etc.
    private String lastFourDigits;
    private String expiryMonth;
    private String expiryYear;
    private boolean isDefault;

    public PaymentMethod() {
        this.isDefault = false;
    }

    public PaymentMethod(String cardHolderName, String cardType, String lastFourDigits,
            String expiryMonth, String expiryYear) {
        this.id = generateId();
        this.cardHolderName = cardHolderName;
        this.cardType = cardType;
        this.lastFourDigits = lastFourDigits;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.isDefault = false;
    }

    private String generateId() {
        return "PM_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getMaskedCardNumber() {
        return "**** **** **** " + lastFourDigits;
    }

    public String getDisplayName() {
        return getMaskedCardNumber() + " - " + cardType;
    }

    @Override
    public String toString() {
        return getDisplayName() + " (Exp: " + expiryMonth + "/" + expiryYear + ")";
    }
}
