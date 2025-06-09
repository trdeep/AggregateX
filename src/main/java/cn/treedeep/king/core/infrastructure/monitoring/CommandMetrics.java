package cn.treedeep.king.core.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 命令执行指标收集
 * <p>
 * 收集命令执行的各项指标:
 * 1. 执行次数
 * 2. 执行时间
 * 3. 成功/失败次数
 */
public class CommandMetrics {

    private final MeterRegistry registry;
    private final ConcurrentHashMap<String, Timer> successTimers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> failureTimers = new ConcurrentHashMap<>();

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
