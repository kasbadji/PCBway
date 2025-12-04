package UI;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import styles.RoundedButton;
import service.OrderServiceMongo;
import service.UserService;
import model.Order;
import model.User;
import model.PaymentMethod;

public class ProfileFrame extends JFrame {
    private User currentUser;
    private OrderServiceMongo orderService;
    private UserService userService;
    private DecimalFormat priceFormat;
    private JTextField nameField, emailField, phoneField, addressField;
    private JPanel orderHistoryPanel;
    private JPanel savedCardsPanel;

    public ProfileFrame(User user) {
        this.currentUser = user;
        this.orderService = OrderServiceMongo.getInstance();
        this.userService = UserService.getInstance();
        this.priceFormat = new DecimalFormat("$#,##0.00");

        initializeFrame();
        createLayout();
        loadUserData();
        refreshOrderHistory();
    }

    private void initializeFrame() {
        setTitle("User Profile");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
    }

    private void createLayout() {
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        // Personal Information Tab
        JPanel personalInfoPanel = createPersonalInfoPanel();
        tabbedPane.addTab("Personal Information", personalInfoPanel);

        // Order History Tab
        JPanel orderHistoryTab = createOrderHistoryPanel();
        tabbedPane.addTab("Order History", orderHistoryTab);

        // Billing & Payment Tab
        JPanel billingPanel = createBillingPanel();
        tabbedPane.addTab("Billing & Payment", billingPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Back button
        RoundedButton backButton = new RoundedButton("← Back to Shop", 10);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(new Color(34, 139, 34));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.addActionListener(e -> {
            new ElectronicsFrame().setVisible(true);
            dispose();
        });

        // Profile title
        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("Personal Information");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 139, 34));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = createStyledTextField();
        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = createStyledTextField();
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        panel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        phoneField = createStyledTextField();
        panel.add(phoneField, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(addressLabel, gbc);

        gbc.gridx = 1;
        addressField = createStyledTextField();
        addressField.setPreferredSize(new Dimension(300, 80));
        panel.add(addressField, gbc);

        // Save button
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        RoundedButton saveButton = new RoundedButton("Save Changes", 10);
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setForeground(Color.WHITE);
        saveButton.setPreferredSize(new Dimension(150, 45));
        saveButton.addActionListener(e -> saveUserData());
        panel.add(saveButton, gbc);

        return panel;
    }

    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Order History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 139, 34));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Orders container
        orderHistoryPanel = new JPanel();
        orderHistoryPanel.setLayout(new BoxLayout(orderHistoryPanel, BoxLayout.Y_AXIS));
        orderHistoryPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(orderHistoryPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBillingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("Billing & Payment");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 139, 34));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Saved Cards Section
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel cardsLabel = new JLabel("Saved Payment Methods:");
        cardsLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(cardsLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel cardsPanel = createSavedCardsPanel();
        panel.add(cardsPanel, gbc);

        gbc.gridwidth = 1;

        // Add New Card Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        RoundedButton addCardButton = new RoundedButton("+ Add New Card", 10);
        addCardButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        addCardButton.setBackground(new Color(34, 139, 34));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.setPreferredSize(new Dimension(180, 45));
        addCardButton.addActionListener(e -> addNewCard());
        panel.add(addCardButton, gbc);

        return panel;
    }

    private JPanel createSavedCardsPanel() {
        savedCardsPanel = new JPanel();
        savedCardsPanel.setLayout(new BoxLayout(savedCardsPanel, BoxLayout.Y_AXIS));
        savedCardsPanel.setBackground(Color.WHITE);
        savedCardsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        savedCardsPanel.setPreferredSize(new Dimension(500, 200));

        refreshSavedCards();

        return savedCardsPanel;
    }

    private void refreshSavedCards() {
        savedCardsPanel.removeAll();

        if (currentUser == null || currentUser.getPaymentMethods() == null
                || currentUser.getPaymentMethods().isEmpty()) {
            JLabel noCardsLabel = new JLabel("No payment methods saved");
            noCardsLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            noCardsLabel.setForeground(Color.GRAY);
            noCardsLabel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
            savedCardsPanel.add(noCardsLabel);
        } else {
            for (PaymentMethod paymentMethod : currentUser.getPaymentMethods()) {
                JPanel cardPanel = new JPanel(new BorderLayout());
                cardPanel.setBackground(Color.WHITE);
                cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                // Card info with default indicator
                String cardText = paymentMethod.getDisplayName();
                if (paymentMethod.isDefault()) {
                    cardText += " (Default)";
                }
                JLabel cardLabel = new JLabel(cardText);
                cardLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

                // Button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                buttonPanel.setBackground(Color.WHITE);

                // Set Default button (only if not already default)
                if (!paymentMethod.isDefault() && currentUser.getPaymentMethods().size() > 1) {
                    JButton setDefaultButton = new JButton("Set Default");
                    setDefaultButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    setDefaultButton.setBackground(new Color(34, 139, 34));
                    setDefaultButton.setForeground(Color.WHITE);
                    setDefaultButton.setPreferredSize(new Dimension(100, 30));
                    setDefaultButton.addActionListener(e -> setDefaultCard(paymentMethod.getId()));
                    buttonPanel.add(setDefaultButton);
                }

                // Remove button
                JButton removeButton = new JButton("Remove");
                removeButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
                removeButton.setBackground(new Color(220, 53, 69));
                removeButton.setForeground(Color.WHITE);
                removeButton.setPreferredSize(new Dimension(80, 30));
                removeButton.addActionListener(e -> removeCard(paymentMethod.getId()));
                buttonPanel.add(removeButton);

                cardPanel.add(cardLabel, BorderLayout.CENTER);
                cardPanel.add(buttonPanel, BorderLayout.EAST);
                savedCardsPanel.add(cardPanel);
            }
        }

        savedCardsPanel.revalidate();
        savedCardsPanel.repaint();
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setPreferredSize(new Dimension(300, 40));
        return field;
    }

    private void loadUserData() {
        if (currentUser != null) {
            nameField.setText(currentUser.getFullname() != null ? currentUser.getFullname() : "");
            emailField.setText(currentUser.getEmail());
            // Load additional user data
            phoneField.setText(currentUser.getPhonenumber() != null ? currentUser.getPhonenumber() : "");
            addressField.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        }
    }

    private void saveUserData() {
        if (currentUser != null) {
            try {
                // Get the updated values
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();

                // Basic validation
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Name cannot be empty!",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Update the current user object
                currentUser.setFullname(name);
                currentUser.setPhonenumber(phone);
                currentUser.setAddress(address);

                // Save to database using UserService
                boolean success = userService.updateUser(currentUser);

                // Update the current user in UserService to keep session in sync
                if (success && userService.getCurrentUser() != null &&
                        userService.getCurrentUser().getEmail().equals(currentUser.getEmail())) {
                    // The UserService.updateUser method should already handle this,
                    // but we ensure the current session reflects the changes
                    System.out.println("✓ Profile updated in database and session synchronized");
                }

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Profile updated successfully and saved to database!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Profile updated locally but failed to save to database.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error updating profile: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void refreshOrderHistory() {
        orderHistoryPanel.removeAll();

        if (currentUser != null) {
            List<Order> orders = orderService.getOrdersByUser(currentUser.getEmail());

            if (orders.isEmpty()) {
                JLabel noOrdersLabel = new JLabel("No orders found");
                noOrdersLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
                noOrdersLabel.setForeground(Color.GRAY);
                noOrdersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                orderHistoryPanel.add(Box.createRigidArea(new Dimension(0, 50)));
                orderHistoryPanel.add(noOrdersLabel);
            } else {
                for (Order order : orders) {
                    JPanel orderPanel = createOrderPanel(order);
                    orderHistoryPanel.add(orderPanel);
                    orderHistoryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        }

        orderHistoryPanel.revalidate();
        orderHistoryPanel.repaint();
    }

    private JPanel createOrderPanel(Order order) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Left: Order info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel orderIdLabel = new JLabel("Order #" + order.getOrderId().substring(0, 8));
        orderIdLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        orderIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("Date: " + order.getOrderDate().toString());
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel("Status: " + order.getStatus());
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(new Color(34, 139, 34));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(orderIdLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);

        // Right: Total and actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);

        JLabel totalLabel = new JLabel("Total: " + priceFormat.format(order.getTotal()));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalLabel.setForeground(new Color(34, 139, 34));

        RoundedButton viewButton = new RoundedButton("View Details", 8);
        viewButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        viewButton.setBackground(new Color(34, 139, 34));
        viewButton.setForeground(Color.WHITE);
        viewButton.setPreferredSize(new Dimension(100, 35));
        viewButton.addActionListener(e -> viewOrderDetails(order));

        rightPanel.add(totalLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rightPanel.add(viewButton);

        panel.add(infoPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void viewOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(order.getOrderId()).append("\n");
        details.append("Date: ").append(order.getOrderDate().toString()).append("\n");
        details.append("Status: ").append(order.getStatus()).append("\n\n");
        details.append("Items:\n");

        for (Order.OrderItem item : order.getItems()) {
            details.append("- ").append(item.getProductName())
                    .append(" x").append(item.getQuantity())
                    .append(" = ").append(priceFormat.format(item.getPrice() * item.getQuantity()))
                    .append("\n");
        }

        details.append("\nSubtotal: ").append(priceFormat.format(order.getSubtotal()));
        details.append("\nShipping: ").append(priceFormat.format(order.getShipping()));
        details.append("\nTotal: ").append(priceFormat.format(order.getTotal()));

        if (order.getPaymentInfo() != null) {
            details.append("\n\nPayment: ").append(order.getPaymentInfo().getCardType())
                    .append(" ending in ").append(order.getPaymentInfo().getLastFourDigits());
        }

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addNewCard() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Card Holder Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Card Holder Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Card Number (we'll only store last 4 digits)
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        JTextField cardNumberField = new JTextField(20);
        panel.add(cardNumberField, gbc);

        // Card Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Card Type:"), gbc);
        gbc.gridx = 1;
        String[] cardTypes = { "Visa", "MasterCard", "American Express", "Discover" };
        JComboBox<String> cardTypeCombo = new JComboBox<>(cardTypes);
        panel.add(cardTypeCombo, gbc);

        // Expiry Month
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Expiry Month (MM):"), gbc);
        gbc.gridx = 1;
        JTextField expiryMonthField = new JTextField(5);
        panel.add(expiryMonthField, gbc);

        // Expiry Year
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Expiry Year (YY):"), gbc);
        gbc.gridx = 1;
        JTextField expiryYearField = new JTextField(5);
        panel.add(expiryYearField, gbc);

        // CVV (for validation, not stored)
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        JPasswordField cvvField = new JPasswordField(5);
        panel.add(cvvField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Payment Method",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String cardHolderName = nameField.getText().trim();
                String cardNumber = cardNumberField.getText().trim().replaceAll("\\s+", "");
                String cardType = (String) cardTypeCombo.getSelectedItem();
                String expiryMonth = expiryMonthField.getText().trim();
                String expiryYear = expiryYearField.getText().trim();
                String cvv = new String(cvvField.getPassword()).trim();

                // Validation
                if (cardHolderName.isEmpty() || cardNumber.isEmpty() || expiryMonth.isEmpty() ||
                        expiryYear.isEmpty() || cvv.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (cardNumber.length() < 13 || cardNumber.length() > 19) {
                    JOptionPane.showMessageDialog(this, "Invalid card number!",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!expiryMonth.matches("^(0[1-9]|1[0-2])$")) {
                    JOptionPane.showMessageDialog(this, "Invalid month! Use format: 01-12",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!expiryYear.matches("^\\d{2}$")) {
                    JOptionPane.showMessageDialog(this, "Invalid year! Use 2-digit format (e.g., 25)",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!cvv.matches("^\\d{3,4}$")) {
                    JOptionPane.showMessageDialog(this, "Invalid CVV!",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Extract last 4 digits
                String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);

                // Create payment method
                PaymentMethod paymentMethod = new PaymentMethod(cardHolderName, cardType,
                        lastFourDigits, expiryMonth, expiryYear);

                // Add to user
                currentUser.addPaymentMethod(paymentMethod);

                // Save to database
                boolean success = userService.updateUser(currentUser);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment method added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshSavedCards();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save payment method.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding payment method: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void removeCard(String paymentMethodId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this payment method?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = currentUser.removePaymentMethod(paymentMethodId);

            if (removed) {
                boolean success = userService.updateUser(currentUser);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment method removed successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshSavedCards();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save changes.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void setDefaultCard(String paymentMethodId) {
        currentUser.setDefaultPaymentMethod(paymentMethodId);

        boolean success = userService.updateUser(currentUser);

        if (success) {
            JOptionPane.showMessageDialog(this, "Default payment method updated!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshSavedCards();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update default payment method.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
