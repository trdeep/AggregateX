package cn.treedeep.king.generator.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 领域方法信息
 * <p>
 * 表示聚合根或实体中的业务方法，包含方法签名、参数、返回类型等信息
 *
 * @author 周广明
 * @since 2025-01-10
 */
@Getter
public class Method {
    private final String name;
    private final String comment;
    private final String returnType;
    private final boolean isVoid;
    private final List<Parameter> parameters = new ArrayList<>();

    public Method(String name, String comment, String returnType) {
        this.name = name;
        this.comment = comment;
        this.returnType = returnType != null ? returnType : "void";
        this.isVoid = "void".equals(this.returnType);
    }

    /**
     * 创建领域方法
     *
     * @param name 方法名
     * @param comment 方法注释
     * @param returnType 返回类型
     * @param parameters 方法参数
     * @return Method实例
     */
    public static Method create(String name, String comment, String returnType, Parameter... parameters) {
        Method method = new Method(name, comment, returnType);
        method.parameters.addAll(Arrays.stream(parameters).toList());
        return method;
    }

    /**
     * 创建无返回值的领域方法
     *
     * @param name 方法名
     * @param comment 方法注释
     * @param parameters 方法参数
     * @return Method实例
     */
    public static Method create(String name, String comment, Parameter... parameters) {
        return create(name, comment, "void", parameters);
    }

    /**
     * 创建无返回值的领域方法（支持字符串参数）
     *
     * @param name 方法名
     * @param comment 方法注释
     * @param parametersString 参数字符串，格式如"String name, int age"
     * @return Method实例
     */
    public static Method create(String name, String comment, String parametersString) {
        Method method = new Method(name, comment, "void");
        if (parametersString != null && !parametersString.trim().isEmpty()) {
            parseParametersString(parametersString, method);
        }
        return method;
    }

    /**
     * 设置返回类型（链式调用）
     *
     * @param returnType 返回类型
     * @return Method实例，便于链式调用
     */
    public Method returns(String returnType) {
        Method newMethod = new Method(this.name, this.comment, returnType);
        newMethod.parameters.addAll(this.parameters);
        return newMethod;
    }

    /**
     * 添加方法参数
     *
     * @param name 参数名
     * @param type 参数类型
     * @param comment 参数注释
     */
    public void addParameter(String name, String type, String comment) {
        this.parameters.add(Parameter.create(name, type, comment));
    }

    /**
     * 创建方法参数
     *
     * @param name 参数名
     * @param type 参数类型
     * @param comment 参数注释
     * @return Parameter实例
     */
    public static Parameter parameter(String name, String type, String comment) {
        return Parameter.create(name, type, comment);
    }

    /**
     * 解析参数字符串
     *
     * @param parametersString 参数字符串
     * @param method 方法实例
     */
    private static void parseParametersString(String parametersString, Method method) {
        String[] params = parametersString.split(",");
        for (String param : params) {
            param = param.trim();
            if (!param.isEmpty()) {
                String[] parts = param.split("\\s+");
                if (parts.length >= 2) {
                    String type = parts[0];
                    String name = parts[1];
                    String comment = name + "参数"; // 默认注释
                    method.addParameter(name, type, comment);
                }
            }
        }
    }

    /**
     * 方法参数类
     */
    @Getter
    public static class Parameter {
        private final String name;
        private final String type;
        private final String comment;

        public Parameter(String name, String type, String comment) {
            this.name = name;
            this.type = type;
            this.comment = comment;
        }

        public static Parameter create(String name, String type, String comment) {
            return new Parameter(name, type, comment);
        }
    }
}
