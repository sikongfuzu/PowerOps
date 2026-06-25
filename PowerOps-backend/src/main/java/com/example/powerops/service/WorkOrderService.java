package com.example.powerops.service;

import com.example.powerops.entity.WorkOrder;

import java.util.Map;

/**
 * 工单管理 Service 接口
 * 流程：createOrder(待处理) → assignOrder(处理中) → completeOrder(已完成)
 * 简化版：completeOrder 无需 solution 参数
 */
public interface WorkOrderService {

    /** 分页查询工单列表 */
    Map<String, Object> listOrders(String orderType, String status, Long handlerId, String priority, int pageNum, int pageSize);

    /** 根据ID查询工单 */
    WorkOrder getOrderById(Long orderId);

    /** 创建工单（初始状态"待处理"，orderNo 自动生成） */
    void createOrder(WorkOrder workOrder);

    /** 指派工单：设置处理人，状态变更为"处理中" */
    void assignOrder(Long orderId, Long handlerId);

    /** 通用状态变更 */
    void updateOrderStatus(Long orderId, String status);

    /** 完成工单：状态变更为"已完成"，自动填充 finishTime */
    void completeOrder(Long orderId);

    /** 修改工单信息 */
    void updateOrder(WorkOrder workOrder);

    /** 删除工单 */
    void deleteOrder(Long orderId);
}
