package cn.treedeep.king.core.domain.validation;

import cn.treedeep.king.core.domain.cqrs.Command;
import jakarta.validation.ValidationException;

/**
 * 命令验证器
 * <p>
 * 负责验证命令的合法性
 */
public interface CommandValidator {

    /**
     * 验证命令
     *
     * @param command 要验证的命令
     * @throws ValidationException 如果验证失败
     */
    void validate(Command command);

}
