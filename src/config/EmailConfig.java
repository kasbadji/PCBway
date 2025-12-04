package config;

public class EmailConfig {
    // Enable/Disable email sending (set to false to only save to database)
    public static final boolean EMAIL_ENABLED = true; // Change to true when email is configured

    // SMTP Configuration
    // For Gmail: Use "smtp.gmail.com" with port 587 or 465
    // For Outlook: Use "smtp-mail.outlook.com" with port 587
    // For Yahoo: Use "smtp.mail.yahoo.com" with port 587

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "587"; // Use 465 for SSL, 587 for TLS
    public static final boolean SMTP_AUTH = true;
    public static final boolean SMTP_STARTTLS = true;
    public static final boolean SMTP_SSL = false; // Set to true if using port 465

    // Email credentials - CHANGE THESE TO YOUR ACTUAL EMAIL SETTINGS
    // For Gmail: You MUST use an "App Password" not your regular password
    // Generate at: https://myaccount.google.com/apppasswords
    // Steps: Google Account > Security > 2-Step Verification > App passwords
    public static final String EMAIL_USERNAME = "pcbway123@gmail.com";
    public static final String EMAIL_PASSWORD = "hnsd powk oetb kaco";

    // Recipient email (where contact form messages will be sent)
    public static final String RECIPIENT_EMAIL = "support@pcbway.com";

    // Email settings
    public static final String EMAIL_FROM_NAME = "PCBway Contact Form";

    /**
     * Check if email is properly configured
     */
    public static boolean isConfigured() {
        return EMAIL_ENABLED &&
                !EMAIL_USERNAME.equals("your-email@gmail.com") &&
                !EMAIL_PASSWORD.contains("your-") &&
                !EMAIL_PASSWORD.contains("app-password");
    }

    /**
     * Get configuration status message
     */
    public static String getConfigStatus() {
        if (!EMAIL_ENABLED) {
            return "Email disabled - Messages will only be saved to database";
        } else if (isConfigured()) {
            return "Email configured: " + EMAIL_USERNAME;
        } else {
            return "Email enabled but not configured - Update src/config/EmailConfig.java";
        }
    }
}
