package cn.treedeep.king.shared.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceUtil {
    private ResourceUtil() {
        throw new IllegalStateException("工具类不允许实例化");
    }

    public static String readResource(String path) {
        try {
            // 优先尝试从开发环境路径读取
            InputStream stream = ResourceUtil.class.getResourceAsStream(path);
            if (stream != null && stream.available() > 0) {
                return new String(FileCopyUtils.copyToByteArray(stream), StandardCharsets.UTF_8);
            }

            // 回退到 ClassPathResource 读取（适用于生产环境）
            ClassPathResource resource = new ClassPathResource(path);
            return new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }
    }

    public static boolean resourceExists(String path) {
        return new ClassPathResource(path).exists();
    }


}
