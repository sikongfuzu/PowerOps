# PowerOps 项目结构说明

## 完整目录结构

```
PowerOps/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── powerops/
│   │   │           ├── PowerOpsApplication.java          # 启动类
│   │   │           ├── common/                            # 通用类
│   │   │           │   ├── Result.java                    # 统一返回结果封装
│   │   │           │   ├── Code.java                      # 响应状态码常量
│   │   │           │   └── BusinessException.java         # 自定义业务异常
│   │   │           ├── config/                            # 配置类
│   │   │           │   └── GlobalExceptionHandler.java    # 全局异常处理器
│   │   │           ├── controller/                        # 控制器层
│   │   │           │   └── UserController.java            # 用户控制器
│   │   │           ├── service/                           # 服务层接口
│   │   │           │   └── UserService.java               # 用户服务接口
│   │   │           ├── service/impl/                      # 服务层实现
│   │   │           │   └── UserServiceImpl.java           # 用户服务实现
│   │   │           ├── mapper/                            # Mapper接口
│   │   │           │   └── UserMapper.java                # 用户Mapper
│   │   │           ├── entity/                            # 实体类
│   │   │           │   └── SysUser.java                   # 系统用户实体
│   │   │           ├── dto/                               # 数据传输对象
│   │   │           │   ├── LoginRequest.java              # 登录请求DTO
│   │   │           │   └── UserRequest.java               # 用户新增/修改DTO
│   │   │           └── vo/                                # 视图对象
│   │   │               ├── LoginVO.java                   # 登录响应VO
│   │   │               └── UserVO.java                    # 用户信息VO
│   │   └── resources/
│   │       ├── application.properties                     # 应用配置文件
│   │       ├── mapper/                                    # MyBatis XML映射文件
│   │       │   └── UserMapper.xml                         # 用户Mapper XML
│   │       └── sql/                                       # SQL脚本
│   │           └── init.sql                               # 数据库初始化脚本
│   └── test/
│       └── java/
│           └── com/
│               └── powerops/
│                   └── PowerOpsApplicationTests.java      # 测试类
├── pom.xml                                                # Maven配置文件
├── README.md                                              # 项目说明文档
└── API_DOCUMENTATION.md                                   # API接口文档
```

## 分层架构说明

### 1. Controller 层（控制器层）
- **职责**: 接收HTTP请求，参数验证，调用Service，返回响应
- **示例**: `UserController.java`
- **注解**: `@RestController`, `@RequestMapping`

### 2. Service 层（业务逻辑层）
- **职责**: 业务逻辑处理，事务控制
- **接口**: `UserService.java`
- **实现**: `UserServiceImpl.java`
- **注解**: `@Service`, `@Transactional`

### 3. Mapper 层（数据访问层）
- **职责**: 数据库CRUD操作
- **接口**: `UserMapper.java` (使用 `@Mapper`)
- **XML**: `UserMapper.xml` (MyBatis动态SQL)

### 4. Entity 层（实体层）
- **职责**: 与数据库表一一对应
- **示例**: `SysUser.java` → `SYS_USER` 表
- **注解**: `@Data` (Lombok)

### 5. DTO 层（数据传输对象）
- **职责**: 接收前端请求参数，参数验证
- **示例**: `LoginRequest.java`, `UserRequest.java`
- **注解**: `@Valid`, `@NotBlank`, `@Pattern`

### 6. VO 层（视图对象）
- **职责**: 返回给前端的数据对象（不包含敏感信息如密码）
- **示例**: `LoginVO.java`, `UserVO.java`

### 7. Common 层（通用类）
- **Result**: 统一API返回格式
- **Code**: 状态码常量定义
- **BusinessException**: 自定义业务异常

### 8. Config 层（配置类）
- **GlobalExceptionHandler**: 全局异常处理

## 技术要点

### MyBatis XML 模式
- 位置: `src/main/resources/mapper/*.xml`
- 配置: `mybatis.mapper-locations=classpath:mapper/*.xml`
- 支持动态SQL、分页查询、条件筛选

### Oracle 特性
- 使用 `SEQUENCE.NEXTVAL` 实现自增ID
- 使用 `ROWNUM` 实现分页
- 表名全部大写（符合Oracle规范）

### 参数验证
- 使用 Spring Validation
- DTO 中添加验证注解
- Controller 中使用 `@Valid` 触发验证

### 异常处理
- 业务异常: `BusinessException`
- 全局捕获: `@RestControllerAdvice`
- 统一返回: `Result.error()`

## 开发规范

### 命名规范
- **包名**: 小写，如 `com.powerops.controller`
- **类名**: 大驼峰，如 `UserController`
- **方法名**: 小驼峰，如 `getUserById`
- **变量名**: 小驼峰，如 `userName`
- **常量名**: 全大写+下划线，如 `SUCCESS`
- **数据库表**: 全大写+下划线，如 `SYS_USER`
- **数据库字段**: 全大写+下划线，如 `USER_ID`

### RESTful API 规范
- `GET /api/user/list` - 查询列表
- `GET /api/user/{id}` - 查询单个
- `POST /api/user` - 新增
- `PUT /api/user/{id}` - 更新
- `DELETE /api/user/{id}` - 删除

### 返回格式规范
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 下一步开发建议

1. **商铺模块 (SHOP)**
   - 创建 `Shop` 实体
   - 创建 `ShopMapper` 接口和XML
   - 创建 `ShopService` 及实现
   - 创建 `ShopController`

2. **设备模块 (DEVICE)**
   - 支持树形结构（parent_device_id）
   - 按商铺查询设备

3. **报警→工单联动**
   - 在 `AlarmService` 中自动生成工单
   - 根据报警等级设置优先级

4. **用电→账单生成**
   - 按月统计用电量
   - 自动计算电费

5. **JWT认证**
   - 替换简单UUID token
   - 添加拦截器验证token
