package com.example.powerops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户新增/修改DTO
 */
@Data
public class UserRequest {
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String realName;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "^(ADMIN|OPERATOR|MERCHANT)$", message = "角色只能是ADMIN、OPERATOR或MERCHANT")
    private String roleCode;

    private Integer status = 1; // 默认启用
}
