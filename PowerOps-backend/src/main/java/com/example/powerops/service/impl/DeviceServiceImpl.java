package com.example.powerops.service.impl;

import com.example.powerops.common.BusinessException;
import com.example.powerops.common.Code;
import com.example.powerops.dto.DeviceRequest;
import com.example.powerops.entity.Device;
import com.example.powerops.mapper.DeviceMapper;
import com.example.powerops.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备管理服务实现
 *
 * <p>提供设备 CRUD、分页查询、按商铺查询及状态更新功能。</p>
 * <p>新增时 status 由前端传入（默认"运行"），不再硬编码。</p>
 * <p>额定参数和健康评分通过 BeanUtils 从 DTO 复制到实体。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    /**
     * 分页查询设备列表
     *
     * @param deviceName 设备名称（模糊匹配）
     * @param deviceType 设备类型（精确匹配）
     * @param shopId     所属商铺ID
     * @param status     设备状态
     * @param pageNum    当前页码
     * @param pageSize   每页条数
     * @return 包含 list / total / pageNum / pageSize 的 Map
     */
    @Override
    public Map<String, Object> listDevices(String deviceName, String deviceType, Long shopId, String status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Device> devices = deviceMapper.listDevices(deviceName, deviceType, shopId, status, offset, pageSize);
        long total = deviceMapper.countDevices(deviceName, deviceType, shopId, status);
        Map<String, Object> result = new HashMap<>();
        result.put("list", devices);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 根据ID查询单个设备
     *
     * @param deviceId 设备ID
     * @return 设备实体
     * @throws BusinessException 设备不存在时抛出
     */
    @Override
    public Device getDeviceById(Long deviceId) {
        Device device = deviceMapper.findById(deviceId);
        if (device == null) throw new BusinessException(Code.NOT_FOUND, "设备不存在");
        return device;
    }

    /**
     * 根据商铺ID查询设备列表（返回全部设备，不分页）
     *
     * @param shopId 商铺ID
     * @return 该商铺下的所有设备
     */
    @Override
    public List<Device> listDevicesByShopId(Long shopId) {
        return deviceMapper.listByShopId(shopId);
    }

    /**
     * 新增设备
     * <p>将 DeviceRequest 转成 Device 实体后插入数据库。</p>
     * <p>status 由前端传入，不设默认值；额定参数和健康评分可选。</p>
     *
     * @param request 设备请求体
     */
    @Override
    @Transactional
    public void createDevice(DeviceRequest request) {
        Device device = new Device();
        BeanUtils.copyProperties(request, device);
        int rows = deviceMapper.insert(device);
        if (rows == 0) throw new BusinessException("创建设备失败");
        log.info("创建设备成功: {}", request.getDeviceName());
    }

    /**
     * 更新设备信息
     * <p>先校验设备是否存在，再用请求内容覆盖更新。</p>
     *
     * @param request 设备请求体（deviceId 必填）
     */
    @Override
    @Transactional
    public void updateDevice(DeviceRequest request) {
        Device exist = deviceMapper.findById(request.getDeviceId());
        if (exist == null) throw new BusinessException(Code.NOT_FOUND, "设备不存在");
        Device device = new Device();
        BeanUtils.copyProperties(request, device);
        int rows = deviceMapper.update(device);
        if (rows == 0) throw new BusinessException("更新设备失败");
        log.info("更新设备成功: {}", request.getDeviceName());
    }

    /**
     * 更新设备状态（运行/故障/检修）
     * <p>只修改 status 字段，不涉及其他属性。</p>
     *
     * @param deviceId 设备ID
     * @param status   目标状态
     */
    @Override
    @Transactional
    public void updateDeviceStatus(Long deviceId, String status) {
        Device device = deviceMapper.findById(deviceId);
        if (device == null) throw new BusinessException(Code.NOT_FOUND, "设备不存在");
        device.setStatus(status);
        deviceMapper.update(device);
        log.info("更新设备状态成功，设备ID: {}, 状态: {}", deviceId, status);
    }

    /**
     * 删除设备
     *
     * @param deviceId 设备ID
     */
    @Override
    @Transactional
    public void deleteDevice(Long deviceId) {
        Device device = deviceMapper.findById(deviceId);
        if (device == null) throw new BusinessException(Code.NOT_FOUND, "设备不存在");
        deviceMapper.deleteById(deviceId);
        log.info("删除设备成功: {}", deviceId);
    }
}
