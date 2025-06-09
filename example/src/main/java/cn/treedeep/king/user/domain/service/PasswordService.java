package cn.treedeep.king.user.domain.service;

import cn.treedeep.king.user.domain.Password;

/**
 * 密码服务接口
 */
public interface PasswordService {

    /**
     * 对原始密码进行哈希处理
     */
    Password hash(String rawPassword);

    /**
     * 验证原始密码与哈希密码是否匹配
     */
    boolean matches(String rawPassword, Password hashedPassword);

    /**
     * 验证原始密码与哈希字符串是否匹配
     */
    boolean matches(String rawPassword, String hashedPassword);
}
