package com.example.powerops.mapper;

import com.example.powerops.entity.WorkOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkOrderMapper {
    WorkOrder findById(@Param("orderId") Long orderId);
    List<WorkOrder> listOrders(@Param("orderType") String orderType,
                              @Param("status") String status,
                              @Param("handlerId") Long handlerId,
                              @Param("priority") String priority,
                              @Param("offset") int offset,
                              @Param("limit") int limit);
    long countOrders(@Param("orderType") String orderType,
                    @Param("status") String status,
                    @Param("handlerId") Long handlerId,
                    @Param("priority") String priority);
    int insert(WorkOrder workOrder);
    int update(WorkOrder workOrder);
    int updateStatus(@Param("orderId") Long orderId, @Param("status") String status);
    int assignOrder(@Param("orderId") Long orderId, @Param("handlerId") Long handlerId);
    String generateOrderNo();
    int deleteById(@Param("orderId") Long orderId);
}
