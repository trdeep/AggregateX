package cn.treedeep.king.core.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.SerializationUtils;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 值对象基类
 * <p>
 * 所有的值对象都需要继承此类。值对象是DDD中的重要概念，
 * 用于表示那些没有标识符、通过属性值来确定同一性的对象。
 * <p>
 * 值对象特征：<br>
 * 1. 无标识符：不需要唯一标识<br>
 * 2. 不可变性：创建后属性不可改变<br>
 * 3. 相等性比较：基于所有属性值<br>
 * 4. 可替换性：可以被相同属性值的对象替换
 * <p>
 * 实现要求：<br>
 * 1. 所有属性必须是final的，确保不可变性<br>
 * 2. equals方法必须考虑所有属性<br>
 * 3. hashCode方法必须与equals一致<br>
 * 4. toString方法应包含所有属性信息
 * <p>
 * 示例：
 * <pre>
 *     {@code
 * public class Money extends ValueObjectBase {
 *     private final BigDecimal amount;
 *     private final Currency currency;
 *
 *     public Money add(Money other) {
 *         if (!this.currency.equals(other.currency)) {
 *             throw new IllegalArgumentException("不同货币不能直接相加");
 *         }
 *         return new Money(this.amount.add(other.amount), this.currency);
 *     }
 * }
 * }
 * </pre>
 */
@ValueObject
@MappedSuperclass
public abstract class ValueObjectBase implements Serializable {
    /**
     * 获取用于相等性比较的组件
     * <p>
     * 子类必须实现此方法以提供用于相等性比较的属性值数组
     *
     * @return 属性值数组
     */
    @Transient
    protected abstract Object[] getEqualityComponents();

    /**
     * 比较两个值对象是否相等
     * 值对象的相等性比较应该基于其所有属性值
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        ValueObjectBase other = (ValueObjectBase) obj;
        return Arrays.deepEquals(getEqualityComponents(), other.getEqualityComponents());
    }

    /**
     * 计算值对象的哈希码
     * 哈希码的计算应该基于其所有属性值
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getEqualityComponents());
    }

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
    protected void validateInvariants() {
        // 默认实现不做任何验证
    }

    /**
     * 比较两个值对象是否在业务上相等
     *
     * @param other 要比较的另一个值对象
     * @return true 如果两个值对象在业务上相等
     */
    public boolean sameValueAs(ValueObjectBase other) {
        return equals(other);
    }
}
