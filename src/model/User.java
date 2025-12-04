package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private String fullname;
    private String address;
    private String phonenumber;
    private List<PaymentMethod> paymentMethods;

    public User() {
        this.email = "";
        this.password = "";
        this.fullname = "";
        this.address = "";
        this.phonenumber = "";
        this.paymentMethods = new ArrayList<>();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.fullname = "";
        this.address = "";
        this.phonenumber = "";
        this.paymentMethods = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    // Payment method management methods
    public void addPaymentMethod(PaymentMethod paymentMethod) {
        if (this.paymentMethods == null) {
            this.paymentMethods = new ArrayList<>();
        }
        // If this is the first card, make it default
        if (this.paymentMethods.isEmpty()) {
            paymentMethod.setDefault(true);
        }
        this.paymentMethods.add(paymentMethod);
    }

    public boolean removePaymentMethod(String paymentMethodId) {
        if (this.paymentMethods == null)
            return false;

        PaymentMethod toRemove = null;
        for (PaymentMethod pm : this.paymentMethods) {
            if (pm.getId().equals(paymentMethodId)) {
                toRemove = pm;
                break;
            }
        }

        if (toRemove != null) {
            boolean wasDefault = toRemove.isDefault();
            this.paymentMethods.remove(toRemove);

            // If we removed the default card and there are other cards, make the first one
            // default
            if (wasDefault && !this.paymentMethods.isEmpty()) {
                this.paymentMethods.get(0).setDefault(true);
            }
            return true;
        }
        return false;
    }

    public PaymentMethod getDefaultPaymentMethod() {
        if (this.paymentMethods == null)
            return null;

        for (PaymentMethod pm : this.paymentMethods) {
            if (pm.isDefault()) {
                return pm;
            }
        }
        return this.paymentMethods.isEmpty() ? null : this.paymentMethods.get(0);
    }

    public void setDefaultPaymentMethod(String paymentMethodId) {
        if (this.paymentMethods == null)
            return;

        // First, unset all defaults
        for (PaymentMethod pm : this.paymentMethods) {
            pm.setDefault(false);
        }

        // Then set the specified one as default
        for (PaymentMethod pm : this.paymentMethods) {
            if (pm.getId().equals(paymentMethodId)) {
                pm.setDefault(true);
                break;
            }
        }
    }

}
