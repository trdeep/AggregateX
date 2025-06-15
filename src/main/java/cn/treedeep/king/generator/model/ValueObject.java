package cn.treedeep.king.generator.model;

import java.util.Arrays;

/**
 * 值对象信息
 *
 * @author 周广明
 * @since 2025-06-13
 */
public class ValueObject extends Entity {
    public ValueObject(String name, String comment) {
        super(name, comment);
    }

    public static ValueObject create(String name, String comment, Property... properties) {
        ValueObject valueObject = new ValueObject(name, comment);
        valueObject.setProperties(Arrays.stream(properties).toList());
        return valueObject;
    }

    public static Property property(String name, String comment) {
        return new Property(name, comment);
    }
}
