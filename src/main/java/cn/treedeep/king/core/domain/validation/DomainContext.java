package cn.treedeep.king.core.domain.validation;

/**
 * 领域上下文接口
 * 用于传递验证所需的上下文信息
 */
public interface DomainContext {
    /**
     * 获取上下文中的值
     *
     * @param key 上下文键
     * @return 上下文值
     */
    Object get(String key);

    /**
     * 设置上下文值
     *
     * @param key   上下文键
     * @param value 上下文值
     */
    void set(String key, Object value);
}
