package com.example.powerops.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户信息VO（不包含密码）
 */
@Data
public class UserVO {
    private Long userId;
    private String username;
    private String realName;
    private String phone;
    private String roleCode;
    private Integer status;
    private LocalDateTime createTime;
}
