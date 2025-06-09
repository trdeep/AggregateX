package cn.treedeep.king.user.infrastructure.repository;

import cn.treedeep.king.core.domain.AbstractRepository;
import cn.treedeep.king.core.domain.DomainEventPublisher;
import cn.treedeep.king.core.domain.EventStore;
import cn.treedeep.king.user.domain.*;
import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户仓储实现（最终使用的是这个）
 */
@Primary
@Repository
public class UserRepositoryImpl extends AbstractRepository<User, UserId> implements UserRepository {

    @Resource(name = "userJpaRepository")
    private UserRepository userRepository;

    public UserRepositoryImpl(CacheManager cacheManager,
                              EventStore eventStore,
                              DomainEventPublisher eventPublisher) {
        super(cacheManager, eventStore, eventPublisher);
    }

    @Override
    protected Optional<User> doLoad(UserId userId) {
        return userRepository.findById(userId);
    }

    @Override
    protected void doSave(User aggregate) {
        userRepository.save(aggregate);
    }

    @Override
    public void deleteById(UserId id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UserId id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByUsernameContaining(String username, Pageable pageable) {
        return userRepository.findByUsernameContaining(username, pageable);
    }

    @Override
    public Page<User> findByEmailValueContaining(String email, Pageable pageable) {
        return userRepository.findByEmailValueContaining(email, pageable);
    }

    @Override
    public Page<User> findByStatus(UserStatus status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<User> findByConditions(String username, String email, UserStatus status, Pageable pageable) {
        return userRepository.findByConditions(username, email, status, pageable);
    }
}
