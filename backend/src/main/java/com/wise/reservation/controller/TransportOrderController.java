package com.wise.reservation.controller;

import com.wise.reservation.common.PageResult;
import com.wise.reservation.common.Result;
import com.wise.reservation.model.entity.TransportOrder;
import com.wise.reservation.service.TransportOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class TransportOrderController {

    private final TransportOrderService transportOrderService;

    @GetMapping("/available")
    public Result<PageResult<TransportOrder>> listAvailable(
            @RequestParam(defaultValue = "all") String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(transportOrderService.listAvailable(filter, page, size));
    }

    @GetMapping("/{id}")
    public Result<TransportOrder> getById(@PathVariable Long id) {
        return Result.success(transportOrderService.getById(id));
    }

    @PostMapping("/{id}/grab")
    public Result<Void> grabOrder(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        transportOrderService.grabOrder(id, token);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<PageResult<TransportOrder>> myOrders(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(transportOrderService.myOrders(token, page, size));
    }
}
