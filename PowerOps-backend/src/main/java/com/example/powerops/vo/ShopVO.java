package com.example.powerops.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商铺视图对象，供前端展示
 *
 * <p>从 Shop 实体转换而来，字段相同。</p>
 * <p>可直接返回实体本身，VO 在此保留以统一模式，</p>
 * <p>便于未来扩展（如隐藏 contactPhone 部分数字）。</p>
 */
@Data
public class ShopVO {
    /** 商铺ID */
    private Long shopId;

    /** 商铺编号 */
    private String shopNo;

    /** 商铺名称 */
    private String shopName;

    /** 所属楼栋 */
    private String building;

    /** 所在楼层 */
    private String floorNo;

    /** 业态类型：餐饮 / 零售 / 服务 */
    private String businessType;

    /** 联系人姓名 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 商铺状态：0-停用, 1-营业中, 2-装修中 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}
