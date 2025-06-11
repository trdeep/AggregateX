package cn.treedeep.king.core.domain.validation;

/**
 * 上下文感知验证器接口
 * <p>
 * 提供基于上下文的验证功能，允许验证器访问和使用验证上下文。
 * 验证器可以：
 * <ul>
 *   <li>获取上下文信息</li>
 *   <li>基于上下文进行验证</li>
 *   <li>处理上下文相关的验证规则</li>
 * </ul>
 *
 * @param <T> 待验证对象类型
 */
public interface ContextAwareValidator<T> {

    /**
     * 在上下文中验证对象
     * <p>
     * 使用提供的验证上下文对目标对象进行验证。
     * 验证过程可以访问上下文中的信息，如：
     * <ul>
     *   <li>当前状态</li>
     *   <li>业务规则</li>
     *   <li>验证配置</li>
     * </ul>
     *
     * @param target  待验证的对象
     * @param context 验证上下文
     * @throws DomainValidationException 当验证失败时抛出
     */
    void validateWithContext(T target, DomainContext context);
}
