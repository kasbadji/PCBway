package config;

public class EmailConfig {

    public static final boolean EMAIL_ENABLED = true;

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "587";
    public static final boolean SMTP_AUTH = true;
    public static final boolean SMTP_STARTTLS = true;
    public static final boolean SMTP_SSL = false;

    public static final String EMAIL_USERNAME = "pcbway123@gmail.com";
    public static final String EMAIL_PASSWORD = "hnsd powk oetb kaco";
    public static final String RECIPIENT_EMAIL = "support@pcbway.com";
    public static final String EMAIL_FROM_NAME = "PCBway Contact Form";

    public static boolean isConfigured() {
        return EMAIL_ENABLED &&
                !EMAIL_USERNAME.equals("your-email@gmail.com") &&
                !EMAIL_PASSWORD.contains("your-") &&
                !EMAIL_PASSWORD.contains("app-password");
    }

    public static String getConfigStatus() {
        if (!EMAIL_ENABLED) {
            return "Email disabled - Messages will only be saved to database";
        } else if (isConfigured()) {
            return "Email configured: " + EMAIL_USERNAME;
        } else {
            return "Email enabled but not configured - Update EmailConfig.java";
        }
    }
}
