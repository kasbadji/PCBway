package UI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import styles.RoundedBorder;
import styles.RoundedButton;
import model.PCBOrder;
import service.CartService;

public class PcbPrinting extends JFrame {
    private PCBOrder currentOrder;
    private CartService cartService;
    private DecimalFormat priceFormat;
    
    // UI Components
    private JLabel fileLabel;
    private JComboBox<String> materialCombo;
    private JComboBox<Integer> layersCombo;
    private JComboBox<String> surfaceFinishCombo;
    private JComboBox<String> solderMaskCombo;
    private JSpinner quantitySpinner;
    private JLabel pricePerPieceLabel;
    private JLabel totalPriceLabel;
    
    public PcbPrinting() {
        this.currentOrder = new PCBOrder();
        this.cartService = CartService.getInstance();
        this.priceFormat = new DecimalFormat("$0.00");
        
        setTitle("PCB Printing");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        add(createNavBar(), BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("PCB Printing Services", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(0, 100, 0));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        contentPanel.add(title, BorderLayout.NORTH);
        
        JPanel orderFormPanel = createOrderForm();
        JScrollPane scrollPane = new JScrollPane(orderFormPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        updatePriceDisplay();
    }
    
    private JPanel createOrderForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 50, 100));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // File Upload Section
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JPanel filePanel = createFileUploadPanel();
        formPanel.add(filePanel, gbc);
        
        // Specifications Section
        gbc.gridwidth = 1; gbc.gridy = 1;
        
        // Material Selection
        gbc.gridx = 0;
        JLabel materialLabel = new JLabel("Material:");
        materialLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(materialLabel, gbc);
        
        gbc.gridx = 1;
        materialCombo = new JComboBox<>(PCBOrder.MATERIALS);
        materialCombo.setPreferredSize(new Dimension(200, 35));
        materialCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        materialCombo.addActionListener(e -> {
            currentOrder.setMaterial((String) materialCombo.getSelectedItem());
            updatePriceDisplay();
        });
        formPanel.add(materialCombo, gbc);
        
        // Layer Selection
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel layersLabel = new JLabel("Layers:");
        layersLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(layersLabel, gbc);
        
        gbc.gridx = 1;
        Integer[] layerOptions = new Integer[PCBOrder.LAYER_OPTIONS.length];
        for (int i = 0; i < PCBOrder.LAYER_OPTIONS.length; i++) {
            layerOptions[i] = PCBOrder.LAYER_OPTIONS[i];
        }
        layersCombo = new JComboBox<>(layerOptions);
        layersCombo.setPreferredSize(new Dimension(200, 35));
        layersCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        layersCombo.addActionListener(e -> {
            currentOrder.setLayers((Integer) layersCombo.getSelectedItem());
            updatePriceDisplay();
        });
        formPanel.add(layersCombo, gbc);
        
        // Surface Finish
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel surfaceLabel = new JLabel("Surface Finish:");
        surfaceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(surfaceLabel, gbc);
        
        gbc.gridx = 1;
        surfaceFinishCombo = new JComboBox<>(PCBOrder.SURFACE_FINISHES);
        surfaceFinishCombo.setPreferredSize(new Dimension(200, 35));
        surfaceFinishCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        surfaceFinishCombo.addActionListener(e -> {
            currentOrder.setSurfaceFinish((String) surfaceFinishCombo.getSelectedItem());
            updatePriceDisplay();
        });
        formPanel.add(surfaceFinishCombo, gbc);
        
        // Solder Mask
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel solderMaskLabel = new JLabel("Solder Mask:");
        solderMaskLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(solderMaskLabel, gbc);
        
        gbc.gridx = 1;
        solderMaskCombo = new JComboBox<>(PCBOrder.SOLDER_MASK_COLORS);
        solderMaskCombo.setPreferredSize(new Dimension(200, 35));
        solderMaskCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        solderMaskCombo.addActionListener(e -> {
            currentOrder.setSolderMask((String) solderMaskCombo.getSelectedItem());
        });
        formPanel.add(solderMaskCombo, gbc);
        
        // Quantity
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(quantityLabel, gbc);
        
        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
        quantitySpinner.setPreferredSize(new Dimension(200, 35));
        quantitySpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        quantitySpinner.addChangeListener(e -> {
            currentOrder.setQuantity((Integer) quantitySpinner.getValue());
            updatePriceDisplay();
        });
        formPanel.add(quantitySpinner, gbc);
        
        // Pricing Section
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel pricingPanel = createPricingPanel();
        formPanel.add(pricingPanel, gbc);
        
