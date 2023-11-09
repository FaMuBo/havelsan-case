package com.havelsan.backend.controller;


import com.havelsan.backend.entity.UserDTO;
import com.havelsan.backend.entity.UserResponseDTO;
import com.havelsan.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(this.userService.login(userDTO));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(this.userService.register(userDTO));
    }
}
