package com.example.powerops.service;

import com.example.powerops.dto.LoginRequest;
import com.example.powerops.dto.UserRequest;
import com.example.powerops.vo.LoginVO;
import com.example.powerops.vo.UserVO;

import java.util.Map;

/**
 * 用户Service
 */
public interface UserService {

    /**
     * 用户登录
     */
    LoginVO login(LoginRequest request);

    /**
     * 分页查询用户列表
     */
    Map<String, Object> listUsers(String username, String roleCode, Integer status, int pageNum, int pageSize);

    /**
     * 根据ID查询用户
     */
    UserVO getUserById(Long userId);

    /**
     * 新增用户
     */
    void createUser(UserRequest request);

    /**
     * 更新用户
     */
    void updateUser(UserRequest request);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);
}
