package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import styles.RoundedButton;
import service.OrderServiceMongo;
import service.UserService;
import model.Order;
import model.User;

public class ProfileFrame extends JFrame {
    private User currentUser;
    private OrderServiceMongo orderService;
    private UserService userService;
    private DecimalFormat priceFormat;
    private JTextField nameField, emailField, phoneField, addressField;
    private JPanel orderHistoryPanel;
    
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
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        nameField = createStyledTextField();
        panel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        emailField = createStyledTextField();
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        panel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        phoneField = createStyledTextField();
        panel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(addressLabel, gbc);
        
        gbc.gridx = 1;
        addressField = createStyledTextField();
        addressField.setPreferredSize(new Dimension(300, 80));
        panel.add(addressField, gbc);
        
        // Save button
        gbc.gridx = 1; gbc.gridy = 5;
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
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Saved Cards Section
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel cardsLabel = new JLabel("Saved Payment Methods:");
        cardsLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(cardsLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel cardsPanel = createSavedCardsPanel();
        panel.add(cardsPanel, gbc);
        
        gbc.gridwidth = 1;
        
        // Add New Card Button
        gbc.gridx = 0; gbc.gridy = 3;
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setPreferredSize(new Dimension(500, 200));
        
        // Sample saved cards
        String[] cards = {"**** **** **** 1234 - Visa", "**** **** **** 5678 - MasterCard"};
        for (String card : cards) {
            JPanel cardPanel = new JPanel(new BorderLayout());
            cardPanel.setBackground(Color.WHITE);
            cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JLabel cardLabel = new JLabel(card);
            cardLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            JButton removeButton = new JButton("Remove");
            removeButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            removeButton.setBackground(new Color(220, 53, 69));
            removeButton.setForeground(Color.WHITE);
            removeButton.setPreferredSize(new Dimension(80, 30));
            
            cardPanel.add(cardLabel, BorderLayout.CENTER);
            cardPanel.add(removeButton, BorderLayout.EAST);
            panel.add(cardPanel);
        }
        
        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
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
            // Update user data using UserService
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            
            // Update the current user object
            currentUser.setFullname(name);
            currentUser.setPhonenumber(phone);
            currentUser.setAddress(address);
            
            JOptionPane.showMessageDialog(this, 
                "Profile updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
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
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
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
        JOptionPane.showMessageDialog(this, 
            "Add New Card functionality would be implemented here.\nThis would open a secure card entry form.", 
            "Add New Card", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}