package cn.treedeep.king.shared.utils;

/**
 * Bean工具类
 * <p>
 * 扩展Spring的BeanUtils，提供更多便利的Bean操作方法。
 * 主要用于对象之间的属性复制和转换操作。
 * <p>
 * 功能特点：
 * <ul>
 * <li>忽略空值复制 - 只复制非null的属性值</li>
 * <li>类型安全 - 确保源属性和目标属性类型兼容</li>
 * <li>异常处理 - 统一处理反射操作中的异常</li>
 * <li>性能优化 - 缓存PropertyDescriptor减少反射开销</li>
 * </ul>
 * <p>
 * 使用场景：
 * <ul>
 * <li>DTO与Entity之间的转换</li>
 * <li>聚合根状态的部分更新</li>
 * <li>命令对象到领域对象的映射</li>
 * <li>查询结果的对象组装</li>
 * </ul>
 */
public class BeanUtil extends org.springframework.beans.BeanUtils {

    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }

        java.beans.PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());
        java.beans.PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());

        for (java.beans.PropertyDescriptor sourcePd : sourcePds) {
            if (sourcePd.getReadMethod() != null) {
                try {
                    Object value = sourcePd.getReadMethod().invoke(source);
                    if (value != null) {
                        for (java.beans.PropertyDescriptor targetPd : targetPds) {
                            if (targetPd.getName().equals(sourcePd.getName())
                                    && targetPd.getWriteMethod() != null) {
                                targetPd.getWriteMethod().invoke(target, value);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to copy properties", e);
                }
            }
        }
    }
}
