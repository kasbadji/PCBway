package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContactMessage {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String message;
    private LocalDateTime timestamp;
    private boolean emailSent;

    public ContactMessage() {
        this.timestamp = LocalDateTime.now();
        this.emailSent = false;
    }

    public ContactMessage(String name, String surname, String email, String message) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.emailSent = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isEmailSent() { return emailSent; }
    public void setEmailSent(boolean emailSent) { this.emailSent = emailSent; }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("ContactMessage{id='%s', name='%s', surname='%s', email='%s', timestamp='%s', emailSent=%b}",
                id, name, surname, email, getFormattedTimestamp(), emailSent);
    }
}