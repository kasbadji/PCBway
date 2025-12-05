package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import styles.RoundedBorder;
import styles.RoundedButton;
import service.CartServiceMongo;

public class ServicesFrame extends JFrame {
    private CartServiceMongo cartService;
    private JLabel cartCountLabel;

    public ServicesFrame() {
        this.cartService = CartServiceMongo.getInstance();
        setTitle("Services");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // ========== HEADER ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 80, 15, 80));

        // --- Navigation Links
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 0));
        navPanel.setBackground(Color.WHITE);

        String[] navItems = { "SERVICES", "PCB PRINTING", "ELECTRONICS", "CONTACT" };
        for (String item : navItems) {
            JLabel link = new JLabel(item);
            link.setFont(new Font("SansSerif", Font.BOLD, 20));
            link.setForeground(new Color(2, 158, 54));

            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    link.setForeground(new Color(0, 100, 0));
                    link.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    link.setForeground(new Color(2, 158, 54));
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    navigateToPage(item);
                }
            });
            navPanel.add(link);
        }

        // --- Right section: Search + icons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setBackground(Color.WHITE);

        // Custom text field with placeholder
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

        RoundedButton logoutButton = new RoundedButton("Logout", 20);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setHoverColor(new Color(200, 35, 51));
        logoutButton.setPreferredSize(new Dimension(90, 35));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                ServicesFrame.this,
                "Do you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (response == JOptionPane.YES_OPTION) {
                SignupFrame.getUserService().logout();
                JOptionPane.showMessageDialog(ServicesFrame.this,
                    "You have been logged out successfully.",
                    "Logout",
                    JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        // Profile button
        JLabel profileIcon = new JLabel("👤 Profile");
        profileIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        profileIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SignupFrame.getUserService().isLoggedIn()) {
                    new ProfileFrame(SignupFrame.getUserService().getCurrentUser()).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(ServicesFrame.this,
                        "Please login to access your profile.",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE);
                    new LoginFrame().setVisible(true);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                profileIcon.setForeground(new Color(0, 100, 0));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                profileIcon.setForeground(Color.BLACK);
            }
        });

        cartCountLabel = new JLabel("🛒(0)");
        cartCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        cartCountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartCountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SignupFrame.getUserService().isLoggedIn()) {
                    new CartFrame(SignupFrame.getUserService().getCurrentUser()).setVisible(true);
                } else {
                    new CartFrame().setVisible(true);
                }
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
        rightPanel.add(logoutButton);
        rightPanel.add(profileIcon);
        rightPanel.add(cartCountLabel);

        header.add(navPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        JLabel title = new JLabel("Our Services", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(0, 100, 0));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 20, 0));
        contentPanel.add(title, BorderLayout.NORTH);
        // --- Services section
        JPanel servicesPanel = new JPanel(new GridBagLayout());
        servicesPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 50, 0, 50);
        gbc.gridy = 0;
        // Add cards with navigation
        JPanel pcbCard = createServiceCard("PCB PRINTING", "images/Pcb.png", () -> navigateToPage("PCB PRINTING"));
        gbc.gridx = 0;
        servicesPanel.add(pcbCard, gbc);
        JPanel elecCard = createServiceCard("ELECTRONICS", "images/Electronics.png", () -> navigateToPage("ELECTRONICS"));
        gbc.gridx = 1;
        servicesPanel.add(elecCard, gbc);
        contentPanel.add(servicesPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        if (cartCountLabel != null) {
            int itemCount = cartService.getTotalItemCount();
            cartCountLabel.setText("🛒(" + itemCount + ")");
        }
    }

    private void navigateToPage(String pageName) {
        SwingUtilities.invokeLater(() -> {
            switch (pageName) {
                case "SERVICES":
                    
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

    private JPanel createServiceCard(String title, String imagePath, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Images
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaled = icon.getImage().getScaledInstance(300, 240, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                if (getIcon() != null) {
                    g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                    getIcon().paintIcon(this, g2, 0, 0);
                }
                g2.setClip(null);
                g2.setColor(new Color(220, 220, 220));
                g2.setStroke(new BasicStroke(2));
                g2.draw(new java.awt.geom.RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 30, 30));
                g2.dispose();
            }
        };
        imageLabel.setPreferredSize(new Dimension(300, 240));
        imageLabel.setMaximumSize(new Dimension(300, 240));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setOpaque(false);
        JButton button = new JButton(title + " ↗");
        button.setBackground(new Color(200, 255, 200));
        button.setForeground(new Color(0, 120, 0));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(25, new Color(200, 255, 200), 0));
        button.setPreferredSize(new Dimension(200, 45));
        button.setMaximumSize(new Dimension(200, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add click action
        button.addActionListener(e -> {
            if (onClick != null) {
                onClick.run();
            }
        });
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(180, 240, 180));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(200, 255, 200));
            }
        });
        // Add components
        card.add(imageLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(button);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServicesFrame frame = new ServicesFrame();
            frame.setVisible(true);
        });
    }
}
