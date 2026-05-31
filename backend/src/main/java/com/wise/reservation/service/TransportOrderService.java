package com.wise.reservation.service;

import com.wise.reservation.common.PageResult;
import com.wise.reservation.model.entity.TransportOrder;

public interface TransportOrderService {
    PageResult<TransportOrder> listAvailable(String filter, int page, int size);
    TransportOrder getById(Long id);
    void grabOrder(Long id, String token);
    PageResult<TransportOrder> myOrders(String token, int page, int size);
}
