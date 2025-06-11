package cn.treedeep.king.${moduleNameLower}.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * 创建${moduleComment}请求
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@Getter
@Setter
@Schema(description = "创建${moduleComment}请求")
public class Create${moduleNameCamel}Request {

    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称长度不能超过100个字符")
    @Schema(description = "名称", example = "示例${moduleNameCamel}")
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    @Schema(description = "描述", example = "这是一个示例${moduleNameCamel}")
    private String description;
}
