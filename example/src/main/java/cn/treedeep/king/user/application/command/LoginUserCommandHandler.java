package cn.treedeep.king.user.application.command;

import cn.treedeep.king.core.application.cqrs.command.AbstractCommandHandler;
import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.domain.EventBus;
import cn.treedeep.king.core.domain.Repository;
import cn.treedeep.king.user.domain.Email;
import cn.treedeep.king.user.domain.User;
import cn.treedeep.king.user.domain.UserId;
import cn.treedeep.king.user.domain.UserRepository;
import cn.treedeep.king.user.domain.service.PasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户登录命令处理器
 */
@Component
@Slf4j
@Transactional
public class LoginUserCommandHandler extends AbstractCommandHandler<LoginUserCommand, User> {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    protected LoginUserCommandHandler(Repository<User, UserId> repository,
                                         EventBus eventBus,
                                         CommandBus commandBus,
                                         UserRepository userRepository,
                                         PasswordService passwordService) {

        super(repository, eventBus, commandBus);
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }


    @Override
    public User handleCommand(LoginUserCommand command) {
        log.info("处理用户登录命令: usernameOrEmail={}", command.getUsernameOrEmail());

        // 查找用户（支持用户名或邮箱登录）
        User user = findUser(command.getUsernameOrEmail())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在或密码错误"));

        // 验证密码
        if (!passwordService.matches(command.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户不存在或密码错误");
        }

        // 执行登录逻辑
        user.login(command.getLoginIp(), command.getUserAgent());

        log.info("用户登录成功: userId={}, username={}", user.getId().getValue(), user.getUsername());
        return user;
    }

    private Optional<User> findUser(String usernameOrEmail) {
        // 首先尝试按用户名查找
        Optional<User> userByUsername = userRepository.findByUsername(usernameOrEmail);
        if (userByUsername.isPresent()) {
            return userByUsername;
        }

        // 如果用户名未找到，尝试按邮箱查找
        try {
            Email email = Email.of(usernameOrEmail);
            return userRepository.findByEmail(email);
        } catch (IllegalArgumentException e) {
            // 如果不是有效的邮箱格式，返回空
            return Optional.empty();
        }
    }
}
