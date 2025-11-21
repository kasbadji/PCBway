package UI;

import javax.swing.*;
import styles.RoundedBorder;
import styles.RoundedButton;
import service.UserService;
import service.UserServiceMongo;
import java.awt.*;

public class SignupFrame extends JFrame {
    
    private static UserService userService = new UserServiceMongo();

    public SignupFrame() {
        setTitle("Sign Up");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(2, 158, 54));
        leftPanel.setPreferredSize(new Dimension(960, 1080));

        JLabel title = new JLabel("YourPCB");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 80));

        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(title);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);

        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel signUpLabel = new JLabel("SIGN UP");
        signUpLabel.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel emailLabel = new JLabel("Email address");
        JTextField emailField = new JTextField(20);

        emailField.setBorder(new RoundedBorder(10, new Color(200, 200, 200), 2));
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        emailField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                emailField.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                emailField.setBackground(new Color(255, 255, 255));
            }
        });

        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(20);

        passwordField.setBorder(new RoundedBorder(10, new Color(200, 200, 200), 2));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        passwordField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                passwordField.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                passwordField.setBackground(new Color(255, 255, 255));
            }
        });

        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setBackground(Color.WHITE);
        rememberMe.setFocusPainted(false);

        RoundedButton registerBtn = new RoundedButton("Register Account", 25);
        registerBtn.setBackground(new Color(200, 255, 200));
        registerBtn.setForeground(new Color(0, 120, 0));
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerBtn.setHoverColor(new Color(180, 240, 180));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                userService.register(email, password);
                JOptionPane.showMessageDialog(this, 
                    "Account created successfully!\nYou can now login.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                SwingUtilities.invokeLater(() -> {
                    new LoginFrame().setVisible(true);
                    this.dispose();
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Registration failed: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });



        JLabel loginLabel = new JLabel("Have an account ?");
        JButton loginBtn = new JButton("Sign in here");
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setForeground(new Color(0, 102, 204));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
                this.dispose();
            });
        });

        JPanel linkPanel = new JPanel();
        linkPanel.setBackground(Color.WHITE);
        linkPanel.add(loginLabel);
        linkPanel.add(loginBtn);


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0);
        rightPanel.add(signUpLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 2, 0);
        rightPanel.add(emailLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.insets = new Insets(2, 0, 10, 0);
        rightPanel.add(emailField, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 2, 0);
        rightPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.insets = new Insets(2, 0, 10, 0);
        rightPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        rightPanel.add(rememberMe, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.ipady = 10;
        rightPanel.add(registerBtn, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.ipady = 0;
        rightPanel.add(linkPanel, gbc);


        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    public static UserService getUserService() {
        return userService;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignupFrame frame = new SignupFrame();
            frame.setVisible(true);
        });
    }
}
