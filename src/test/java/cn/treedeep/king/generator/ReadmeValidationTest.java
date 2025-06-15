package cn.treedeep.king.generator;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * README文档验证测试
 */
public class ReadmeValidationTest {

    @Test
    public void testReadmeContainsNewFeatures() throws Exception {
        Path readmePath = Path.of("README.md");
        assertTrue(Files.exists(readmePath), "README.md文件应该存在");
        
        String readmeContent = Files.readString(readmePath);
        
        // 验证JSON5支持的描述
        assertTrue(readmeContent.contains("JSON5配置支持"), 
                "README应该包含JSON5配置支持的描述");
        
        // 验证remarks功能的描述
        assertTrue(readmeContent.contains("模块文档生成"), 
                "README应该包含模块文档生成的描述");
        
        // 验证新的配置示例
        assertTrue(readmeContent.contains("remarks"), 
                "README应该包含remarks字段的示例");
        
        // 验证JSON5格式示例
        assertTrue(readmeContent.contains("// JSON5"), 
                "README应该包含JSON5格式的注释示例");
        
        // 验证版本历史更新
        assertTrue(readmeContent.contains("v1.3.0"), 
                "README应该包含最新版本信息");
        
        // 验证相关文档链接
        assertTrue(readmeContent.contains("JSON_CONFIG_GUIDE.md"), 
                "README应该包含JSON配置指南的链接");
        
        System.out.println("✅ README.md文档验证通过");
        System.out.println("📄 文档包含了所有新功能的描述");
    }

    @Test
    public void testReadmeStructure() throws Exception {
        Path readmePath = Path.of("README.md");
        String readmeContent = Files.readString(readmePath);
        
        // 验证主要章节存在
        assertTrue(readmeContent.contains("## 🚀 核心特性"), "应该包含核心特性章节");
        assertTrue(readmeContent.contains("### 3. 智能代码生成器"), "应该包含代码生成器章节");
        assertTrue(readmeContent.contains("## 🔧 代码生成器详细指南"), "应该包含详细指南章节");
        assertTrue(readmeContent.contains("### JSON5配置使用"), "应该包含JSON5配置使用章节");
        assertTrue(readmeContent.contains("## 🆕 版本历史"), "应该包含版本历史章节");
        assertTrue(readmeContent.contains("## 📚 相关文档"), "应该包含相关文档章节");
        
        System.out.println("✅ README.md结构验证通过");
        System.out.println("📋 所有主要章节都存在");
    }
}
