package cn.treedeep.king.core.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 错误响应
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /**
     * 错误代码
     */
    private String code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误详情
     */
    private Object details;
}
