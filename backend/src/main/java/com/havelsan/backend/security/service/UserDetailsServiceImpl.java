package com.havelsan.backend.security.service;

import com.havelsan.backend.model.User;
import com.havelsan.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(null);

        return  UserDetailsImpl.build(user);
    }
}
