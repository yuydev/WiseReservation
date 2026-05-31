package com.wise.reservation.service;

import com.wise.reservation.model.entity.User;
import java.util.Map;

public interface UserService {
    User getProfile(String token);
    Map<String, Object> getStats(String token);
}
