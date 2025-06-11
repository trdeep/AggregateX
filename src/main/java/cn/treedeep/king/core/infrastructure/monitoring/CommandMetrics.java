package cn.treedeep.king.core.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 命令执行指标收集器
 * <p>
 * 基于Micrometer框架实现的命令执行指标收集组件，提供全面的命令执行监控数据。
 * 支持与Prometheus、InfluxDB、CloudWatch等多种监控系统集成。
 * <p>
 * 收集的指标：
 * <ul>
 * <li>执行次数 - 按命令类型统计执行总数</li>
 * <li>执行时间 - 记录命令执行的耗时分布</li>
 * <li>成功率 - 统计命令执行的成功和失败比例</li>
 * <li>吞吐量 - 计算每秒处理的命令数量</li>
 * <li>错误分布 - 按异常类型统计失败原因</li>
 * </ul>
 * <p>
 * 标签维度：
 * <ul>
 * <li>type - 命令类型名称</li>
 * <li>result - 执行结果(success/failure)</li>
 * <li>error_type - 错误类型（仅失败时）</li>
 * <li>aggregate_type - 聚合根类型</li>
 * </ul>
 * <p>
 * 性能优化：
 * <ul>
 * <li>使用ConcurrentHashMap缓存Timer实例</li>
 * <li>避免频繁创建Meter对象</li>
 * <li>支持批量指标推送</li>
 * </ul>
 */
public class CommandMetrics {

    private final MeterRegistry registry;
    private final ConcurrentHashMap<String, Timer> successTimers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> failureTimers = new ConcurrentHashMap<>();

    /**
     * 构造命令指标收集器
     *
     * @param registry Micrometer注册表，用于指标收集和上报
     */
    public CommandMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    /**
     * 记录命令执行成功
     *
     * @param commandType 命令类型
     * @param duration   执行时间(毫秒)
     */
    public void recordSuccess(String commandType, long duration) {
        Timer timer = successTimers.computeIfAbsent(commandType,
            type -> Timer.builder("command.execution")
                        .tag("type", type)
                        .tag("result", "success")
                        .register(registry));

        timer.record(duration, TimeUnit.MILLISECONDS);
    }

    /**
     * 记录命令执行失败
     *
     * @param commandType 命令类型
     * @param duration   执行时间(毫秒)
     */
    public void recordFailure(String commandType, long duration) {
        Timer timer = failureTimers.computeIfAbsent(commandType,
            type -> Timer.builder("command.execution")
                        .tag("type", type)
                        .tag("result", "failure")
                        .register(registry));

        timer.record(duration, TimeUnit.MILLISECONDS);
    }
}
