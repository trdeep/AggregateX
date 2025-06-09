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
public class CommandValidator {

    private final Validator validator;

    /**
     * 设置是否启用快速失败模式
     * <p>
     * 在快速失败模式下，发现第一个验证错误时就会抛出异常
     */
    private boolean failFast = true;

    /**
     * 设置是否启用验证
     * <p>
     * 如果禁用验证，validate方法将直接返回而不进行任何验证
     */
    private boolean validationEnabled = true;

    public CommandValidator(Validator validator) {
        this.validator = validator;
    }


    /**
     * 验证命令标识符
     */
    private void validateCommandIdentifier(Command command) {
        if (command.getCommandId() == null || command.getCommandId().trim().isEmpty()) {
            throw new ValidationException("Command ID cannot be empty");
        }
    }

    /**
     * 验证时间戳
     */
    private void validateTimestamp(Command command) {
        if (command.getTimestamp() <= 0) {
            throw new ValidationException("Command timestamp must be greater than 0");
        }
    }

    /**
     * 验证命令
     *
     * @param command 要验证的命令
     * @throws ValidationException 如果验证失败
     */
    public void validate(Command command) {
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
