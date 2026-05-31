package com.wise.reservation.controller;

import com.wise.reservation.common.Result;
import com.wise.reservation.model.dto.LoginRequest;
import com.wise.reservation.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody @Valid LoginRequest request) {
        return Result.success(authService.login(request.getCode()));
    }
}
