package cn.treedeep.king.tools.generator;

import cn.treedeep.king.tools.DDDModuleGenerator;

import java.nio.file.Path;

/**
 * 简单的生成器
 */
public class SimpleGeneratorTest {

    public static void main(String[] args) {

        // 工程目录
        String path = ".";

        // 模块信息
        String moduleName = "order";
        String moduleComment = "订单";

        // 创建生成器实例
        DDDModuleGenerator generator = new DDDModuleGenerator();

        try {
            // 生成模块
            generator.generateModule(path, null, true, moduleName, moduleComment, null, null);
            System.out.println("✅ 模块生成成功!");
        } catch (Exception e) {
            System.err.println("❌ 生成失败: " + e.getMessage());
            e.printStackTrace();
        }

    }

}

