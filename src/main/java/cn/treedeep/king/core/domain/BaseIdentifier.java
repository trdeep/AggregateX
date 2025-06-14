package cn.treedeep.king.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

/**
 * 标识符基类
 * <p>
 * 为领域模型提供统一的标识符实现。所有的领域标识符都需要继承此类。
 * 支持自动生成的UUID和手动指定的标识符。
 * <p>
 * 标识符特征：<br>
 * 1. 不可变性：一旦创建不可修改<br>
 * 2. 唯一性：在特定上下文中具有唯一性<br>
 * 3. 持久性：可以被持久化和恢复<br>
 * 4. 可序列化：支持系统间传输
 * <p>
 * 使用场景：<br>
 * 1. 实体和聚合根的标识符<br>
 * 2. 跨界上下文的引用<br>
 * 3. 分布式系统中的全局唯一标识
 * <p>
 * 示例：
 * <pre>
 * {@code
 * public class OrderId extends BaseIdentifier {
 *     public OrderId() {
 *         super(); // 使用自动生成的UUID
 *     }
 *
 *     public OrderId(String id) {
 *         super(id); // 使用指定的标识符
 *     }
 * }
 * }
 * </pre>
 */
@Getter
@MappedSuperclass
public abstract class BaseIdentifier implements Identifier {

    /**
     * 标识符值
     */
    @Column(name = "id", length = 36, nullable = false)
    @Comment("ID")
    private String value;

    /**
     * 默认构造函数，生成UUID作为标识符
     */
    protected BaseIdentifier() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * 使用指定的标识符值构造
     *
     * @param id 标识符值
     */
    protected BaseIdentifier(String id) {
        this.value = id;
    }

    /**
     * 设置标识符值
     *
     * @param value 标识符值
     */
    protected void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseIdentifier that = (BaseIdentifier) obj;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), getValue());
    }
}
