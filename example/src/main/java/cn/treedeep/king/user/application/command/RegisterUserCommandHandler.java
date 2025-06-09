package cn.treedeep.king.user.application.command;

import cn.treedeep.king.core.application.cqrs.command.AbstractCommandHandler;
import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.domain.EventBus;
import cn.treedeep.king.core.domain.Repository;
import cn.treedeep.king.user.domain.*;
import cn.treedeep.king.user.domain.service.PasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户注册命令处理器
 */
@Component
@Slf4j
@Transactional
public class RegisterUserCommandHandler extends AbstractCommandHandler<RegisterUserCommand, User> {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    protected RegisterUserCommandHandler(Repository<User, UserId> repository,
                                         EventBus eventBus,
                                         CommandBus commandBus,
                                         UserRepository userRepository,
                                         PasswordService passwordService) {

        super(repository, eventBus, commandBus);
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public User handleCommand(RegisterUserCommand command) {
        log.info("处理用户注册命令: username={}, email={}", command.getUsername(), command.getEmail());

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(command.getUsername())) {
            throw new IllegalArgumentException("用户名已存在: " + command.getUsername());
        }

        // 检查邮箱是否已存在
        Email email = Email.of(command.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已存在: " + command.getEmail());
        }

        // 对密码进行哈希处理
        Password hashedPassword = passwordService.hash(command.getPassword());

        // 创建用户聚合根
        UserId userId = UserId.generate();
        User user = User.create(userId, command.getUsername(), email, hashedPassword);

        log.info("用户注册成功: userId={}, username={}", userId.getValue(), command.getUsername());

        return user;
    }

}
