package com.wise.reservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wise.reservation.common.PageResult;
import com.wise.reservation.model.entity.TransportOrder;
import com.wise.reservation.repository.TransportOrderMapper;
import com.wise.reservation.service.TransportOrderService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransportOrderServiceImpl implements TransportOrderService {

    private final TransportOrderMapper transportOrderMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public PageResult<TransportOrder> listAvailable(String filter, int page, int size) {
        LambdaQueryWrapper<TransportOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransportOrder::getStatus, "available");

        switch (filter) {
            case "urgent":
                wrapper.eq(TransportOrder::getType, "urgent");
                break;
            case "nearby":
                wrapper.orderByAsc(TransportOrder::getDistance);
                break;
            case "highValue":
                wrapper.orderByDesc(TransportOrder::getPrice);
                break;
            default:
                wrapper.orderByDesc(TransportOrder::getCreateTime);
                break;
        }

        Page<TransportOrder> pageResult = transportOrderMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(pageResult.getRecords(), pageResult.getTotal(), pageResult.getCurrent(), pageResult.getPages());
    }

    @Override
    public TransportOrder getById(Long id) {
        return transportOrderMapper.selectById(id);
    }

    @Override
    public void grabOrder(Long id, String token) {
        Long userId = getUserIdFromToken(token);
        TransportOrder order = transportOrderMapper.selectById(id);

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!"available".equals(order.getStatus())) {
            throw new RuntimeException("订单已被抢走");
        }

        order.setStatus("grabbed");
        order.setDriverId(userId);
        order.setGrabbedTime(LocalDateTime.now());
        transportOrderMapper.updateById(order);
    }

    @Override
    public PageResult<TransportOrder> myOrders(String token, int page, int size) {
        Long userId = getUserIdFromToken(token);
        LambdaQueryWrapper<TransportOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransportOrder::getDriverId, userId);
        wrapper.orderByDesc(TransportOrder::getCreateTime);

        Page<TransportOrder> pageResult = transportOrderMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(pageResult.getRecords(), pageResult.getTotal(), pageResult.getCurrent(), pageResult.getPages());
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
