package com.microservices.apigateway.services.impl;

import com.microservices.apigateway.repository.UserRepository;
import com.microservices.apigateway.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("No User Found!"));
    }
}
