package UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import styles.RoundedBorder;
import styles.RoundedButton;
import service.ProductService;
import service.CartService;
import model.Product;

public class ElectronicsFrame extends JFrame {
    private ProductService productService;
    private CartService cartService;
    private DecimalFormat priceFormat;
    private JPanel productsPanel;
    private JLabel cartCountLabel;
    
    public ElectronicsFrame() {
        this.productService = ProductService.getInstance();
        this.cartService = CartService.getInstance();
        this.priceFormat = new DecimalFormat("$0.00");
        
        setTitle("Electronics");
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
        
        JLabel title = new JLabel("Electronics Store", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(0, 100, 0));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        contentPanel.add(title, BorderLayout.NORTH);
        
        // Products grid
        createProductsGrid();
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        
        updateCartDisplay();
    }
    
    private void createProductsGrid() {
        productsPanel = new JPanel(new GridLayout(0, 3, 30, 30));
        productsPanel.setBackground(Color.WHITE);
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 80, 80));
        
        List<Product> products = productService.getAllProducts();
        
        for (Product product : products) {
            JPanel productCard = createProductCard(product);
            productsPanel.add(productCard);
        }
    }
    
    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(350, 400));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Product image
        JLabel imageLabel = createImageLabel(product.getImagePath());
        imageLabel.setPreferredSize(new Dimension(320, 150));
        imageLabel.setBackground(new Color(245, 245, 245));
        imageLabel.setOpaque(true);
        imageLabel.setBorder(new RoundedBorder(10, new Color(220, 220, 220), 1));
        card.add(imageLabel, BorderLayout.NORTH);
        
        // Product info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Product name
        JLabel nameLabel = new JLabel("<html><div style='text-align: center;'>" + product.getName() + "</div></html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        
        // Product description
        JTextArea descArea = new JTextArea(product.getDescription());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        infoPanel.add(descArea, BorderLayout.CENTER);
        
        // Price and add to cart
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        
        JLabel priceLabel = new JLabel(priceFormat.format(product.getPrice()));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        priceLabel.setForeground(new Color(0, 100, 0));
        priceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        RoundedButton addToCartBtn = new RoundedButton("Add to Cart", 25);
        addToCartBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        addToCartBtn.setBackground(new Color(200, 255, 200));
        addToCartBtn.setForeground(new Color(0, 120, 0));
        addToCartBtn.setHoverColor(new Color(180, 240, 180));
        addToCartBtn.setPreferredSize(new Dimension(100, 30));
        addToCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addToCartBtn.addActionListener(e -> {
            cartService.addToCart(product, 1);
            updateCartDisplay();
            JOptionPane.showMessageDialog(this, 
                product.getName() + " added to cart!", 
                "Added to Cart", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        bottomPanel.add(priceLabel, BorderLayout.WEST);
        bottomPanel.add(addToCartBtn, BorderLayout.EAST);
        
        infoPanel.add(bottomPanel, BorderLayout.SOUTH);
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void updateCartDisplay() {
        if (cartCountLabel != null) {
            int itemCount = cartService.getTotalItemCount();
            cartCountLabel.setText("🛒(" + itemCount + ")");
        }
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
            
            // Highlight current page
            if (item.equals("ELECTRONICS")) {
                link.setForeground(new Color(0, 100, 0));
            } else {
                link.setForeground(new Color(34, 139, 34));
            }

            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    link.setForeground(new Color(0, 100, 0));
                    link.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (item.equals("ELECTRONICS")) {
                        link.setForeground(new Color(0, 100, 0));
                    } else {
                        link.setForeground(new Color(34, 139, 34));
                    }
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
        
        // Add search functionality
        searchField.addActionListener(e -> {
            String query = searchField.getText().trim();
            searchProducts(query);
        });

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        userIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    ElectronicsFrame.this,
                    "Do you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    SignupFrame.getUserService().logout();
                    JOptionPane.showMessageDialog(ElectronicsFrame.this,
                        "You have been logged out successfully.",
                        "Logout",
                        JOptionPane.INFORMATION_MESSAGE);
                    new LoginFrame().setVisible(true);
                    dispose();
                }
            }
        });

        cartCountLabel = new JLabel("🛒(0)");
        cartCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        cartCountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartCountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CartFrame().setVisible(true);
                dispose();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                cartCountLabel.setForeground(new Color(0, 100, 0));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cartCountLabel.setForeground(Color.BLACK);
            }
        });

        rightPanel.add(searchField);
        rightPanel.add(userIcon);
        rightPanel.add(cartCountLabel);

        header.add(navPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private void searchProducts(String query) {
        productsPanel.removeAll();
        List<Product> filteredProducts = productService.searchProducts(query);
        
        for (Product product : filteredProducts) {
            JPanel productCard = createProductCard(product);
            productsPanel.add(productCard);
        }
        
        productsPanel.revalidate();
        productsPanel.repaint();
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
                    // Already on Electronics page
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
    
    private JLabel createImageLabel(String imagePath) {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        
        try {
            // Try to load the image
            ImageIcon icon = new ImageIcon(imagePath);
            
            // Check if image loaded successfully
            if (icon.getIconWidth() > 0) {
                // Scale the image to fit
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(300, 140, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
            } else {
                // Fallback to placeholder
                label.setText("📦");
                label.setFont(new Font("SansSerif", Font.PLAIN, 60));
            }
        } catch (Exception e) {
            // Fallback to placeholder
            label.setText("📦");
            label.setFont(new Font("SansSerif", Font.PLAIN, 60));
        }
        
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ElectronicsFrame().setVisible(true);
        });
    }
}
