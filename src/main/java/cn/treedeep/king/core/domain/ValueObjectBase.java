package cn.treedeep.king.core.domain;

import org.apache.commons.lang3.SerializationUtils;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;

@ValueObject
public abstract class ValueObjectBase implements Serializable {

    /**
     * 创建当前值对象的深度复制
     * <p>
     * 用于防御性复制，确保值对象的不可变性
     *
     * @return 当前值对象的深度复制
     */
    protected ValueObjectBase deepCopy() {
        return SerializationUtils.clone(this);
    }


    /**
     * 验证值对象的不变量
     * 子类应该重写此方法以添加特定的验证规则
     *
     * @throws IllegalArgumentException 当验证失败时
     */
    protected void validateInvariants() throws IllegalArgumentException {
        // 默认实现不做任何验证
    }
}
