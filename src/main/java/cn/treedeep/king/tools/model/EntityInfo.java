package cn.treedeep.king.tools.model;

/**
 * 实体信息
 *
 * @author 周广明
 * @since 2025-06-13
 */
public class EntityInfo {
    private final String name;
    private final String comment;

    public EntityInfo(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
