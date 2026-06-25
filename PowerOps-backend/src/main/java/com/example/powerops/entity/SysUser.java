package com.example.powerops.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统用户实体
 * 对应 SYS_USER 表，支持三种角色：ADMIN（管理员）、OPERATOR（运维人员）、MERCHANT（商户）
 * status: 0-禁用, 1-启用
 */
@Data
public class SysUser {
    private Long userId;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String roleCode;
    private Integer status;
    private LocalDateTime createTime;
}
