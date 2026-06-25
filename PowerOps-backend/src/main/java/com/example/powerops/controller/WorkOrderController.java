package com.example.powerops.controller;

import com.example.powerops.common.Result;
import com.example.powerops.entity.WorkOrder;
import com.example.powerops.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 工单管理 Controller
 * 工单流程：创建(待处理) → 指派(处理中) → 完成(已完成)
 */
@RestController
@RequestMapping("/api/workorder")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    /**
     * 分页查询工单列表，支持类型/状态/处理人/优先级筛选
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listOrders(
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long handlerId,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(workOrderService.listOrders(orderType, status, handlerId, priority, pageNum, pageSize));
    }

    /**
     * 根据ID查询工单详情
     */
    @GetMapping("/{id}")
    public Result<?> getOrderById(@PathVariable Long id) {
        return Result.success(workOrderService.getOrderById(id));
    }

    /**
     * 创建工单（工单编号自动生成，初始状态"待处理"）
     */
    @PostMapping
    public Result<?> createOrder(@RequestBody WorkOrder workOrder) {
        workOrderService.createOrder(workOrder);
        return Result.success();
    }

    /**
     * 指派工单：设置处理人，状态变更为"处理中"
     */
    @PutMapping("/{id}/assign")
    public Result<?> assignOrder(@PathVariable Long id, @RequestParam Long handlerId) {
        workOrderService.assignOrder(id, handlerId);
        return Result.success();
    }

    /**
     * 通用状态变更接口
     */
    @PutMapping("/{id}/status")
    public Result<?> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        workOrderService.updateOrderStatus(id, status);
        return Result.success();
    }

    /**
     * 完成工单：状态变更为"已完成"，自动填充完成时间
     */
    @PutMapping("/{id}/complete")
    public Result<?> completeOrder(@PathVariable Long id) {
        workOrderService.completeOrder(id);
        return Result.success();
    }

    /**
     * 修改工单信息
     */
    @PutMapping("/{id}")
    public Result<?> updateOrder(@PathVariable Long id, @RequestBody WorkOrder workOrder) {
        workOrder.setOrderId(id);
        workOrderService.updateOrder(workOrder);
        return Result.success();
    }

    /**
     * 删除工单
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteOrder(@PathVariable Long id) {
        workOrderService.deleteOrder(id);
        return Result.success();
    }
}
