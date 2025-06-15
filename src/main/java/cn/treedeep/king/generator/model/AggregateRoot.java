package cn.treedeep.king.generator.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 聚合根信息
 *
 * @author 周广明
 * @since 2025-06-13
 */
public class AggregateRoot extends Entity {

    @Getter
    private final List<Entity> eos;

    public AggregateRoot(String name, String comment, Entity... eos) {
        super(name, comment);
        this.eos = Arrays.stream(eos).toList();
    }

    public static AggregateRoot create(String name, String comment, Entity... eos) {
        return new AggregateRoot(name, comment, eos);
    }

    public static Property property(String name, String comment) {
        return new Property(name, comment);
    }
}
