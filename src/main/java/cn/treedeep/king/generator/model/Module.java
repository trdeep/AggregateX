package cn.treedeep.king.generator.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 模块信息
 *
 * @author 周广明
 * @since 2025-06-13
 */
@Getter
public class Module {
    private final String name;
    private final String comment;
    private final List<AggregateRoot> aggregateRoots;

    private Module(String name, String comment, List<AggregateRoot> aggregateRoots) {
        this.name = name;
        this.comment = comment;
        this.aggregateRoots = aggregateRoots;
    }

    public static Module create(String name, String comment, AggregateRoot... ars) {
        return new Module(name, comment, Arrays.stream(ars).toList());
    }

}
