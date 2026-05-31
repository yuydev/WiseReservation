package com.wise.reservation.controller;

import com.wise.reservation.common.PageResult;
import com.wise.reservation.common.Result;
import com.wise.reservation.model.entity.Reservation;
import com.wise.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public Result<PageResult<Reservation>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(reservationService.list(status, keyword, page, size));
    }

    @GetMapping("/{id}")
    public Result<Reservation> getById(@PathVariable Long id) {
        return Result.success(reservationService.getById(id));
    }

    @PostMapping
    public Result<Reservation> create(@RequestBody Reservation reservation) {
        return Result.success(reservationService.create(reservation));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        reservationService.updateStatus(id, body.get("status"));
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return Result.success();
    }
}
