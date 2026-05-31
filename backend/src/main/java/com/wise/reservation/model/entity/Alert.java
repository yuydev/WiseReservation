package com.wise.reservation.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("alert")
public class Alert {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String icon;

    /**
     * 类型: warning-预警, pending-待办, info-通知
     */
    private String type;

    /**
     * 状态: unread-未读, read-已读, handled-已处理
     */
    private String status;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
