package com.example.powerops.mapper;

import com.example.powerops.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     */
    SysUser findByUsername(@Param("username") String username);

    /**
     * 根据ID查询用户
     */
    SysUser findById(@Param("userId") Long userId);

    /**
     * 查询用户列表（分页）
     */
    List<SysUser> listUsers(@Param("username") String username, 
                           @Param("roleCode") String roleCode, 
                           @Param("status") Integer status,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    /**
     * 查询用户总数
     */
    long countUsers(@Param("username") String username, 
                   @Param("roleCode") String roleCode, 
                   @Param("status") Integer status);

    /**
     * 新增用户
     */
    int insert(SysUser user);

    /**
     * 更新用户
     */
    int update(SysUser user);

    /**
     * 删除用户
     */
    int deleteById(@Param("userId") Long userId);
}
