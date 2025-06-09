package cn.treedeep.king.user.presentation.dto;

import cn.treedeep.king.user.application.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户列表响应
 */
@Data
@Schema(description = "用户列表响应")
public class UserListResponse {

    @Schema(description = "用户列表")
    private List<UserDto> users;

    @Schema(description = "总数量")
    private long totalElements;

    @Schema(description = "总页数")
    private int totalPages;

    @Schema(description = "当前页码")
    private int currentPage;

    @Schema(description = "每页大小")
    private int pageSize;

    @Schema(description = "是否有下一页")
    private boolean hasNext;

    @Schema(description = "是否有上一页")
    private boolean hasPrevious;
}
