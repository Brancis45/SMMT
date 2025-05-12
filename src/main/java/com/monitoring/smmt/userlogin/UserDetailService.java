package com.monitoring.smmt.userlogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //gets username and pass for comparison
    //collection empty because no roles used
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.monitoring.smmt.userlogin.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
