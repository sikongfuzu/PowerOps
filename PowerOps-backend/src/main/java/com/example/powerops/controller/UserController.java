package com.example.powerops.controller;

import com.example.powerops.common.Result;
import com.example.powerops.dto.LoginRequest;
import com.example.powerops.dto.UserRequest;
import com.example.powerops.service.UserService;
import com.example.powerops.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统用户 Controller
 * 提供登录、用户 CRUD 接口
 * 角色：ADMIN（管理员）/ OPERATOR（运维人员）/ MERCHANT（商户）
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     * @param request 包含 username/password
     * @return LoginVO（含 userId, username, realName, roleCode, token）
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        LoginVO loginVO = userService.login(request);
        return Result.success(loginVO);
    }

    /**
     * 分页查询用户列表（返回的 UserVO 不包含密码）
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String roleCode,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = userService.listUsers(username, roleCode, status, pageNum, pageSize);
        return Result.success(data);
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<?> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<?> createUser(@Validated(UserRequest.CreateGroup.class) @RequestBody UserRequest request) {
        userService.createUser(request);
        return Result.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/{id}")
    public Result<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        request.setUserId(id);
        userService.updateUser(request);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
