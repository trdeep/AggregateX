package cn.treedeep.king.core.infrastructure.monitoring;

import cn.treedeep.king.shared.properties.KingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

/**
 * 应用程序启动监听器
 * <p>
 * 监听Spring Boot应用的启动完成事件，执行必要的初始化操作和状态记录。
 * <p>
 * 主要功能：
 * <ul>
 * <li>记录应用启动完成的日志信息</li>
 * <li>输出服务器地址和端口信息</li>
 * <li>初始化监控和健康检查组件</li>
 * <li>触发应用就绪状态的相关操作</li>
 * </ul>
 * <p>
 * 触发时机：
 * <ul>
 * <li>应用上下文完全刷新后</li>
 * <li>所有Bean初始化完成后</li>
 * <li>CommandLineRunner和ApplicationRunner执行前</li>
 * </ul>
 */
@Component("king_core_ApplicationListener")
public class ApplicationListener implements org.springframework.context.ApplicationListener<ApplicationStartedEvent> {

    /**
     * 处理应用启动完成事件
     * <p>
     * 当应用启动完成时，记录启动信息并输出服务器地址
     *
     * @param event 应用启动事件
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        KingProperties kingProperties = event.getApplicationContext().getBean(KingProperties.class);
        Logger logger = LoggerFactory.getLogger(ApplicationListener.class);
        logger.info("Application started at {}", kingProperties.getServerURL());
    }
}
