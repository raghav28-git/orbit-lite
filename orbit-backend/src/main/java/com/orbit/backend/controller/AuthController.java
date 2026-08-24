package com.orbit.backend.controller;

import com.orbit.backend.dto.RegisterRequest;
import com.orbit.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(
            @RequestBody RegisterRequest request
    ) {
        authService.register(
                request.getEmail(),
                request.getPassword()
        );

        return Map.of(
                "message",
                "User registered successfully"
        );
    }
}