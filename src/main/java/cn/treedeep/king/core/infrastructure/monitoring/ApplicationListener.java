package cn.treedeep.king.core.infrastructure.monitoring;

import cn.treedeep.king.shared.properties.KingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

@Component("king_core_ApplicationListener")
public class ApplicationListener implements org.springframework.context.ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        KingProperties kingProperties = event.getApplicationContext().getBean(KingProperties.class);
        Logger logger = LoggerFactory.getLogger(ApplicationListener.class);
        logger.info("Application started at {}", kingProperties.getServerURL());
    }
}
