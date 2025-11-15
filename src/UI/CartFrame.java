package UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import styles.RoundedBorder;
import styles.RoundedButton;
import service.CartService;
import model.CartItem;

public class CartFrame extends JFrame {
    private CartService cartService;
    private DecimalFormat priceFormat;
    private JPanel cartItemsPanel;
    private JLabel totalPriceLabel;
    
    public CartFrame() {
        this.cartService = CartService.getInstance();
        this.priceFormat = new DecimalFormat("$0.00");
        
        setTitle("Cart");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // Add navbar
        add(createNavBar(), BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Shopping Cart", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(0, 100, 0));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        contentPanel.add(title, BorderLayout.NORTH);
        
        // Cart content
        createCartContent();
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Total and checkout section
        JPanel bottomPanel = createBottomPanel();
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
        
        refreshCartDisplay();
    }
    
    private void createCartContent() {
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 50, 100));
        
        // Total section
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        
        totalPriceLabel = new JLabel("Total: $0.00");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        totalPriceLabel.setForeground(new Color(0, 100, 0));
        totalPanel.add(totalPriceLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonsPanel.setBackground(Color.WHITE);
        
        RoundedButton clearCartBtn = new RoundedButton("Clear Cart", 25);
        clearCartBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        clearCartBtn.setBackground(new Color(255, 200, 200));
        clearCartBtn.setForeground(new Color(150, 0, 0));
        clearCartBtn.setHoverColor(new Color(240, 180, 180));
        clearCartBtn.setPreferredSize(new Dimension(120, 40));
        clearCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        clearCartBtn.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear the cart?",
                "Clear Cart",
                JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                cartService.clearCart();
                refreshCartDisplay();
                JOptionPane.showMessageDialog(this, "Cart cleared!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        RoundedButton checkoutBtn = new RoundedButton("Checkout", 25);
        checkoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkoutBtn.setBackground(new Color(200, 255, 200));
        checkoutBtn.setForeground(new Color(0, 120, 0));
        checkoutBtn.setHoverColor(new Color(180, 240, 180));
        checkoutBtn.setPreferredSize(new Dimension(120, 40));
        checkoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        checkoutBtn.addActionListener(e -> {
            if (cartService.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Checkout functionality coming soon!\\nTotal: " + priceFormat.format(cartService.getTotalPrice()),
                    "Checkout", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        buttonsPanel.add(clearCartBtn);
        buttonsPanel.add(checkoutBtn);
        
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return bottomPanel;
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
        
        // Update total price
        totalPriceLabel.setText("Total: " + priceFormat.format(cartService.getTotalPrice()));
        
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }
    
    private JPanel createCartItemPanel(CartItem item) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Product image
        JLabel imageLabel = createCartImageLabel(item.getProduct().getImagePath());
        imageLabel.setPreferredSize(new Dimension(80, 80));
        imageLabel.setBackground(new Color(245, 245, 245));
        imageLabel.setOpaque(true);
        imageLabel.setBorder(new RoundedBorder(8, new Color(220, 220, 220), 1));
        
        // Product info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        
        JLabel nameLabel = new JLabel(item.getProduct().getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JLabel priceLabel = new JLabel(priceFormat.format(item.getProduct().getPrice()) + " each");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GRAY);
        
        JLabel totalLabel = new JLabel("Total: " + priceFormat.format(item.getTotalPrice()));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 100, 0));
        
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(priceLabel, BorderLayout.CENTER);
        infoPanel.add(totalLabel, BorderLayout.SOUTH);
        
        // Quantity controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setBackground(Color.WHITE);
        
        RoundedButton decreaseBtn = new RoundedButton("-", 15);
        decreaseBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        decreaseBtn.setPreferredSize(new Dimension(40, 30));
        decreaseBtn.setBackground(new Color(255, 200, 200));
        decreaseBtn.setForeground(new Color(150, 0, 0));
        decreaseBtn.setHoverColor(new Color(240, 180, 180));
        decreaseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel quantityLabel = new JLabel(String.valueOf(item.getQuantity()));
        quantityLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        quantityLabel.setPreferredSize(new Dimension(30, 30));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        RoundedButton increaseBtn = new RoundedButton("+", 15);
        increaseBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        increaseBtn.setPreferredSize(new Dimension(40, 30));
        increaseBtn.setBackground(new Color(200, 255, 200));
        increaseBtn.setForeground(new Color(0, 120, 0));
        increaseBtn.setHoverColor(new Color(180, 240, 180));
        increaseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        RoundedButton removeBtn = new RoundedButton("Remove", 15);
        removeBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        removeBtn.setBackground(new Color(255, 200, 200));
        removeBtn.setForeground(new Color(150, 0, 0));
        removeBtn.setHoverColor(new Color(240, 180, 180));
        removeBtn.setPreferredSize(new Dimension(70, 30));
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listeners
        decreaseBtn.addActionListener(e -> {
            if (item.getQuantity() > 1) {
                cartService.updateQuantity(item.getProduct(), item.getQuantity() - 1);
                refreshCartDisplay();
            }
        });
        
        increaseBtn.addActionListener(e -> {
            cartService.updateQuantity(item.getProduct(), item.getQuantity() + 1);
            refreshCartDisplay();
        });
        
        removeBtn.addActionListener(e -> {
            cartService.removeFromCart(item.getProduct());
            refreshCartDisplay();
        });
        
        controlsPanel.add(decreaseBtn);
        controlsPanel.add(quantityLabel);
        controlsPanel.add(increaseBtn);
        controlsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        controlsPanel.add(removeBtn);
        
        itemPanel.add(imageLabel, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(controlsPanel, BorderLayout.EAST);
        
        return itemPanel;
    }
    
    private JPanel createNavBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 80, 15, 80));

