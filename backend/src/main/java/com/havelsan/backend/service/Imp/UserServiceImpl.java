package com.havelsan.backend.service.Imp;

import com.havelsan.backend.entity.UserDTO;
import com.havelsan.backend.entity.UserResponseDTO;
import com.havelsan.backend.model.User;
import com.havelsan.backend.repository.UserRepository;
import com.havelsan.backend.security.jwt.JwtUtils;
import com.havelsan.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponseDTO login(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return UserResponseDTO.builder()
                .token("Bearer " + jwt)
                .build();
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
       this.userRepository.save(user);
        return userDTO;
    }
}
