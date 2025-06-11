package cn.treedeep.king.core.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.types.Identifier;

/**
 * 实体基类
 * <p>
 * 所有的领域实体都需要继承此类。提供了实体的基本特性和行为。
 * <p>
 * 实体特征：<br>
 * 1. 具有唯一标识符（ID）<br>
 * 2. 具有生命周期和状态<br>
 * 3. 可变的，状态可以改变<br>
 * 4. 相等性通过标识符判断
 * <p>
 * 实现说明：<br>
 * 1. 使用JPA注解支持持久化<br>
 * 2. 内置版本控制支持乐观锁<br>
 * 3. 提供统一的相等性比较<br>
 * 4. 支持领域驱动设计的实体概念
 * <p>
 * 使用规范：<br>
 * 1. 实体必须定义唯一标识符<br>
 * 2. 避免使用复杂的继承层次<br>
 * 3. 保持实体的职责单一<br>
 * 4. 确保实体的不变性约束
 * <p>
 * 示例：
 * <pre>
 * {@code
 * public class Product extends EntityBase<ProductId> {
 *     private String name;
 *     private Money price;
 *
 *     public void updatePrice(Money newPrice) {
 *         this.price = newPrice;
 *     }
 * }
 * }
 * </pre>
 *
 * @param <ID> 实体标识符类型
 */
@Getter
@Entity
@MappedSuperclass
public abstract class EntityBase<ID extends Identifier> {

    @Version
    @Comment("版本号，用于乐观锁控制")
    private Long version;

    /**
     * 获取实体标识符
     *
     * @return 实体标识符
     */
    public abstract ID getId();


    @Getter
    @Comment("是否已删除")
    private boolean isDeleted;

    /**
     * 标记实体是否为"脏"状态（已变更但未持久化）。
     *
     * <p>核心用途：
     * <ol>
     *   <li><b>持久化优化</b> - 仅保存脏实体，避免全量更新</li>
     *   <li><b>变更追踪</b> - 识别需要生成领域事件的实体</li>
     *   <li><b>乐观锁协同</b> - 与{@code version}字段配合控制并发</li>
     *   <li><b>审计支持</b> - 记录变更实体用于日志</li>
     * </ol>
     *
     * <p>使用规范：
     * <ul>
     *   <li>修改实体属性后<em>必须</em>调用{@link #markAsDirty()}</li>
     *   <li>持久化成功后<em>必须</em>调用{@link #clearDirtyFlag()}</li>
     *   <li>子实体变更时需同步标记父聚合根为脏</li>
     * </ul>
     *
     * @see #getVersion() 乐观锁版本号
     * @see #isDeleted() 删除状态标记
     */
    @Getter
    @Comment("是否为\"脏\"状态（已变更但未持久化）")
    private boolean isDirty;

    /**
     * 标记实体为已删除
     */
    protected void markAsDeleted() {
        this.isDeleted = true;
        this.markAsDirty();
    }

    /**
     * 标记实体状态已变更
     */
    protected void markAsDirty() {
        this.isDirty = true;
    }

    /**
     * 清除状态变更标记
     */
    protected void clearDirtyFlag() {
        this.isDirty = false;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EntityBase<?> that = (EntityBase<?>) obj;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }
}
