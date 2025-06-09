package cn.treedeep.king.user.application.command;

import cn.treedeep.king.core.application.cqrs.command.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登录命令
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserCommand extends Command {

    @NotBlank(message = "用户名或邮箱不能为空")
    private String usernameOrEmail;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String loginIp;

    private String userAgent;

    public LoginUserCommand(String usernameOrEmail, String password, String loginIp, String userAgent) {
        super();
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.loginIp = loginIp;
        this.userAgent = userAgent;
    }

    @Override
    public String getAggregateId() {
        // 登录命令不针对特定聚合根，返回用户名或邮箱作为标识
        return usernameOrEmail;
    }
}
