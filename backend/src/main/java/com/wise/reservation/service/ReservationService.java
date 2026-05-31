package com.wise.reservation.service;

import com.wise.reservation.common.PageResult;
import com.wise.reservation.model.entity.Reservation;

public interface ReservationService {
    PageResult<Reservation> list(String status, String keyword, int page, int size);
    Reservation getById(Long id);
    Reservation create(Reservation reservation);
    void updateStatus(Long id, String status);
    void delete(Long id);
}
