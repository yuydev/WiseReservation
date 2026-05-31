package com.wise.reservation.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wise.reservation.model.entity.Alert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlertMapper extends BaseMapper<Alert> {
}
