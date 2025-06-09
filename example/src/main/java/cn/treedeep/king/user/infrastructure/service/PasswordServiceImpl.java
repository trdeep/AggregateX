package cn.treedeep.king.user.infrastructure.service;

import cn.treedeep.king.user.domain.Password;
import cn.treedeep.king.user.domain.service.PasswordService;
import org.springframework.stereotype.Service;

/**
 * 密码服务实现
 */
@Service
public class PasswordServiceImpl implements PasswordService {

    @Override
    public Password hash(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("原始密码不能为空");
        }
        String hashedPassword = rawPassword + "ABC";
        return Password.of(hashedPassword);
    }

    @Override
    public boolean matches(String rawPassword, Password hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        return (rawPassword + "ABC").equals(hashedPassword.getHashedValue());
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        return (rawPassword + "ABC").equals(hashedPassword);
    }
}