        // Navigation Links
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 35, 0));
        navPanel.setBackground(Color.WHITE);

        String[] navItems = { "SERVICES", "PCB PRINTING", "ELECTRONICS", "CONTACT" };
        for (String item : navItems) {
            JLabel link = new JLabel(item);
            link.setFont(new Font("SansSerif", Font.BOLD, 16));
            link.setForeground(new Color(34, 139, 34));

            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    link.setForeground(new Color(0, 100, 0));
                    link.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    link.setForeground(new Color(34, 139, 34));
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    navigateToPage(item);
                }
            });
            navPanel.add(link);
        }

        // Right section: Search + icons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(18) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.setFont(getFont());
                    g2.drawString("Search...", 10, 20);
                    g2.dispose();
                }
            }
        };
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(new RoundedBorder(20, new Color(34, 139, 34), 2));
        searchField.setPreferredSize(new Dimension(200, 35));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        userIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    CartFrame.this,
                    "Do you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    SignupFrame.getUserService().logout();
                    JOptionPane.showMessageDialog(CartFrame.this,
                        "You have been logged out successfully.",
                        "Logout",
                        JOptionPane.INFORMATION_MESSAGE);
                    new LoginFrame().setVisible(true);
                    dispose();
                }
            }
        });

        JLabel cartIcon = new JLabel("🛒");
        cartIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        cartIcon.setForeground(new Color(0, 100, 0)); // Highlight cart icon

        rightPanel.add(searchField);
        rightPanel.add(userIcon);
        rightPanel.add(cartIcon);

        header.add(navPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private void navigateToPage(String pageName) {
        SwingUtilities.invokeLater(() -> {
            switch (pageName) {
                case "SERVICES":
                    new ServicesFrame().setVisible(true);
                    this.dispose();
                    break;
                case "PCB PRINTING":
                    new PcbPrinting().setVisible(true);
                    this.dispose();
                    break;
                case "ELECTRONICS":
                    new ElectronicsFrame().setVisible(true);
                    this.dispose();
                    break;
                case "CONTACT":
                    new ContactFrame().setVisible(true);
                    this.dispose();
                    break;
                default:
                    break;
            }
        });
    }

    private JLabel createCartImageLabel(String imagePath) {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        
        try {
            // Try to load the image
            ImageIcon icon = new ImageIcon(imagePath);
            
            // Check if image loaded successfully
            if (icon.getIconWidth() > 0) {
                // Scale the image to fit cart item size
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
            } else {
                // Fallback to placeholder
                label.setText("📦");
                label.setFont(new Font("SansSerif", Font.PLAIN, 30));
            }
        } catch (Exception e) {
            // Fallback to placeholder
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