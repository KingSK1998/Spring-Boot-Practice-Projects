package com.security.SpringSecurityAPI.model;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security cannot work with the User class directly.
 * It needs a wrapper, we need to implement UserDetails interface.
 * Any class that implements UserDetails interface is what Spring Security uses to get core user information.
 * This class is serializable.
 */
public class CurrentUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private User user;

    public CurrentUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}