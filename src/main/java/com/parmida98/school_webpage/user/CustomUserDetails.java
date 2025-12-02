package com.parmida98.school_webpage.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/** CustomUserDetails - Custom Implementation
 *   Why:
 *      - SoC, offload the CustomUser: Entity
 *      - Override for Custom Logic (e.g: loading authorities through Enum)
 *      - Spring handles Authentication Logic behind the scenes using CustomUserDetails class
 *      - Entity preferably LIVES within CustomUserDetails
 * */

public class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final CustomUser customUser;

    public CustomUserDetails(CustomUser customUser) {
        this.customUser = customUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        customUser.getRoles().forEach(
                userRole -> authorities.addAll(userRole.getUserAuthorities())
        );

        return Collections.unmodifiableSet(authorities); // Make List 'final' through 'unmodifiable'
    }

    @Override
    public String getPassword() {
        return customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return customUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return customUser.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return customUser.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return customUser.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return customUser.isEnabled();
    }

    public CustomUser getCustomUser() {
        return customUser;
    }
}
