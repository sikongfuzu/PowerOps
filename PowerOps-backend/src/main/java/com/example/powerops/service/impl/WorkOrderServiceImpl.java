package com.example.powerops.service.impl;

import com.example.powerops.common.BusinessException;
import com.example.powerops.common.Code;
import com.example.powerops.entity.WorkOrder;
import com.example.powerops.mapper.WorkOrderMapper;
import com.example.powerops.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单管理 Service 实现
 * 流程：create(待处理) → assign(处理中) → complete(已完成)
 * 简化版：completeOrder 无需 solution，使用 updateStatus 完成状态变更
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderMapper workOrderMapper;

    @Override
    public Map<String, Object> listOrders(String orderType, String status, Long handlerId, String priority, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<WorkOrder> orders = workOrderMapper.listOrders(orderType, status, handlerId, priority, offset, pageSize);
        long total = workOrderMapper.countOrders(orderType, status, handlerId, priority);
        Map<String, Object> result = new HashMap<>();
        result.put("list", orders);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public WorkOrder getOrderById(Long orderId) {
        WorkOrder order = workOrderMapper.findById(orderId);
        if (order == null) throw new BusinessException(Code.NOT_FOUND, "工单不存在");
        return order;
    }

    @Override
    @Transactional
    public void createOrder(WorkOrder workOrder) {
        // 自动生成工单编号，格式：WO + yyyyMMdd + 4位流水
        try {
            String orderNo = workOrderMapper.generateOrderNo();
            workOrder.setOrderNo(orderNo);
        } catch (Exception e) {
            // 序列异常时使用时间戳兜底
            workOrder.setOrderNo("WO" + System.currentTimeMillis());
        }
        workOrder.setStatus("待处理");
        int rows = workOrderMapper.insert(workOrder);
        if (rows == 0) throw new BusinessException("创建工单失败");
        log.info("创建工单成功，工单号: {}", workOrder.getOrderNo());
    }

    @Override
    @Transactional
    public void assignOrder(Long orderId, Long handlerId) {
        WorkOrder order = workOrderMapper.findById(orderId);
        if (order == null) throw new BusinessException(Code.NOT_FOUND, "工单不存在");
        if (!"待处理".equals(order.getStatus())) throw new BusinessException("只能指派待处理的工单");
        int rows = workOrderMapper.assignOrder(orderId, handlerId);
        if (rows == 0) throw new BusinessException("指派工单失败");
        log.info("指派工单成功，工单ID: {}, 指派人: {}", orderId, handlerId);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        WorkOrder order = workOrderMapper.findById(orderId);
        if (order == null) throw new BusinessException(Code.NOT_FOUND, "工单不存在");
        int rows = workOrderMapper.updateStatus(orderId, status);
        if (rows == 0) throw new BusinessException("更新工单状态失败");
        log.info("更新工单状态成功，工单ID: {}, 状态: {}", orderId, status);
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        updateOrderStatus(orderId, "已完成");
        log.info("完成工单成功，工单ID: {}", orderId);
    }

    @Override
    @Transactional
    public void updateOrder(WorkOrder workOrder) {
        WorkOrder order = workOrderMapper.findById(workOrder.getOrderId());
        if (order == null) throw new BusinessException(Code.NOT_FOUND, "工单不存在");
        workOrderMapper.update(workOrder);
        log.info("更新工单成功，工单ID: {}", workOrder.getOrderId());
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        WorkOrder order = workOrderMapper.findById(orderId);
        if (order == null) throw new BusinessException(Code.NOT_FOUND, "工单不存在");
        workOrderMapper.deleteById(orderId);
        log.info("删除工单成功，工单ID: {}", orderId);
    }
}
