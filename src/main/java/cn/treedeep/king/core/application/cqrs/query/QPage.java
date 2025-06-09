package cn.treedeep.king.core.application.cqrs.query;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 分页查询结果
 *
 * @param <T> 结果类型
 */
@Data
@Builder
public class QPage<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
