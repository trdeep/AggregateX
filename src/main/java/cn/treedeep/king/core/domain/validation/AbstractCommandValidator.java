package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.cqrs.Command;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.Setter;

import java.util.Set;

/**
 * 命令验证器
 * <p>
 * 负责验证命令的合法性
 */
@Setter
public abstract class AbstractCommandValidator<C extends Command> implements CommandValidator<C> {

    /**
     * 验证命令
     *
     * @param validator         用于验证命令的Validator实例
     * @param validationEnabled 设置是否启用快速失败模式（如果禁用验证，validate方法将直接返回而不进行任何验证）
     * @param failFast          设置是否启用快速失败模式（在快速失败模式下，发现第一个验证错误时就会抛出异常）
     * @param command           要验证的命令
     * @throws ValidationException 如果验证失败
     */
    public void doValidate(Validator validator, boolean validationEnabled, boolean failFast, C command) {
        if (!validationEnabled) {
            return;
        }

        // 基本验证
        validateCommandIdentifier(command);
        validateTimestamp(command);

        // Bean Validation
        Set<ConstraintViolation<Command>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            if (failFast) {
                // 快速失败模式：只返回第一个错误
                ConstraintViolation<Command> violation = violations.iterator().next();
                throw new ValidationException(String.format("Command validation failed: %s %s",
                        violation.getPropertyPath(), violation.getMessage()));
            } else {
                // 收集所有错误
                throw new ValidationException(buildValidationMessage(violations));
            }
        }

        // 自定义验证
        validate(command);
    }


    /**
     * 验证命令标识符
     */
    private void validateCommandIdentifier(C command) {
        if (command.getCommandId() == null || command.getCommandId().trim().isEmpty()) {
            throw new ValidationException("Command ID cannot be empty");
        }
    }

    /**
     * 验证时间戳
     */
    private void validateTimestamp(C command) {
        if (command.getTimestamp() <= 0) {
            throw new ValidationException("Command timestamp must be greater than 0");
        }
    }

    private String buildValidationMessage(Set<ConstraintViolation<Command>> violations) {
        StringBuilder message = new StringBuilder();
        message.append("Command validation failed: ");
        violations.forEach(violation ->
                message.append(violation.getPropertyPath())
                        .append(" ")
                        .append(violation.getMessage())
                        .append("; ")
        );
        return message.toString();
    }
}
