package cn.treedeep.king.tools.generator;

import cn.treedeep.king.generator.DDDModuleGenerator;
import cn.treedeep.king.generator.model.*;
import cn.treedeep.king.generator.model.Module;

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
        String path = "/Users/zhougm/vscode/KingAdmin";
        String packageName = "cn.treedeep.king.admin";

        // ====== 值对象定义 ======
        var phoneValueObject = ValueObject.create("Phone", "手机号",
                Entity.property("countryCode", "国家代码"),
                Entity.property("phoneNumber", "手机号码")
        );

        var deviceFingerprint = ValueObject.create("DeviceFingerprint", "设备指纹",
                Entity.property("osType", "操作系统"),
                Entity.property("browserType", "浏览器类型"),
                Entity.property("deviceId", "设备ID")
        );

        // ====== 实体定义 ======
        var loginRecordEntity = Entity.create("LoginRecord", "登录记录",
                Entity.property("loginTime", "登录时间"),
                Entity.property("ipAddress", "IP地址"),
                Entity.property("result", "登录结果"),
                ValueObject.property("deviceFingerprint", "嵌套值对象")
                // deviceFingerprint  // 嵌套值对象
        );

        var securityLogEntity = Entity.create("SecurityLog", "安全日志",
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

                // 值对象
                phoneValueObject,
                deviceFingerprint,

                // 实体
                loginRecordEntity,
                securityLogEntity
        );

        // ====== 模块定义 ======
        var modules = List.of(
                Module.create("authentication", "认证模块", userAggregate)
        );

        try {
            DDDModuleGenerator generator = new DDDModuleGenerator();
            generator.generateModules(path, packageName, modules, true);
            System.out.println("✅ 认证模块生成成功");
        } catch (Exception e) {
            System.err.println("❌ 生成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
