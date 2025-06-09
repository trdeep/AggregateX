package cn.treedeep.king.shared.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "king.oss",
        ignoreInvalidFields = true
)
@Data
public class KingOssProperties {

    private Aliyun aliyun;

    @Data
    public static class Aliyun {
        private String accessKeyId;
        private String accessKeySecret;
        private String bucket;
        private String endpoint;
        private int chunkSize;
    }
}
