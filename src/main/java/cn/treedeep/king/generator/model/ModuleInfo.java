package cn.treedeep.king.generator.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模块信息
 *
 * @author 周广明
 * @since 2025-06-13
 */
@Getter
public class ModuleInfo {
    private final String name;
    private final String comment;
    private final String remarks; // 模块备注信息，用于README.md
    private final List<AggregateRoot> aggregateRoots;
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    private final List<ApplicationService> applicationServices = new ArrayList<>();

    private ModuleInfo(String name, String comment, String remarks, List<AggregateRoot> aggregateRoots) {
        this.name = name;
        this.comment = comment;
        this.remarks = remarks;
        this.aggregateRoots = aggregateRoots;
    }

    public static ModuleInfo create(String name, String comment, AggregateRoot... ars) {
        return create(name, comment, null, ars);
    }

    public static ModuleInfo create(String name, String comment, String remarks, AggregateRoot... ars) {
        return new ModuleInfo(name, comment, remarks, Arrays.stream(ars).toList());
    }

    /**
     * 创建模块，支持混合参数（聚合根 + 领域事件 + 应用服务）
     */
    public static ModuleInfo create(String name, String comment, Object... items) {
        return create(name, comment, null, items);
    }

    /**
     * 创建模块，支持混合参数（聚合根 + 领域事件 + 应用服务）
     */
    public static ModuleInfo create(String name, String comment, String remarks, Object... items) {
        List<AggregateRoot> aggregateRoots = new ArrayList<>();
        List<DomainEvent> domainEvents = new ArrayList<>();
        List<ApplicationService> applicationServices = new ArrayList<>();

        for (Object item : items) {
            if (item instanceof AggregateRoot) {
                aggregateRoots.add((AggregateRoot) item);
            } else if (item instanceof DomainEvent) {
                domainEvents.add((DomainEvent) item);
            } else if (item instanceof ApplicationService) {
                applicationServices.add((ApplicationService) item);
            }
        }

        ModuleInfo module = new ModuleInfo(name, comment, remarks, aggregateRoots);
        module.domainEvents.addAll(domainEvents);
        module.applicationServices.addAll(applicationServices);
        return module;
    }

    /**
     * 添加领域事件
     */
    public void addDomainEvent(DomainEvent domainEvent) {
        this.domainEvents.add(domainEvent);
    }

    /**
     * 添加应用服务
     */
    public void addApplicationService(ApplicationService applicationService) {
        this.applicationServices.add(applicationService);
    }

}
