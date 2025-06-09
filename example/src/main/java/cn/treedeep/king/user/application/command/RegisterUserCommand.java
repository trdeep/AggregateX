package cn.treedeep.king.user.application.command;

import cn.treedeep.king.core.application.cqrs.command.Command;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户注册命令
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterUserCommand extends Command {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 255, message = "邮箱长度不能超过255个字符")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    public RegisterUserCommand(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getAggregateId() {
        // 注册命令针对新用户，使用用户名作为标识
        return username;
    }
}
