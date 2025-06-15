package cn.treedeep.king.generator.model;

import java.util.Arrays;

public class Property extends Entity {
    public Property(String name, String comment) {
        super(name, comment);
    }

    public static Property create(String name, String comment, Property... properties) {
        Property propertyObject = new Property(name, comment);
        propertyObject.setProperties(Arrays.stream(properties).toList());
        return propertyObject;
    }
}
