package cn.treedeep.king.shared.properties;

import cn.treedeep.king.shared.utils.SpringBeanUtil;
import com.google.common.base.Strings;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import java.io.File;

@ConfigurationProperties(
        prefix = "king",
        ignoreInvalidFields = true
)

@Data
public class KingProperties {

    private String workDir;
    private String host;
    private Boolean tls = false;
    private String serverUrl;

    public String getHost() {
        return StringUtils.isNoneBlank(host) ? host : "127.0.0.1";
    }

    public String getTempDir() {
        return workDir + File.separator + "temp";
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getServerURL() {
        Environment environment = SpringBeanUtil.getBean(Environment.class);
        String serverUrl = getServerUrl();

        String protocol = tls ? "https://" : "http://";
        String host = getHost();
        String port = environment.getProperty("local.server.port");
        String contextPath = Strings.nullToEmpty(environment.getProperty("server.servlet.context-path"));

        if (StringUtils.isBlank(serverUrl)) {
            return String.format("%s%s:%s%s", protocol, host, port, contextPath);
        }

        return serverUrl;
    }


}
