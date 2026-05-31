package com.wise.reservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wise.reservation.common.PageResult;
import com.wise.reservation.model.entity.Reservation;
import com.wise.reservation.repository.ReservationMapper;
import com.wise.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;

    @Override
    public PageResult<Reservation> list(String status, String keyword, int page, int size) {
        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(status)) {
            wrapper.eq(Reservation::getStatus, status);
        }

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(Reservation::getOrderNo, keyword)
                .or()
                .like(Reservation::getSupplierName, keyword)
            );
        }

        wrapper.orderByDesc(Reservation::getCreateTime);

        Page<Reservation> pageResult = reservationMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(pageResult.getRecords(), pageResult.getTotal(), pageResult.getCurrent(), pageResult.getPages());
    }

    @Override
    public Reservation getById(Long id) {
        return reservationMapper.selectById(id);
    }

    @Override
    public Reservation create(Reservation reservation) {
        // 生成预约单号
        String orderNo = "YY" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        reservation.setOrderNo(orderNo);
        reservation.setStatus("pending");
        reservation.setCreateTime(LocalDateTime.now());
        reservationMapper.insert(reservation);
        return reservation;
    }

    @Override
    public void updateStatus(Long id, String status) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) {
            throw new RuntimeException("预约单不存在");
        }
        reservation.setStatus(status);
        reservation.setUpdateTime(LocalDateTime.now());
        if ("approved".equals(status) || "rejected".equals(status)) {
            reservation.setApprovedTime(LocalDateTime.now());
        }
        reservationMapper.updateById(reservation);
    }

    @Override
    public void delete(Long id) {
        reservationMapper.deleteById(id);
    }
}
