package model;

public class User {
    private String email;
    private String password;
    private String fullname;
    private String address; 
    private String phonenumber;
    
    public User() {
        this.email = "";
        this.password = "";
        this.fullname = "";
        this.address = "";
        this.phonenumber = "";
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.fullname = "";
        this.address = "";
        this.phonenumber = "";
    }




    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullname() { return fullname; }
    public String getAddress() { return address; } 
    public String getPhonenumber() { return phonenumber; }
    
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public void setAddress(String address) { this.address = address; } 
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
    
}