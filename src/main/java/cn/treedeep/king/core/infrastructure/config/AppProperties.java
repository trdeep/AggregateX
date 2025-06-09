package cn.treedeep.king.core.infrastructure.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Audit audit = new Audit();

    private EventArchive eventArchive = new EventArchive();

    @Getter
    @Setter
    public static class EventArchive {
        private boolean enabled = false;
    }

    @Getter
    @Setter
    public static class Audit {
        private boolean enabled = false;
    }
}
