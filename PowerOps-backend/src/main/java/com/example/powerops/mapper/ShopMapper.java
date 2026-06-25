package com.example.powerops.mapper;

import com.example.powerops.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商铺Mapper
 */
@Mapper
public interface ShopMapper {

    /**
     * 根据ID查询商铺
     */
    Shop findById(@Param("shopId") Long shopId);

    /**
     * 查询商铺列表（分页）
     */
    List<Shop> listShops(@Param("shopName") String shopName,
                        @Param("building") String building,
                        @Param("businessType") String businessType,
                        @Param("status") Integer status,
                        @Param("offset") int offset,
                        @Param("limit") int limit);

    /**
     * 查询商铺总数
     */
    long countShops(@Param("shopName") String shopName,
                   @Param("building") String building,
                   @Param("businessType") String businessType,
                   @Param("status") Integer status);

    /**
     * 新增商铺
     */
    int insert(Shop shop);

    /**
     * 更新商铺
     */
    int update(Shop shop);

    /**
     * 删除商铺
     */
    int deleteById(@Param("shopId") Long shopId);
}
