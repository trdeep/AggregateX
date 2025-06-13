package cn.treedeep.king.tools.generator;

import cn.treedeep.king.tools.DDDModuleGenerator;
import cn.treedeep.king.tools.model.EntityInfo;
import cn.treedeep.king.tools.model.ModuleInfo;

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
        String path = "/Users/zhougm/vscode/KingCRM";
        String packageName = "cn.treedeep.king1";

        // 定义模块信息 - 使用 ModuleInfo 和 EntityInfo
        var modules = List.of(
                ModuleInfo.create("identity", "认证", List.of(
                        new EntityInfo("User", "用户实体"),
                        new EntityInfo("Role", "角色实体")
                ))
        );

        try {
            DDDModuleGenerator generator = new DDDModuleGenerator();

            // 批量生成
            generator.generateModules(path, packageName, modules, true);

            System.out.println("🎉 所有模块生成成功");

        } catch (Exception e) {
            System.err.println("❌ 模块生成失败: " + e.getMessage());
        }
    }

}
