package com.wise.reservation.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transport_order")
public class TransportOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private String title;

    private String description;

    private String destination;

    private String materialInfo;

    private BigDecimal price;

    private Double distance;

    private LocalDateTime deadline;

    /**
     * 类型: normal-常规, urgent-紧急, delivery-送货
     */
    private String type;

    /**
     * 状态: available-可抢, grabbed-已抢, in_transit-运输中, completed-已完成, cancelled-已取消
     */
    private String status;

    private Long publisherId;

    private Long driverId;

    private LocalDateTime grabbedTime;

    private LocalDateTime completedTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
