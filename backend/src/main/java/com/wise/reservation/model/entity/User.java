package com.wise.reservation.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String openid;

    private String name;

    private String phone;

    private String avatarUrl;

    private String company;

    /**
     * 角色: admin-管理员, driver-司机, security-安保, dispatcher-调度, department-事业部
     */
    private String role;

    private String roleName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
