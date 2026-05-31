package com.wise.reservation.service;

import java.util.Map;

public interface AuthService {
    Map<String, Object> login(String code);
}
