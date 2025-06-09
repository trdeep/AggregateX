package cn.treedeep.king.user.infrastructure.config;

import cn.treedeep.king.core.application.cqrs.command.CommandBus;
import cn.treedeep.king.core.application.cqrs.query.QueryBus;
import cn.treedeep.king.user.application.command.LoginUserCommand;
import cn.treedeep.king.user.application.command.LoginUserCommandHandler;
import cn.treedeep.king.user.application.command.RegisterUserCommand;
import cn.treedeep.king.user.application.command.RegisterUserCommandHandler;
import cn.treedeep.king.user.application.query.UserListQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 用户模块配置
 */
@Configuration
@RequiredArgsConstructor
public class UserModuleConfig {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final RegisterUserCommandHandler registerUserCommandHandler;
    private final LoginUserCommandHandler loginUserCommandHandler;
    private final UserListQueryHandler userListQueryHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void registerHandlers() {
        // 注册命令处理器
        commandBus.register(RegisterUserCommand.class, registerUserCommandHandler);
        commandBus.register(LoginUserCommand.class, loginUserCommandHandler);

        // 注册查询处理器
        queryBus.register(userListQueryHandler);
    }
}
