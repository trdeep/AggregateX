package cn.treedeep.king.shared.utils;

public class BeanUtils extends org.springframework.beans.BeanUtils {

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
