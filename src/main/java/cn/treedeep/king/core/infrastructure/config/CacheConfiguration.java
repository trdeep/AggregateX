package cn.treedeep.king.core.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 * <p>
 * 使用Caffeine实现高性能的本地缓存
 * <p>
 * 配置包括：<br>
 * 1. 事件缓存 - 用于缓存聚合根的事件流<br>
 * 2. 快照缓存 - 用于缓存聚合根的最新快照<br>
 * 3. 归档缓存 - 用于缓存已归档的事件
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfiguration {

    /**
     * 配置缓存管理器
     */
    @Bean
    public CacheManager eventStoreCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 设置默认的缓存配置
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10_000)        // 最大缓存条数
                .expireAfterWrite(1, TimeUnit.HOURS)  // 写入1小时后过期
                .recordStats());            // 开启统计

        // 设置缓存名称列表
        cacheManager.setCacheNames(Arrays.asList(
                "events",       // 事件缓存
                "snapshots",    // 快照缓存
                "archives",     // 归档缓存
                "aggregates"    // 聚合根缓存
        ));

        // 针对不同缓存指定不同的配置
        cacheManager.registerCustomCache("events",
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .recordStats()
                        .build());

        cacheManager.registerCustomCache("snapshots",
                Caffeine.newBuilder()
                        .maximumSize(5_000)
                        .expireAfterWrite(2, TimeUnit.HOURS)
                        .recordStats()
                        .build());

        cacheManager.registerCustomCache("archives",
                Caffeine.newBuilder()
                        .maximumSize(20_000)
                        .expireAfterWrite(24, TimeUnit.HOURS)
                        .recordStats()
                        .build());

        cacheManager.registerCustomCache("aggregates",
                Caffeine.newBuilder()
                        .maximumSize(20_000)
                        .expireAfterWrite(24, TimeUnit.HOURS)
                        .recordStats()
                        .build());

        return cacheManager;
    }
}
