package cn.treedeep.king.user.domain;

import cn.treedeep.king.core.domain.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 用户仓储接口
 */
public interface UserRepository extends Repository<User, UserId> {

    /**
     * 根据 ID 查找用户
     */
    Optional<User> findById(UserId id);

    /**
     * 删除用户
     */
    void delete(User user);

    /**
     * 根据 ID 删除用户
     */
    void deleteById(UserId id);

    /**
     * 检查用户是否存在
     */
    boolean existsById(UserId id);

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(Email email);

    /**
     * 检查用户名是否已存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmail(Email email);

    /**
     * 分页查询用户
     */
    Page<User> findAll(Pageable pageable);

    /**
     * 根据用户名模糊查询并分页
     */
    Page<User> findByUsernameContaining(String username, Pageable pageable);

    /**
     * 根据邮箱模糊查询并分页
     */
    Page<User> findByEmailValueContaining(String email, Pageable pageable);

    /**
     * 根据状态查询并分页
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * 复合条件查询并分页
     */
    Page<User> findByConditions(String username, String email, UserStatus status, Pageable pageable);
}
