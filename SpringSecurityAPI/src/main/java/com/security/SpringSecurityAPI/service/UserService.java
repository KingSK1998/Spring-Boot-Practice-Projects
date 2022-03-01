package com.security.SpringSecurityAPI.service;

import com.security.SpringSecurityAPI.model.CurrentUserDetails;
import com.security.SpringSecurityAPI.model.User;
import com.security.SpringSecurityAPI.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{

    /**
     * Spring Secuirty will use the UserDetailsService to authenticate logged in users.
     * 
     * This service layer will exposes methods allowing us to register a new user,
     * send email to a user and authenticate an already registered user.
    */

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CurrentUserDetails(user);
    }
    
}
