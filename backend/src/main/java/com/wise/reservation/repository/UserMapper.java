package com.wise.reservation.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wise.reservation.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
