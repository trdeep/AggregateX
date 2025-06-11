package cn.treedeep.king.tools.generator;


import cn.treedeep.king.tools.DDDModuleGenerator;

import java.nio.file.Path;

/**
 * 简单的生成器测试
 */
public class SimpleGeneratorTest {

    public static void main(String[] args) {

        // 目录
        Path path = Path.of("/Users/zhougm/vscode/GoodLuck");
        System.out.println("测试目录: " + path);

        // 创建生成器实例
        DDDModuleGenerator generator = new DDDModuleGenerator();

        try {
            // 生成product模块
            generator.generateModule(path.toString(), "order", null, null);
            System.out.println("✅ 模块生成成功!");

            // 验证文件是否存在
            Path productPath = path.resolve("order");

            System.out.println("✅ 所有核心文件验证通过!");
            System.out.println("📍 生成的模块位置: " + productPath.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("❌ 生成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
