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

    /**
     * 值对象属性 - 用于标识实体中需要嵌套值对象的属性
     */
    public static class ValueObjectProperty extends Property {

        public ValueObjectProperty(String name, String comment) {
            super(name, comment);
        }

        public static ValueObjectProperty create(String name, String comment) {
            return new ValueObjectProperty(name, comment);
        }

        /**
         * 检查属性是否为值对象属性
         */
        public static boolean isValueObjectProperty(Property property) {
            return property instanceof ValueObjectProperty;
        }
    }



    public static class AggregateRootProperty extends Property {

        public AggregateRootProperty(String name, String comment) {
            super(name, comment);
        }

        public static AggregateRootProperty create(String name, String comment) {
            return new AggregateRootProperty(name, comment);
        }

        public static boolean isAggregateRootProperty(Property property) {
            return property instanceof AggregateRootProperty;
        }
    }
}
