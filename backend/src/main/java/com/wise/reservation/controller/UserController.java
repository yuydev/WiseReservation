package com.wise.reservation.controller;

import com.wise.reservation.common.Result;
import com.wise.reservation.model.entity.User;
import com.wise.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public Result<User> getProfile(@RequestHeader("Authorization") String token) {
        return Result.success(userService.getProfile(token));
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@RequestHeader("Authorization") String token) {
        return Result.success(userService.getStats(token));
    }
}
