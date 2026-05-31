package com.wise.reservation.service.impl;

import com.wise.reservation.model.entity.User;
import com.wise.reservation.repository.UserMapper;
import com.wise.reservation.service.AuthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public Map<String, Object> login(String code) {
        // 在实际项目中，这里应该调用微信API获取openid
        // 这里简化处理，使用code作为模拟openid
        String openid = "mock_openid_" + code;

        // 查找或创建用户
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getOpenid, openid)
        );

        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setName("新用户");
            user.setRole("driver");
            user.setRoleName("司机");
            userMapper.insert(user);
        }

        // 生成JWT token
        String token = generateToken(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        return result;
    }

    private String generateToken(Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }
}