        // Order Button
        gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel buttonPanel = createButtonPanel();
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private JPanel createFileUploadPanel() {
        JPanel filePanel = new JPanel(new BorderLayout());
        filePanel.setBackground(Color.WHITE);
        filePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Upload PCB Files",
            0, 0, new Font("SansSerif", Font.BOLD, 18), new Color(0, 100, 0)
        ));
        filePanel.setPreferredSize(new Dimension(600, 120));
        
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        RoundedButton uploadBtn = new RoundedButton("📁 Upload Model", 25);
        uploadBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        uploadBtn.setBackground(new Color(200, 255, 200));
        uploadBtn.setForeground(new Color(0, 120, 0));
        uploadBtn.setHoverColor(new Color(180, 240, 180));
        uploadBtn.setPreferredSize(new Dimension(160, 45));
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect is handled by RoundedButton
        
        uploadBtn.addActionListener(e -> uploadFile());
        
        fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        fileLabel.setForeground(Color.GRAY);
        
        contentPanel.add(uploadBtn);
        contentPanel.add(fileLabel);
        
        JTextArea infoArea = new JTextArea("Supported formats: .zip, .rar, .gerber, .gbr, .pcb, .brd");
        infoArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoArea.setForeground(Color.GRAY);
        infoArea.setEditable(false);
        infoArea.setBackground(Color.WHITE);
        
        filePanel.add(contentPanel, BorderLayout.CENTER);
        filePanel.add(infoArea, BorderLayout.SOUTH);
        
        return filePanel;
    }
    
    private JPanel createPricingPanel() {
        JPanel pricingPanel = new JPanel(new BorderLayout());
        pricingPanel.setBackground(Color.WHITE);
        pricingPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Pricing",
            0, 0, new Font("SansSerif", Font.BOLD, 18), new Color(0, 100, 0)
        ));
        pricingPanel.setPreferredSize(new Dimension(600, 100));
        
        JPanel priceContentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        priceContentPanel.setBackground(Color.WHITE);
        
        pricePerPieceLabel = new JLabel("Price per piece: $0.00");
        pricePerPieceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        pricePerPieceLabel.setForeground(new Color(0, 100, 0));
        
        totalPriceLabel = new JLabel("Total: $0.00");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalPriceLabel.setForeground(new Color(0, 100, 0));
        
        priceContentPanel.add(pricePerPieceLabel);
        priceContentPanel.add(totalPriceLabel);
        
        pricingPanel.add(priceContentPanel, BorderLayout.CENTER);
        
        return pricingPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        RoundedButton addToCartBtn = new RoundedButton("🛒 Add to Cart", 25);
        addToCartBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        addToCartBtn.setBackground(new Color(200, 255, 200));
        addToCartBtn.setForeground(new Color(0, 120, 0));
        addToCartBtn.setHoverColor(new Color(180, 240, 180));
        addToCartBtn.setPreferredSize(new Dimension(170, 45));
        addToCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect is handled by RoundedButton
        
        addToCartBtn.addActionListener(e -> addToCart());
        
        
        
        
        
        buttonPanel.add(addToCartBtn);
        
        return buttonPanel;
    }
    
    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select PCB Files");
        
        // Set file filters
        FileNameExtensionFilter zipFilter = new FileNameExtensionFilter("Archive files (*.zip, *.rar)", "zip", "rar");
        FileNameExtensionFilter gerberFilter = new FileNameExtensionFilter("Gerber files (*.gerber, *.gbr)", "gerber", "gbr");
        FileNameExtensionFilter pcbFilter = new FileNameExtensionFilter("PCB files (*.pcb, *.brd)", "pcb", "brd");
        
        fileChooser.addChoosableFileFilter(zipFilter);
        fileChooser.addChoosableFileFilter(gerberFilter);
        fileChooser.addChoosableFileFilter(pcbFilter);
        fileChooser.setFileFilter(zipFilter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentOrder.setUploadedFile(selectedFile);
            fileLabel.setText("📎 " + selectedFile.getName());
            fileLabel.setForeground(new Color(0, 100, 0));
            fileLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        }
    }
    
    private void updatePriceDisplay() {
        currentOrder.calculatePrice();
        pricePerPieceLabel.setText("Price per piece: " + priceFormat.format(currentOrder.getPricePerPiece()));
        totalPriceLabel.setText("Total: " + priceFormat.format(currentOrder.getTotalPrice()));
    }
    
    private void addToCart() {
        if (!currentOrder.isValid()) {
            JOptionPane.showMessageDialog(this,
                "Please upload a PCB file first!",
                "No File Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create a product representation of the PCB order for the cart
        String productName = "Custom PCB - " + currentOrder.getMaterial() + " " + currentOrder.getLayers() + " Layer";
        String description = String.format("%s, %s, %d pieces", 
            currentOrder.getSurfaceFinish(), 
            currentOrder.getSolderMask(), 
            currentOrder.getQuantity());
        
        model.Product pcbProduct = new model.Product(
            productName,
            "images/pcb1.png",
            currentOrder.getTotalPrice(),
            description
        );
        
        cartService.addToCart(pcbProduct, 1);
        
        JOptionPane.showMessageDialog(this,
            "PCB order added to cart!\n" + currentOrder.getOrderSummary(),
            "Added to Cart",
            JOptionPane.INFORMATION_MESSAGE);
    }
    

    

    private JPanel createNavBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 80, 15, 80));

        JPanel navLinks = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        navLinks.setBackground(Color.WHITE);

        String[] navItems = { "SERVICES", "PCB PRINTING", "ELECTRONICS", "CONTACT" };
        for (String item : navItems) {
            JLabel link = new JLabel(item);
            link.setFont(new Font("SansSerif", Font.BOLD, 16));
            
            if (item.equals("PCB PRINTING")) {
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
                    if (item.equals("PCB PRINTING")) {
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
            navLinks.add(link);
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
                    PcbPrinting.this,
                    "Do you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    SignupFrame.getUserService().logout();
                    JOptionPane.showMessageDialog(PcbPrinting.this,
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
        cartIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CartFrame().setVisible(true);
                dispose();
            }
        });

        rightPanel.add(searchField);
        rightPanel.add(userIcon);
        rightPanel.add(cartIcon);

        header.add(navLinks, BorderLayout.WEST);
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
                    // Already on PCB Printing page
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PcbPrinting().setVisible(true);
        });
    }
}
