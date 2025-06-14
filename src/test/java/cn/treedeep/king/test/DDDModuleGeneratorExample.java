package cn.treedeep.king.test;

import cn.treedeep.king.generator.DDDModuleGenerator;
import cn.treedeep.king.generator.model.*;
import cn.treedeep.king.generator.model.ModuleInfo;

import java.util.List;

/**
 * DDDModuleGenerator 新的使用方式示例
 *
 * @author 周广明
 * @since 2025-06-13
 */
public class DDDModuleGeneratorExample {

    public static void main(String[] args) {

        // 工程信息
        String path = "/Users/zhougm/vscode/AggregateX";
        String packageName = "cn.treedeep.king.demo";

        // ====== 值对象定义 ======
        var phone = ValueObject.create("Phone", "手机号",
                Entity.property("countryCode", "国家代码"),
                Entity.property("phoneNumber", "手机号码")
        );

        var deviceFingerprint = ValueObject.create("DeviceFingerprint", "设备指纹",
                Entity.property("osType", "操作系统"),
                Entity.property("browserType", "浏览器类型"),
                Entity.property("deviceId", "设备ID")
        );

        // ====== 实体定义 ======
        var loginRecord = Entity.create("LoginRecord", "登录记录",
                Entity.property("loginTime", "登录时间"),
                Entity.property("ipAddress", "IP地址"),
                Entity.property("result", "登录结果"),
                Property.ValueObjectProperty.create("deviceFingerprint", "设备指纹")
        );

        var securityLog = Entity.create("SecurityLog", "安全日志",
                Entity.property("operationTime", "操作时间"),
                Entity.property("operationType", "操作类型"),
                Entity.property("detail", "操作详情")
        );

        // ====== 聚合根定义（含直接属性） ======
        var userAggregate = AggregateRoot.create("User", "用户聚合根",
                // 聚合根直接属性
                Property.create("username", "用户名"),
                Property.create("email", "邮箱"),
                Property.create("passwordHash", "加密密码"),
                Property.create("status", "账户状态"),
                Property.create("failedLoginAttempts", "登录失败次数"),

                // phoneValueObject嵌套到聚合根里
                Property.AggregateRootProperty.create("phone", "手机号"),

                // 以下对象生成在domain层

                // 值对象
                deviceFingerprint,
                phone,

                // 实体
                loginRecord,
                securityLog,

                // 领域方法
                Method.create("register", "注册方法", "String username, String email, Phone phone, String rawPassword"),
                Method.create("login", "登录方法", "String credential, String ip, DeviceFingerprint device"),
                Method.create("lockAccount", "锁定账户", "String reason")
        );

        // ====== 模块定义 ======
        var modules = List.of(
                ModuleInfo.create("authentication", "认证模块", userAggregate
                        ,
                        // 领域事件
                        DomainEvent.create("UserRegisteredEvent", "用户注册事件"),
                        DomainEvent.create("UserLockedEvent", "用户锁定事件")
                        ,

                        // 应用服务
                        ApplicationService.create("UserManagementService", "用户管理服务")
                                .addMethod(Method.create("registerUser", "注册用户", "String username, String email").returns("String"))
                                .addMethod(Method.create("activateUser", "激活用户", "String userId"))
                                .addMethod(Method.create("getUserStatus", "获取用户状态", "String userId").returns("String"))

                )
        );

        try {
            DDDModuleGenerator generator = new DDDModuleGenerator();
            generator.generateModules(path, packageName, modules, true);
            System.out.println("✅ 认证模块生成成功");
        } catch (Exception e) {
            System.err.println("❌ 生成失败: " + e.getMessage());
        }
    }


}
