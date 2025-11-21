package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import styles.RoundedBorder;

public class ContactFrame extends JFrame {
    public ContactFrame() {
        setTitle("Contact");
        setSize(1400, 900); // Taille ajustée
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // Add navbar
        add(createNavBar(), BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(40, 0, 0, 0);

        // Container pour titre et formulaire
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);

        JLabel title = new JLabel("CONTACT");
        title.setFont(new Font("SansSerif", Font.BOLD, 42));
        title.setForeground(new Color(34, 139, 34));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        mainContainer.add(title);
        mainContainer.add(createContactForm());

        contentPanel.add(mainContainer, gbc);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createContactForm() {
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)));
        formPanel.setPreferredSize(new Dimension(500, 480));

        // Name field
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(34, 139, 34));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JTextField nameField = createTextField();

        // Surname field
        JLabel surnameLabel = new JLabel("Surname");
        surnameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        surnameLabel.setForeground(new Color(34, 139, 34));
        surnameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JTextField surnameField = createTextField();

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(34, 139, 34));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JTextField emailField = createTextField();

        // Message field
        JLabel messageLabel = new JLabel("Message");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageLabel.setForeground(new Color(34, 139, 34));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JTextArea messageArea = new JTextArea(3, 20);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
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
        messageScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        messageScroll.setPreferredSize(new Dimension(400, 70));
        messageScroll.setMaximumSize(new Dimension(400, 70));
        messageScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Submit button avec validation
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        submitButton.setForeground(new Color(0, 100, 0));
        submitButton.setBackground(new Color(210, 240, 210));
        submitButton.setPreferredSize(new Dimension(400, 40));
        submitButton.setMaximumSize(new Dimension(400, 40));
        submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> {
            // Validation des champs
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();
            String message = messageArea.getText().trim();

            // Vérifier si les champs sont vides ou contiennent "Value"
            if (name.isEmpty() || name.equals("Value")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter your name!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                nameField.requestFocus();
                return;
            }

            if (surname.isEmpty() || surname.equals("Value")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter your surname!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                surnameField.requestFocus();
                return;
            }

            if (email.isEmpty() || email.equals("Value")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter your email!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                emailField.requestFocus();
                return;
            }

            // Validation simple de l'email
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid email address!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                emailField.requestFocus();
                return;
            }

            if (message.isEmpty() || message.equals("Value")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter your message!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                messageArea.requestFocus();
                return;
            }

            // Si tous les champs sont valides
            JOptionPane.showMessageDialog(this,
                    "Message sent successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Réinitialiser les champs après l'envoi
            nameField.setText("Value");
            nameField.setForeground(Color.LIGHT_GRAY);
            surnameField.setText("Value");
            surnameField.setForeground(Color.LIGHT_GRAY);
            emailField.setText("Value");
            emailField.setForeground(Color.LIGHT_GRAY);
            messageArea.setText("Value");
            messageArea.setForeground(Color.LIGHT_GRAY);
        });

        // Add components with spacing réduit
        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12))); // Espacement réduit

        formPanel.add(surnameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(surnameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        formPanel.add(messageLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(messageScroll);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(submitButton);

        formContainer.add(formPanel);
        return formContainer;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setPreferredSize(new Dimension(400, 38)); // Taille réduite
        field.setMaximumSize(new Dimension(400, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setText("Value");
        field.setForeground(Color.LIGHT_GRAY);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals("Value")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText("Value");
                    field.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        return field;
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

        // Right section
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
