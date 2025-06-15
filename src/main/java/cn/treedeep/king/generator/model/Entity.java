package cn.treedeep.king.generator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 实体信息
 *
 * @author 周广明
 * @since 2025-06-13
 */
@Getter
public class Entity {
    private final String name;
    private final String comment;

    @Setter
    private List<Property> properties = new ArrayList<>();

    public Entity(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public static Entity create(String name, String comment, Property... properties) {
        Entity entity = new Entity(name, comment);
        entity.properties.addAll(Arrays.stream(properties).toList());
        return entity;
    }

    public void addProperty(String name, String comment) {
        this.properties.add(Entity.property(name, comment));
    }

    public static Property property(String name, String comment) {
        return new Property(name, comment);
    }

}
