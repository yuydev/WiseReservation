package com.wise.reservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wise.reservation.model.entity.Alert;
import com.wise.reservation.model.entity.TransportOrder;
import com.wise.reservation.repository.AlertMapper;
import com.wise.reservation.repository.TransportOrderMapper;
import com.wise.reservation.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransportOrderMapper transportOrderMapper;
    private final AlertMapper alertMapper;

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> overview = new HashMap<>();

        // 今日吞吐量 (模拟数据)
        overview.put("todayThroughput", "1,284.5");

        // 作业车辆统计
        Long activeVehicles = transportOrderMapper.selectCount(
            new LambdaQueryWrapper<TransportOrder>().eq(TransportOrder::getStatus, "in_transit")
        );
        Long totalVehicles = 16L;
        overview.put("activeVehicles", activeVehicles);
        overview.put("totalVehicles", totalVehicles);

        // 待办预警
        List<Alert> alerts = alertMapper.selectList(
            new LambdaQueryWrapper<Alert>()
                .eq(Alert::getStatus, "unread")
                .orderByDesc(Alert::getCreateTime)
                .last("LIMIT 5")
        );
        overview.put("alerts", alerts);
        overview.put("unreadCount", alerts.size());

        return overview;
    }
}
