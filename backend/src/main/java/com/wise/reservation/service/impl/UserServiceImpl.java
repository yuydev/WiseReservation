package com.wise.reservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wise.reservation.model.entity.TransportOrder;
import com.wise.reservation.model.entity.User;
import com.wise.reservation.repository.TransportOrderMapper;
import com.wise.reservation.repository.UserMapper;
import com.wise.reservation.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final TransportOrderMapper transportOrderMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public User getProfile(String token) {
        Long userId = getUserIdFromToken(token);
        return userMapper.selectById(userId);
    }

    @Override
    public Map<String, Object> getStats(String token) {
        Long userId = getUserIdFromToken(token);
        Map<String, Object> stats = new HashMap<>();

        Long totalOrders = transportOrderMapper.selectCount(
            new LambdaQueryWrapper<TransportOrder>().eq(TransportOrder::getDriverId, userId)
        );
        Long completedOrders = transportOrderMapper.selectCount(
            new LambdaQueryWrapper<TransportOrder>()
                .eq(TransportOrder::getDriverId, userId)
                .eq(TransportOrder::getStatus, "completed")
        );

        stats.put("totalOrders", totalOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("rating", "4.9");
        stats.put("thisMonth", 23);

        return stats;
    }

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String subject = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().getSubject();
        return Long.parseLong(subject);
    }
}
