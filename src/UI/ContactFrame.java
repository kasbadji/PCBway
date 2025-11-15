package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import styles.RoundedBorder;

public class ContactFrame extends JFrame {
    public ContactFrame() {
        setTitle("Contact");
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
        JLabel title = new JLabel("CONTACT", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(0, 100, 0));
        title.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));
        contentPanel.add(title, BorderLayout.NORTH);

        // Add contact form
        contentPanel.add(createContactForm(), BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createContactForm() {
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(50, 60, 50, 60)));
        formPanel.setPreferredSize(new Dimension(800, 550));

        // Name field
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(34, 139, 34));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        nameField.setPreferredSize(new Dimension(680, 45));
        nameField.setMaximumSize(new Dimension(680, 45));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Placeholder
        nameField.setText("Value");
        nameField.setForeground(Color.LIGHT_GRAY);
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Value")) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Value");
                    nameField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        // Surname field
        JLabel surnameLabel = new JLabel("Surname");
        surnameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        surnameLabel.setForeground(new Color(34, 139, 34));
        surnameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField surnameField = new JTextField();
        surnameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        surnameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        surnameField.setPreferredSize(new Dimension(680, 45));
        surnameField.setMaximumSize(new Dimension(680, 45));
        surnameField.setAlignmentX(Component.LEFT_ALIGNMENT);


        surnameField.setText("Value");
        surnameField.setForeground(Color.LIGHT_GRAY);
        surnameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (surnameField.getText().equals("Value")) {
                    surnameField.setText("");
                    surnameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (surnameField.getText().isEmpty()) {
                    surnameField.setText("Value");
                    surnameField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(34, 139, 34));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        emailField.setPreferredSize(new Dimension(680, 45));
        emailField.setMaximumSize(new Dimension(680, 45));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField.setText("Value");
        emailField.setForeground(Color.LIGHT_GRAY);
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Value")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Value");
                    emailField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        // Message field
        JLabel messageLabel = new JLabel("Message");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(34, 139, 34));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea messageArea = new JTextArea(3, 20);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageArea.setText("Value");
        messageArea.setForeground(Color.LIGHT_GRAY);
        messageArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageArea.getText().equals("Value")) {
                    messageArea.setText("");
                    messageArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (messageArea.getText().isEmpty()) {
                    messageArea.setText("Value");
                    messageArea.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        messageScroll.setPreferredSize(new Dimension(680, 80));
        messageScroll.setMaximumSize(new Dimension(680, 80));
        messageScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setForeground(new Color(0, 100, 0));
        submitButton.setBackground(new Color(210, 240, 210));
        submitButton.setPreferredSize(new Dimension(680, 45));
        submitButton.setMaximumSize(new Dimension(680, 45));
        submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Message sent successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Add components with spacing
        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(surnameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(surnameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(messageLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(messageScroll);
        formPanel.add(Box.createRigidArea(new Dimension(0, 18)));

        formPanel.add(submitButton);

        formContainer.add(formPanel);
        return formContainer;
    }

    private JPanel createNavBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // Navigation Links
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        navPanel.setBackground(Color.WHITE);

        String[] navItems = { "SERVICES", "PCB PRINTING", "ELECTRONICS", "CONTACT" };
        for (String item : navItems) {
            JLabel link = new JLabel(item);
            link.setFont(new Font("SansSerif", Font.BOLD, 14));

            // Highlight current page
            if (item.equals("CONTACT")) {
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
                    if (item.equals("CONTACT")) {
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
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);
        JTextField searchField = new JTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.setFont(getFont());
                    g2.drawString("Search...", 10, 18);
                    g2.dispose();
                }
            }
        };
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(20, new Color(34, 139, 34), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        searchField.setPreferredSize(new Dimension(180, 32));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("SansSerif", Font.PLAIN, 22));
        userIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        ContactFrame.this,
                        "Do you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    SignupFrame.getUserService().logout();
                    JOptionPane.showMessageDialog(ContactFrame.this,
                            "You have been logged out successfully.",
                            "Logout",
                            JOptionPane.INFORMATION_MESSAGE);
                    new LoginFrame().setVisible(true);
                    dispose();
                }
            }
        });

        JLabel cartIcon = new JLabel("🛒");
        cartIcon.setFont(new Font("SansSerif", Font.PLAIN, 22));
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
                    // Already on Contact page
                    break;
                default:
                    break;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ContactFrame().setVisible(true);
        });
    }
}
