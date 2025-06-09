package cn.treedeep.king.user.infrastructure.repository;

import cn.treedeep.king.user.domain.User;
import cn.treedeep.king.user.domain.UserId;
import cn.treedeep.king.user.domain.UserRepository;
import cn.treedeep.king.user.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户 JPA 仓储接口
 */
@Repository("userJpaRepository")
public interface UserJpaRepository extends JpaRepository<User, UserId>, UserRepository {

    /**
     * 根据用户名查找用户
     */
    @Override
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否已存在
     */
    @Override
    boolean existsByUsername(String username);

    /**
     * 根据用户名模糊查询并分页
     */
    @Override
    Page<User> findByUsernameContaining(String username, Pageable pageable);

    /**
     * 根据状态查询并分页
     */
    @Override
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * 根据邮箱查找用户
     */
    @Query("SELECT u FROM User u WHERE u.email.value = :email")
    Optional<User> findByEmailValue(@Param("email") String email);

    /**
     * 检查邮箱是否已存在
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email.value = :email")
    boolean existsByEmailValue(@Param("email") String email);

    /**
     * 根据邮箱模糊查询并分页
     */
    @Override
    @Query("SELECT u FROM User u WHERE u.email.value LIKE %:email%")
    Page<User> findByEmailValueContaining(@Param("email") String email, Pageable pageable);

    /**
     * 复合条件查询并分页
     */
    @Override
    @Query("SELECT u FROM User u WHERE " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:email IS NULL OR u.email.value LIKE %:email%) AND " +
            "(:status IS NULL OR u.status = :status)")
    Page<User> findByConditions(@Param("username") String username,
                                @Param("email") String email,
                                @Param("status") UserStatus status,
                                Pageable pageable);
}
