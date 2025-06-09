package cn.treedeep.king.shared.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "king.data",
        ignoreInvalidFields = true
)
@Data
public class KingDataProperties {

    private Mybatis mybatis = new Mybatis();

    @Data
    public static class Mybatis {
        private Boolean cache = false;
    }
}
