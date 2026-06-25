package com.example.powerops.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class    LoginVO {
    private Long userId;
    private String username;
    private String realName;
    private String roleCode;
    private String token; // 简单token，实际项目建议使用JWT
}
