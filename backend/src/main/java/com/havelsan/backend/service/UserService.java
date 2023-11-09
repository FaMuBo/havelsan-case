package com.havelsan.backend.service;

import com.havelsan.backend.entity.UserDTO;
import com.havelsan.backend.entity.UserResponseDTO;

public interface UserService {
    UserResponseDTO login(UserDTO userDTO);

    UserDTO register(UserDTO userDTO);
}
