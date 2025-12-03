package com.parmida98.school_webpage.user;

import com.parmida98.school_webpage.user.authority.UserRole;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

/** Entity - Separation of Concerns
 * This SHOULD NOT implement CustomUserDetails
 * Handle CustomUserDetails separately as its own class for better SoC
 * Should however, reflect CustomUserDetails Variables as best practice
 * */

@Table(name = "users")
@Entity
public class CustomUser {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(unique = true, nullable = false)
    private String username;
    @Setter
    @Column(nullable = false)
    private String password;
    @Setter
    private boolean isAccountNonExpired;
    @Setter
    private boolean isAccountNonLocked;
    @Setter
    private boolean isCredentialsNonExpired;
    @Setter
    private boolean isEnabled;

    @Setter
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER) // Fetch Immediately
    @Enumerated(value = EnumType.STRING)
    private Set<UserRole> roles;

    // Constructors
    public CustomUser() {}
    public CustomUser(String username, String password, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled, Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

}
