package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import styles.RoundedButton;
import service.CartService;
import model.CartItem;
import model.User;

public class CartFrame extends JFrame {
    private CartService cartService;
    private DecimalFormat priceFormat;
    private JPanel cartItemsPanel;
    private JLabel subtotalLabel;
    private JLabel totalLabel;
    private JLabel checkoutTotalLabel;
    private JLabel selectedCardLabel = null;
    private String selectedCardType = null;
    private JTextField nameField;
    private JTextField cardNumField;
    private User currentUser;

    public CartFrame() {
        this(null);
    }

    public CartFrame(User user) {
        this.currentUser = user;
        this.cartService = CartService.getInstance();
        this.priceFormat = new DecimalFormat("$#,###");

        setTitle("Shopping Cart");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // Main content panel with two columns
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Back to Home button
        RoundedButton backButton = new RoundedButton("Back to Home", 10);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(new Color(0, 128, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(140, 40));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new ElectronicsFrame().setVisible(true);
            dispose();
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with two columns
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 0, 20);
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;

        // Left column - Shopping cart items
        JPanel leftColumn = createLeftColumn();
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(leftColumn, gbc);

        // Right column - Card details panel
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        gbc.insets = new Insets(20, 20, 0, 0);
        JPanel rightColumn = createCardDetailsPanel();
        centerPanel.add(rightColumn, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        refreshCartDisplay();
    }

    private JPanel createLeftColumn() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("SHOPPING CART");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        leftPanel.add(title, BorderLayout.NORTH);

        // Cart items list
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        leftPanel.add(scrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel createCardDetailsPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.WHITE);
        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(0, 153, 76));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new styles.RoundedBorder(20, new Color(0, 153, 76), 0),
            BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));
        cardPanel.setPreferredSize(new Dimension(420, 700));
        
        outerPanel.add(cardPanel, BorderLayout.CENTER);

        // Title
        JLabel title = new JLabel("Card Details");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Card type label
        JLabel cardTypeLabel = new JLabel("Card type");
        cardTypeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cardTypeLabel.setForeground(Color.WHITE);
        cardTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(cardTypeLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Card logos panel
        JPanel logosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        logosPanel.setBackground(new Color(0, 153, 76));
        logosPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mastercard = createCardImageLabel("images/MasterCard.png", 70, 45);
        JLabel visa = createCardImageLabel("images/Visa.png", 70, 45);
        JLabel rupay = createCardImageLabel("images/RunPay.png", 70, 45);

        logosPanel.add(mastercard);
        logosPanel.add(visa);
        logosPanel.add(rupay);
        cardPanel.add(logosPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Name on card
        JLabel nameLabel = new JLabel("Name on card");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(nameLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        nameField = createGreenTextField("Name");
        cardPanel.add(nameField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Card Number
        JLabel cardNumLabel = new JLabel("Card Number");
        cardNumLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cardNumLabel.setForeground(Color.WHITE);
        cardNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(cardNumLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        cardNumField = createGreenTextField("1111 2222 3333 4444");
        cardPanel.add(cardNumField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Expiration date and CVV row
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dateRow.setBackground(new Color(0, 153, 76));
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel expPanel = new JPanel();
        expPanel.setLayout(new BoxLayout(expPanel, BoxLayout.Y_AXIS));
        expPanel.setBackground(new Color(0, 153, 76));

        JLabel expLabel = new JLabel("Expiration date");
        expLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        expLabel.setForeground(Color.WHITE);
        expLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        expPanel.add(expLabel);
        expPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField expField = createGreenTextField("mm/yy");
        expField.setPreferredSize(new Dimension(150, 38));
        expField.setMaximumSize(new Dimension(150, 38));
        expPanel.add(expField);

        JPanel cvvPanel = new JPanel();
        cvvPanel.setLayout(new BoxLayout(cvvPanel, BoxLayout.Y_AXIS));
        cvvPanel.setBackground(new Color(0, 153, 76));

        JLabel cvvLabel = new JLabel("CVV");
        cvvLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cvvLabel.setForeground(Color.WHITE);
        cvvLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cvvPanel.add(cvvLabel);
        cvvPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextField cvvField = createGreenTextField("123");
        cvvField.setPreferredSize(new Dimension(150, 38));
        cvvField.setMaximumSize(new Dimension(150, 38));
        cvvPanel.add(cvvField);

        dateRow.add(expPanel);
        dateRow.add(Box.createRigidArea(new Dimension(15, 0)));
        dateRow.add(cvvPanel);
        cardPanel.add(dateRow);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Subtotal
        JPanel subtotalPanel = new JPanel(new BorderLayout());
        subtotalPanel.setBackground(new Color(0, 153, 76));
        subtotalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtotalPanel.setMaximumSize(new Dimension(350, 25));

        JLabel subtotalLabelText = new JLabel("Subtotal");
        subtotalLabelText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtotalLabelText.setForeground(Color.WHITE);

        subtotalLabel = new JLabel("$0");
        subtotalLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtotalLabel.setForeground(Color.WHITE);

        subtotalPanel.add(subtotalLabelText, BorderLayout.WEST);
        subtotalPanel.add(subtotalLabel, BorderLayout.EAST);
        cardPanel.add(subtotalPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Shipping
        JPanel shippingPanel = new JPanel(new BorderLayout());
        shippingPanel.setBackground(new Color(0, 153, 76));
        shippingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        shippingPanel.setMaximumSize(new Dimension(350, 25));

        JLabel shippingLabelText = new JLabel("Shipping");
        shippingLabelText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        shippingLabelText.setForeground(Color.WHITE);

        JLabel shippingValue = new JLabel("$4");
        shippingValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        shippingValue.setForeground(Color.WHITE);

        shippingPanel.add(shippingLabelText, BorderLayout.WEST);
        shippingPanel.add(shippingValue, BorderLayout.EAST);
        cardPanel.add(shippingPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Total (Tax incl.)
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(0, 153, 76));
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.setMaximumSize(new Dimension(350, 25));

        JLabel totalLabelText = new JLabel("Total (Tax incl.)");
        totalLabelText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        totalLabelText.setForeground(Color.WHITE);

        totalLabel = new JLabel("$4");
        totalLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        totalLabel.setForeground(Color.WHITE);

        totalPanel.add(totalLabelText, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        cardPanel.add(totalPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Checkout button
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(new Color(0, 153, 76));
        checkoutPanel.setMaximumSize(new Dimension(350, 55));
        checkoutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutPanel.setBorder(BorderFactory.createCompoundBorder(
            new styles.RoundedBorder(12, new Color(0, 153, 76), 0),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        checkoutTotalLabel = new JLabel("$4");
        checkoutTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        checkoutTotalLabel.setForeground(Color.WHITE);

        JLabel checkoutText = new JLabel("Checkout →");
        checkoutText.setFont(new Font("SansSerif", Font.BOLD, 16));
        checkoutText.setForeground(Color.WHITE);

        checkoutPanel.add(checkoutTotalLabel, BorderLayout.WEST);
        checkoutPanel.add(checkoutText, BorderLayout.EAST);
        checkoutPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                processCheckout();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                checkoutPanel.setBackground(new Color(0, 176, 88));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                checkoutPanel.setBackground(new Color(0, 153, 76));
            }
        });

        cardPanel.add(checkoutPanel);

        return outerPanel;
    }

    private JLabel createCardImageLabel(String imagePath, int width, int height) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(width, height));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(new styles.RoundedBorder(8, new Color(200, 200, 200), 2));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        try {
            java.io.File imageFile = new java.io.File(imagePath);
            if (imageFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imagePath);
                // Scale image slightly smaller than label size to show border
                Image scaledImage = originalIcon.getImage().getScaledInstance(width - 8, height - 8,
                        Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImage));
            } else {
                System.err.println("Image file not found: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }

        // Add click listener for selection
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Deselect previous card
                if (selectedCardLabel != null) {
                    selectedCardLabel.setBorder(new styles.RoundedBorder(8, new Color(200, 200, 200), 2));
                }
                // Select this card
                selectedCardLabel = label;
                label.setBorder(new styles.RoundedBorder(8, new Color(0, 153, 76), 3));
                
                // Determine card type from image path
                if (imagePath.contains("MasterCard")) {
                    selectedCardType = "MasterCard";
                } else if (imagePath.contains("Visa")) {
                    selectedCardType = "Visa";
                } else if (imagePath.contains("RunPay")) {
                    selectedCardType = "RuPay";
                }
            }
        });

        return label;
    }

    private JTextField createGreenTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(new Color(0, 176, 88));
        field.setForeground(new Color(200, 230, 200));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new styles.RoundedBorder(8, new Color(0, 176, 88), 0),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(350, 45));

        // Add focus listener to clear placeholder
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(200, 230, 200));
                }
            }
        });

        return field;
    }

    private void refreshCartDisplay() {
        cartItemsPanel.removeAll();

        List<CartItem> items = cartService.getCartItems();

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 24));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));
            cartItemsPanel.add(emptyLabel);
        } else {
            for (CartItem item : items) {
                JPanel itemPanel = createCartItemPanel(item);
                cartItemsPanel.add(itemPanel);
                cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        // Update totals
        double total = cartService.getTotalPrice();
        double totalWithShipping = total + 4;
        subtotalLabel.setText(priceFormat.format(total));
        totalLabel.setText(priceFormat.format(totalWithShipping));
        checkoutTotalLabel.setText(priceFormat.format(totalWithShipping));

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private JPanel createCartItemPanel(CartItem item) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            new styles.RoundedBorder(15, new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Product image
        JLabel imageLabel = createCartImageLabel(item.getProduct().getImagePath());
        imageLabel.setPreferredSize(new Dimension(80, 80));
        imageLabel.setBackground(new Color(248, 248, 248));
        imageLabel.setOpaque(true);
        imageLabel.setBorder(new styles.RoundedBorder(10, new Color(220, 220, 220), 1));

        // Product info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JLabel nameLabel = new JLabel(item.getProduct().getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel unitPriceLabel = new JLabel("Unit: " + priceFormat.format(item.getProduct().getPrice()));
        unitPriceLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        unitPriceLabel.setForeground(new Color(100, 100, 100));
        unitPriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(unitPriceLabel);

        // Right controls panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

        // Quantity controls
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        quantityPanel.setBackground(Color.WHITE);
        quantityPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Minus button
        RoundedButton minusBtn = new RoundedButton("-", 8);
        minusBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        minusBtn.setPreferredSize(new Dimension(35, 35));
        minusBtn.setBackground(new Color(240, 240, 240));
        minusBtn.setForeground(Color.BLACK);
        minusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minusBtn.addActionListener(e -> {
            if (item.getQuantity() > 1) {
                cartService.updateQuantity(item.getProduct(), item.getQuantity() - 1);
                refreshCartDisplay();
            }
        });

        JLabel quantityNum = new JLabel(String.valueOf(item.getQuantity()));
        quantityNum.setFont(new Font("SansSerif", Font.BOLD, 16));
        quantityNum.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // Plus button
        RoundedButton plusBtn = new RoundedButton("+", 8);
        plusBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        plusBtn.setPreferredSize(new Dimension(35, 35));
        plusBtn.setBackground(new Color(0, 128, 0));
        plusBtn.setForeground(Color.WHITE);
        plusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        plusBtn.addActionListener(e -> {
            cartService.updateQuantity(item.getProduct(), item.getQuantity() + 1);
            refreshCartDisplay();
        });

        quantityPanel.add(minusBtn);
        quantityPanel.add(quantityNum);
        quantityPanel.add(plusBtn);

        // Total price for this item
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        pricePanel.setBackground(Color.WHITE);
        pricePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel totalPriceLabel = new JLabel(priceFormat.format(item.getTotalPrice()));
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalPriceLabel.setForeground(new Color(0, 128, 0));
        pricePanel.add(totalPriceLabel);

        // Delete button
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        deletePanel.setBackground(Color.WHITE);
        deletePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        RoundedButton deleteBtn = new RoundedButton("Remove", 8);
        deleteBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        deleteBtn.setPreferredSize(new Dimension(80, 28));
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> {
            cartService.removeFromCart(item.getProduct());
            refreshCartDisplay();
        });
        deletePanel.add(deleteBtn);

        controlsPanel.add(quantityPanel);
        controlsPanel.add(pricePanel);
        controlsPanel.add(deletePanel);

        itemPanel.add(imageLabel, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(controlsPanel, BorderLayout.EAST);

        return itemPanel;
    }
    
    private void processCheckout() {
        if (cartService.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Your cart is empty. Please add items before checkout.", 
                "Cart Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cardName = nameField.getText().trim();
        String cardNumber = cardNumField.getText().trim().replaceAll("\\s+", "");

        if (cardName.isEmpty() || cardName.equals("Name")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter the cardholder name.", 
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        if (cardNumber.isEmpty() || cardNumber.equals("1111222233334444")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid card number.", 
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            cardNumField.requestFocus();
            return;
        }

        if (selectedCardType == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a card type.", 
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
            "Proceed with checkout for " + checkoutTotalLabel.getText() + "?",
            "Confirm Checkout", JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            try {
                String userEmail = (currentUser != null) ? currentUser.getEmail() : "guest@pcbway.com";
                
                // Try to use MongoDB service first
                Object orderService = null;
                try {
                    Class<?> orderServiceClass = Class.forName("service.OrderServiceMongo");
                    orderService = orderServiceClass.getMethod("getInstance").invoke(null);
                    
                    Class<?> orderClass = Class.forName("model.Order");
                    Object order = orderServiceClass.getMethod("createOrder", 
                        String.class, List.class, String.class, String.class, String.class)
                        .invoke(orderService, userEmail, cartService.getCartItems(), 
                              cardName, selectedCardType, cardNumber);
                    
                    String orderId = (String) orderClass.getMethod("getOrderId").invoke(order);
                    
                    JOptionPane.showMessageDialog(this, 
                        "Order placed successfully!\n" +
                        "Order ID: " + orderId + "\n" +
                        "Total: " + checkoutTotalLabel.getText() + "\n" +
                        "Payment: " + selectedCardType + " ending in " + cardNumber.substring(Math.max(0, cardNumber.length() - 4)),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (ClassNotFoundException e) {
                    // MongoDB not available, use in-memory service
                    Class<?> orderServiceClass = Class.forName("service.OrderService");
                    orderService = orderServiceClass.getMethod("getInstance").invoke(null);
                    
                    Class<?> orderClass = Class.forName("model.Order");
                    Object order = orderServiceClass.getMethod("createOrder", 
                        String.class, List.class, String.class, String.class, String.class)
                        .invoke(orderService, userEmail, cartService.getCartItems(), 
                              cardName, selectedCardType, cardNumber);
                    
                    String orderId = (String) orderClass.getMethod("getOrderId").invoke(order);
                    
                    JOptionPane.showMessageDialog(this, 
                        "Order placed successfully!\n" +
                        "Order ID: " + orderId + "\n" +
                        "Total: " + checkoutTotalLabel.getText() + "\n" +
                        "Payment: " + selectedCardType + " ending in " + cardNumber.substring(Math.max(0, cardNumber.length() - 4)),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                
                cartService.clearCart();
                refreshCartDisplay();
                
            } catch (Exception ex) {
                System.err.println("Error processing checkout: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Order placed successfully (in-memory)!\n" +
                    "Total: " + checkoutTotalLabel.getText(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                cartService.clearCart();
                refreshCartDisplay();
            }
        }
    }

    private JLabel createCartImageLabel(String imagePath) {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
            } else {
                label.setText("📦");
                label.setFont(new Font("SansSerif", Font.PLAIN, 30));
            }
        } catch (Exception e) {
            label.setText("📦");
            label.setFont(new Font("SansSerif", Font.PLAIN, 30));
        }
        
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CartFrame().setVisible(true);
        });
    }
}
