package UI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import styles.RoundedBorder;
import styles.RoundedButton;
import model.PCBOrder;
import service.CartServiceMongo;

public class PcbPrinting extends JFrame {
    private PCBOrder currentOrder;
    private CartServiceMongo cartService;

    // UI Components
    private JComboBox<String> materialCombo;
    private JComboBox<String> layersCombo;
    private JLabel totalPriceLabel;

    public PcbPrinting() {
        this.currentOrder = new PCBOrder();
        this.cartService = CartServiceMongo.getInstance();
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

        JLabel title = new JLabel("PCB PRINTING", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(0, 100, 0));
        title.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 0));
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
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(80, 150, 80, 150));

        // Main content panel using GridBagLayout for better control
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Left side - Preview and Upload
        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setBackground(Color.WHITE);

        // Preview box
        JPanel previewBox = new JPanel();
        previewBox.setBackground(new Color(245, 245, 245)); // Lighter gray
        previewBox.setPreferredSize(new Dimension(300, 300));
        previewBox.setBorder(new RoundedBorder(12, new Color(220, 220, 220), 1));

        // Upload button
        RoundedButton uploadBtn = new RoundedButton("UPLOAD MODEL", 22);
        uploadBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        uploadBtn.setBackground(new Color(130, 210, 130)); // Light green
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setHoverColor(new Color(100, 200, 100));
        uploadBtn.setPreferredSize(new Dimension(200, 45));
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadBtn.addActionListener(e -> uploadFile());

        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.add(uploadBtn);

        leftPanel.add(previewBox, BorderLayout.CENTER);
        leftPanel.add(uploadPanel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 80);
        mainContentPanel.add(leftPanel, gbc);

        // Middle - Options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(Color.WHITE);

        // Material section
        JLabel materialLabel = new JLabel("CHOOSE MATERIAL");
        materialLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        materialLabel.setForeground(Color.BLACK);
        materialLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add empty option as first option to match the design
        String[] materialsWithPlaceholder = new String[PCBOrder.MATERIALS.length + 1];
        materialsWithPlaceholder[0] = "";
        System.arraycopy(PCBOrder.MATERIALS, 0, materialsWithPlaceholder, 1, PCBOrder.MATERIALS.length);

        materialCombo = new JComboBox<>(materialsWithPlaceholder);
        materialCombo.setSelectedIndex(0); // Start with empty
        materialCombo.setMaximumSize(new Dimension(480, 50));
        materialCombo.setPreferredSize(new Dimension(480, 50));
        materialCombo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        materialCombo.setBackground(Color.WHITE);
        materialCombo.setForeground(Color.BLACK);
        materialCombo.setBorder(new RoundedBorder(25, new Color(180, 180, 180), 2));
        materialCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        materialCombo.setOpaque(true);
        materialCombo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set custom renderer to ensure text is visible
        materialCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(200, 230, 200) : Color.WHITE);
                setForeground(Color.BLACK);
                setFont(new Font("SansSerif", Font.PLAIN, 15));
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                setOpaque(true);
                return this;
            }
        });

        materialCombo.addActionListener(e -> {
            String selected = (String) materialCombo.getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                currentOrder.setMaterial(selected);
                updatePriceDisplay();
            }
        });

        // Layers section
        JLabel layersLabel = new JLabel("CHOOSE HOW MANY LAYERS");
        layersLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        layersLabel.setForeground(Color.BLACK);
        layersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add empty option as first option to match the design
        String[] layerOptionsWithPlaceholder = new String[PCBOrder.LAYER_OPTIONS.length + 1];
        layerOptionsWithPlaceholder[0] = "";
        for (int i = 0; i < PCBOrder.LAYER_OPTIONS.length; i++) {
            layerOptionsWithPlaceholder[i + 1] = String.valueOf(PCBOrder.LAYER_OPTIONS[i]);
        }

        layersCombo = new JComboBox<>(layerOptionsWithPlaceholder);
        layersCombo.setSelectedIndex(0); // Start with empty
        layersCombo.setMaximumSize(new Dimension(480, 50));
        layersCombo.setPreferredSize(new Dimension(480, 50));
        layersCombo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        layersCombo.setBackground(Color.WHITE);
        layersCombo.setForeground(Color.BLACK);
        layersCombo.setBorder(new RoundedBorder(25, new Color(180, 180, 180), 2));
        layersCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        layersCombo.setOpaque(true);
        layersCombo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set custom renderer to ensure text is visible
        layersCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(200, 230, 200) : Color.WHITE);
                setForeground(Color.BLACK);
                setFont(new Font("SansSerif", Font.PLAIN, 15));
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                setOpaque(true);
                return this;
            }
        });

        layersCombo.addActionListener(e -> {
            String selected = (String) layersCombo.getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                try {
                    currentOrder.setLayers(Integer.parseInt(selected));
                    updatePriceDisplay();
                } catch (NumberFormatException ex) {
                    // Ignore invalid input
                }
            }
        });

        // Add components with spacing
        optionsPanel.add(materialLabel);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        optionsPanel.add(materialCombo);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        optionsPanel.add(layersLabel);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        optionsPanel.add(layersCombo);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 100);
        mainContentPanel.add(optionsPanel, gbc);

        // Right side - Price and Order button
        JPanel priceOrderPanel = new JPanel();
        priceOrderPanel.setLayout(new BoxLayout(priceOrderPanel, BoxLayout.Y_AXIS));
        priceOrderPanel.setBackground(Color.WHITE);

        // Total price label
        JLabel totalLabel = new JLabel("TOTAL:");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(new Color(80, 80, 80));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalPriceLabel = new JLabel("999 DA");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        totalPriceLabel.setForeground(new Color(34, 139, 34));
        totalPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton orderBtn = new RoundedButton("ORDER", 22);
        orderBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        orderBtn.setBackground(new Color(130, 210, 130)); // Light green
        orderBtn.setForeground(Color.WHITE);
        orderBtn.setHoverColor(new Color(100, 200, 100));
        orderBtn.setPreferredSize(new Dimension(150, 45));
        orderBtn.setMaximumSize(new Dimension(150, 45));
        orderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderBtn.addActionListener(e -> addToCart());

        priceOrderPanel.add(totalLabel);
        priceOrderPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        priceOrderPanel.add(totalPriceLabel);
        priceOrderPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        priceOrderPanel.add(orderBtn);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContentPanel.add(priceOrderPanel, gbc);

        formPanel.add(mainContentPanel, BorderLayout.NORTH);

        return formPanel;
    }

    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select PCB Files");

        // Set file filters
        FileNameExtensionFilter zipFilter = new FileNameExtensionFilter("Archive files (*.zip, *.rar)", "zip", "rar");
        FileNameExtensionFilter gerberFilter = new FileNameExtensionFilter("Gerber files (*.gerber, *.gbr)", "gerber",
                "gbr");
        FileNameExtensionFilter pcbFilter = new FileNameExtensionFilter("PCB files (*.pcb, *.brd)", "pcb", "brd");

        fileChooser.addChoosableFileFilter(zipFilter);
        fileChooser.addChoosableFileFilter(gerberFilter);
        fileChooser.addChoosableFileFilter(pcbFilter);
        fileChooser.setFileFilter(zipFilter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentOrder.setUploadedFile(selectedFile);
            JOptionPane.showMessageDialog(this,
                    "File uploaded: " + selectedFile.getName(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updatePriceDisplay() {
        currentOrder.calculatePrice();
        double total = currentOrder.getTotalPrice();
        // Convert to DA (Algerian Dinar) - using a simple conversion for display
        int totalDA = (int) (total * 100); // Simplified conversion
        totalPriceLabel.setText(totalDA + " DA");
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
                description);

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
                        JOptionPane.QUESTION_MESSAGE);

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
                new CartFrame(null).setVisible(true);
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
