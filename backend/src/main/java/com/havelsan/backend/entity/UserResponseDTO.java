package com.havelsan.backend.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private  String token;
}
