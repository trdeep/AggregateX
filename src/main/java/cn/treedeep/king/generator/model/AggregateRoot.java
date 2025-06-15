package cn.treedeep.king.generator.model;

import lombok.Getter;

import java.util.ArrayList;
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

    public AggregateRoot(String name, String comment, List<Property> directProperties, Entity... eos) {
        super(name, comment);
        this.setProperties(directProperties);
        this.eos = Arrays.stream(eos).toList();
    }

    /**
     * 创建聚合根，支持混合参数（直接属性 + 实体/值对象）
     */
    public static AggregateRoot create(String name, String comment, Object... items) {
        List<Property> directProperties = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();

        for (Object item : items) {
            if (item instanceof Property property) {
                // 直接添加属性，保留其原始类型（Property 或 AggregateRootProperty）
                directProperties.add(property);
            } else if (item instanceof Entity) {
                entities.add((Entity) item);
            }
        }

        return new AggregateRoot(name, comment, directProperties, entities.toArray(new Entity[0]));
    }

    public static Property property(String name, String comment) {
        return new Property(name, comment);
    }
}
