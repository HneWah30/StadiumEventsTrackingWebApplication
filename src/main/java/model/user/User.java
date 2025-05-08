package model.user;

import java.io.Serializable;
import org.mindrot.jbcrypt.BCrypt;

public class User implements Serializable {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;

    public User() {}

    public User(int userId, String username, String email, String passwordHash, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(Role role) { this.role = role; }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }
    
    public String getRoleName() {
    return role != null ? role.name().toLowerCase() : "user";
}
}
