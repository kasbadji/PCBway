package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.ContactMessage;
import service.ContactMessageService;
import service.CartServiceMongo;
import styles.RoundedBorder;
import styles.RoundedButton;
import config.EmailConfig;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class ContactFrame extends JFrame {
    private CartServiceMongo cartService;
    private JLabel cartCountLabel;

    public ContactFrame() {
        this.cartService = CartServiceMongo.getInstance();
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
        
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        if (cartCountLabel != null) {
            int itemCount = cartService.getTotalItemCount();
            cartCountLabel.setText("🛒(" + itemCount + ")");
        }
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

            // Disable button while sending
            submitButton.setEnabled(false);
            submitButton.setText("Sending...");

            // Send email via API
            sendEmailToAPI(email, message, submitButton, nameField, surnameField, emailField, messageArea);
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
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 0));
        navPanel.setBackground(Color.WHITE);

        String[] navItems = { "SERVICES", "PCB PRINTING", "ELECTRONICS", "CONTACT" };
        for (String item : navItems) {
            JLabel link = new JLabel(item);
            link.setFont(new Font("SansSerif", Font.BOLD, 20));

            if (item.equals("CONTACT")) {
                link.setForeground(new Color(0, 100, 0));
            } else {
                link.setForeground(new Color(2, 158, 54));
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
                        link.setForeground(new Color(2, 158, 54));
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

        RoundedButton logoutButton = new RoundedButton("Logout", 20);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setHoverColor(new Color(200, 35, 51));
        logoutButton.setPreferredSize(new Dimension(90, 35));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
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
                    JOptionPane.showMessageDialog(ContactFrame.this,
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

    private void sendEmailToAPI(String email, String message, JButton submitButton,
            JTextField nameField, JTextField surnameField,
            JTextField emailField, JTextArea messageArea) {
        new Thread(() -> {
            try {
                // Get form values
                String name = nameField.getText().equals("Value") ? "" : nameField.getText();
                String surname = surnameField.getText().equals("Value") ? "" : surnameField.getText();

                // Create and save contact message to database first
                ContactMessage contactMessage = new ContactMessage(name, surname, email, message);
                ContactMessageService contactService = ContactMessageService.getInstance();

                System.out.println("Saving contact message to database...");
                boolean savedToDb = contactService.saveContactMessage(contactMessage);

                if (!savedToDb) {
                    SwingUtilities.invokeLater(() -> {
                        submitButton.setEnabled(true);
                        submitButton.setText("Submit");
                        JOptionPane.showMessageDialog(this,
                                "Failed to save message to database. Please try again.",
                                "Database Error",
                                JOptionPane.ERROR_MESSAGE);
                    });
                    return;
                }

                // Send email using JavaMail
                System.out.println("Sending email via SMTP...");
                System.out.println(EmailConfig.getConfigStatus());

                boolean emailSent = false;
                String errorMessage = null;

                // Check if email is enabled and configured
                if (!EmailConfig.EMAIL_ENABLED) {
                    System.out.println("ℹ Email disabled - message saved to database only");
                    errorMessage = "Email sending is disabled (see EmailConfig.EMAIL_ENABLED)";
                    emailSent = false; // Mark as not sent
                } else if (!EmailConfig.isConfigured()) {
                    System.out.println("⚠ Email not configured - skipping email send");
                    errorMessage = "Email configuration needed. Update src/config/EmailConfig.java with your SMTP settings.";
                } else {
                    try {
                        // Setup mail server properties with enhanced configuration
                        Properties props = new Properties();
                        props.put("mail.smtp.host", EmailConfig.SMTP_HOST);
                        props.put("mail.smtp.port", EmailConfig.SMTP_PORT);
                        props.put("mail.smtp.auth", String.valueOf(EmailConfig.SMTP_AUTH));
                        props.put("mail.smtp.starttls.enable", String.valueOf(EmailConfig.SMTP_STARTTLS));
                        props.put("mail.smtp.starttls.required", "false"); // Don't fail if STARTTLS unavailable

                        // SSL settings
                        if (EmailConfig.SMTP_SSL) {
                            props.put("mail.smtp.ssl.enable", "true");
                            props.put("mail.smtp.socketFactory.port", EmailConfig.SMTP_PORT);
                            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        }

                        // Timeouts
                        props.put("mail.smtp.connectiontimeout", "10000");
                        props.put("mail.smtp.timeout", "10000");
                        props.put("mail.smtp.writetimeout", "10000");

                        // Debug mode
                        props.put("mail.debug", "true");

                        // Create session with authentication
                        Session session = Session.getInstance(props, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(EmailConfig.EMAIL_USERNAME,
                                        EmailConfig.EMAIL_PASSWORD);
                            }
                        });

                        // Create email message
                        Message msg = new MimeMessage(session);
                        msg.setFrom(new InternetAddress(EmailConfig.EMAIL_USERNAME, EmailConfig.EMAIL_FROM_NAME));
                        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EmailConfig.RECIPIENT_EMAIL));
                        msg.setSubject("Contact Form Submission from " + name + " " + surname);

                        // Create email body
                        String emailBody = String.format(
                                "New Contact Form Submission\n" +
                                        "============================\n\n" +
                                        "Name: %s %s\n" +
                                        "Email: %s\n" +
                                        "Timestamp: %s\n\n" +
                                        "Message:\n%s\n\n" +
                                        "---\n" +
                                        "This message was sent via PCBway Contact Form",
                                name, surname, email,
                                contactMessage.getFormattedTimestamp(),
                                message);

                        msg.setText(emailBody);

                        // Send the email
                        Transport.send(msg);
                        emailSent = true;
                        System.out.println("✓ Email sent successfully via SMTP");

                    } catch (Exception e) {
                        System.err.println("✗ Email sending failed: " + e.getMessage());
                        e.printStackTrace();
                        errorMessage = "Email sending failed: " + e.getMessage();
                    }
                }

                // Update email sent status in database
                if (contactMessage.getId() != null) {
                    contactService.updateEmailSentStatus(contactMessage.getId(), emailSent);
                }

                final boolean finalEmailSent = emailSent;
                final String finalErrorMessage = errorMessage;

                SwingUtilities.invokeLater(() -> {
                    submitButton.setEnabled(true);
                    submitButton.setText("Submit");

                    if (finalEmailSent) {
                        // Success - both database and email
                        String successMessage = String.format(
                                "Message sent successfully!\n\n" +
                                        "✓ Saved to database at %s\n" +
                                        "✓ Email sent via SMTP\n" +
                                        "✓ Contact: %s %s (%s)",
                                contactMessage.getFormattedTimestamp(),
                                name, surname, email);

                        JOptionPane.showMessageDialog(this,
                                successMessage,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Reset form
                        nameField.setText("Value");
                        nameField.setForeground(Color.LIGHT_GRAY);
                        surnameField.setText("Value");
                        surnameField.setForeground(Color.LIGHT_GRAY);
                        emailField.setText("Value");
                        emailField.setForeground(Color.LIGHT_GRAY);
                        messageArea.setText("Value");
                        messageArea.setForeground(Color.LIGHT_GRAY);
                    } else if (!EmailConfig.EMAIL_ENABLED) {
                        // Database saved, email disabled
                        String successMessage = String.format(
                                "Message saved successfully!\n\n" +
                                        "✓ Saved to database at %s\n" +
                                        "ℹ Email sending is disabled\n" +
                                        "Contact: %s %s (%s)",
                                contactMessage.getFormattedTimestamp(),
                                name, surname, email);

                        JOptionPane.showMessageDialog(this,
                                successMessage,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Reset form
                        nameField.setText("Value");
                        nameField.setForeground(Color.LIGHT_GRAY);
                        surnameField.setText("Value");
                        surnameField.setForeground(Color.LIGHT_GRAY);
                        emailField.setText("Value");
                        emailField.setForeground(Color.LIGHT_GRAY);
                        messageArea.setText("Value");
                        messageArea.setForeground(Color.LIGHT_GRAY);
                    } else {
                        // Database saved but email failed
                        String partialMessage = String.format(
                                "Message saved to database but email sending failed.\n\n" +
                                        "✓ Saved to database at %s\n" +
                                        "✗ Email error: %s\n" +
                                        "Contact: %s %s (%s)",
                                contactMessage.getFormattedTimestamp(),
                                finalErrorMessage,
                                name, surname, email);

                        JOptionPane.showMessageDialog(this,
                                partialMessage,
                                "Partial Success",
                                JOptionPane.WARNING_MESSAGE);
                    }
                });

            } catch (Exception e) {
                System.out.println("Exception in contact form submission: " + e.getMessage());
                e.printStackTrace();

                SwingUtilities.invokeLater(() -> {
                    submitButton.setEnabled(true);
                    submitButton.setText("Submit");

                    JOptionPane.showMessageDialog(this,
                            "Failed to send message. Error: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
}
