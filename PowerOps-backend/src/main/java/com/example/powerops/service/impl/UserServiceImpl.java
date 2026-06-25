package com.example.powerops.service.impl;

import com.example.powerops.common.BusinessException;
import com.example.powerops.common.Code;
import com.example.powerops.dto.LoginRequest;
import com.example.powerops.dto.UserRequest;
import com.example.powerops.entity.SysUser;
import com.example.powerops.mapper.UserMapper;
import com.example.powerops.service.UserService;
import com.example.powerops.vo.LoginVO;
import com.example.powerops.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public LoginVO login(LoginRequest request) {
        // 查询用户
        SysUser user = userMapper.findByUsername(request.getUsername());
        
        if (user == null) {
            throw new BusinessException(Code.UNAUTHORIZED, "用户名或密码错误");
        }

        // 验证状态
        if (user.getStatus() == 0) {
            throw new BusinessException(Code.FORBIDDEN, "账号已被禁用");
        }

        // 验证密码（实际项目应该使用BCrypt加密）
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessException(Code.UNAUTHORIZED, "用户名或密码错误");
        }

        // 生成简单token（实际项目建议使用JWT）
        String token = UUID.randomUUID().toString().replace("-", "");

        // 构建返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getUserId());
        loginVO.setUsername(user.getUsername());
        loginVO.setRealName(user.getRealName());
        loginVO.setRoleCode(user.getRoleCode());
        loginVO.setToken(token);

        log.info("用户登录成功: {}", user.getUsername());
        return loginVO;
    }

    @Override
    public Map<String, Object> listUsers(String username, String roleCode, Integer status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        
        List<SysUser> users = userMapper.listUsers(username, roleCode, status, offset, pageSize);
        long total = userMapper.countUsers(username, roleCode, status);

        // 转换为VO（去除密码）
        List<UserVO> userVOList = users.stream().map(this::convertToVO).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("list", userVOList);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    public UserVO getUserById(Long userId) {
        SysUser user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(Code.NOT_FOUND, "用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional
    public void createUser(UserRequest request) {
        // 检查用户名是否已存在
        SysUser existUser = userMapper.findByUsername(request.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 创建用户
        SysUser user = new SysUser();
        BeanUtils.copyProperties(request, user);
        
        int rows = userMapper.insert(user);
        if (rows == 0) {
            throw new BusinessException("创建用户失败");
        }

        log.info("创建用户成功: {}", request.getUsername());
    }

    @Override
    @Transactional
    public void updateUser(UserRequest request) {
        if (request.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }

        // 检查用户是否存在
        SysUser existUser = userMapper.findById(request.getUserId());
        if (existUser == null) {
            throw new BusinessException(Code.NOT_FOUND, "用户不存在");
        }

        // 更新用户
        SysUser user = new SysUser();
        BeanUtils.copyProperties(request, user);
        
        int rows = userMapper.update(user);
        if (rows == 0) {
            throw new BusinessException("更新用户失败");
        }

        log.info("更新用户成功: {}", request.getUsername());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        SysUser user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(Code.NOT_FOUND, "用户不存在");
        }

        int rows = userMapper.deleteById(userId);
        if (rows == 0) {
            throw new BusinessException("删除用户失败");
        }

        log.info("删除用户成功: {}", userId);
    }

    /**
     * 实体转VO
     */
    private UserVO convertToVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
