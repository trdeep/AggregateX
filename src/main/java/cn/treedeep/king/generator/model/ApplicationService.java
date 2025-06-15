package cn.treedeep.king.generator.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 应用服务信息
 * <p>
 * 表示应用层的服务类，负责业务流程编排和事务管理
 *
 * @author 周广明
 * @since 2025-01-10
 */
@Getter
public class ApplicationService {
    private final String name;
    private final String comment;
    private final String moduleName;
    private final List<ServiceMethod> methods = new ArrayList<>();

    public ApplicationService(String name, String comment, String moduleName) {
        this.name = name;
        this.comment = comment;
        this.moduleName = moduleName;
    }

    /**
     * 创建应用服务
     *
     * @param name 服务名称（如：UserApplicationService）
     * @param comment 服务注释
     * @param moduleName 模块名称
     * @param methods 服务方法
     * @return ApplicationService实例
     */
    public static ApplicationService create(String name, String comment, String moduleName, ServiceMethod... methods) {
        ApplicationService service = new ApplicationService(name, comment, moduleName);
        service.methods.addAll(Arrays.stream(methods).toList());
        return service;
    }

    /**
     * 创建简单的应用服务（无模块名）
     *
     * @param name 服务名称
     * @param comment 服务注释
     * @return ApplicationService实例
     */
    public static ApplicationService create(String name, String comment) {
        return new ApplicationService(name, comment, null);
    }

    /**
     * 添加服务方法
     *
     * @param name 方法名
     * @param comment 方法注释
     * @param returnType 返回类型
     * @param parameters 方法参数
     */
    public void addMethod(String name, String comment, String returnType, ServiceMethod.Parameter... parameters) {
        this.methods.add(ServiceMethod.create(name, comment, returnType, parameters));
    }

    /**
     * 添加方法（链式调用）
     *
     * @param method 服务方法
     * @return ApplicationService实例，便于链式调用
     */
    public ApplicationService addMethod(Method method) {
        // 将Method转换为ServiceMethod
        ServiceMethod serviceMethod = new ServiceMethod(method.getName(), method.getComment(), method.getReturnType());
        for (Method.Parameter param : method.getParameters()) {
            serviceMethod.addParameter(param.getName(), param.getType(), param.getComment());
        }
        this.methods.add(serviceMethod);
        return this;
    }

    /**
     * 创建服务方法
     *
     * @param name 方法名
     * @param comment 方法注释
     * @param returnType 返回类型
     * @param parameters 方法参数
     * @return ServiceMethod实例
     */
    public static ServiceMethod method(String name, String comment, String returnType, ServiceMethod.Parameter... parameters) {
        return ServiceMethod.create(name, comment, returnType, parameters);
    }

    /**
     * 获取服务类名
     */
    public String getClassName() {
        return name;
    }

    /**
     * 获取服务接口名
     */
    public String getInterfaceName() {
        return name.endsWith("Service") ? name : name + "Service";
    }

    /**
     * 获取服务实现类名
     */
    public String getImplementationName() {
        String interfaceName = getInterfaceName();
        return interfaceName + "Impl";
    }

    public static String capitalizeFirstLetter(String moduleName) {
        if (moduleName == null || moduleName.isEmpty()) {
            return moduleName;
        }

        // 将整个字符串转换为小写
        String lowerCaseName = moduleName.toLowerCase();

        // 将第一个字母转换为大写
        return lowerCaseName.substring(0, 1).toUpperCase() + lowerCaseName.substring(1);
    }

    /**
     * 服务方法类
     */
    @Getter
    public static class ServiceMethod {
        private final String name;
        private final String comment;
        private final String returnType;
        private final boolean isVoid;
        private final List<Parameter> parameters = new ArrayList<>();

        public ServiceMethod(String name, String comment, String returnType) {
            this.name = name;
            this.comment = comment;
            this.returnType = returnType != null ? returnType : "void";
            this.isVoid = "void".equals(this.returnType);
        }

        public static ServiceMethod create(String name, String comment, String returnType, Parameter... parameters) {
            ServiceMethod method = new ServiceMethod(name, comment, returnType);
            method.parameters.addAll(Arrays.stream(parameters).toList());
            return method;
        }

        public static ServiceMethod create(String name, String comment, Parameter... parameters) {
            return create(name, comment, "void", parameters);
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
         * 服务方法参数类
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
}
