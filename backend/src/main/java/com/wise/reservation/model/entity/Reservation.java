package com.wise.reservation.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("reservation")
public class Reservation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long supplierId;

    private String supplierName;

    private String materialName;

    private String plateNumber;

    private String driverName;

    private String driverPhone;

    private LocalDateTime appointmentTime;

    /**
     * 状态: pending-待审批, approved-已通过, rejected-已拒绝, completed-已完成, cancelled-已取消
     */
    private String status;

    private String remark;

    private Long createdBy;

    private Long approvedBy;

    private LocalDateTime approvedTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
