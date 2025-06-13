package cn.treedeep.king.tools.model;

import java.util.List;

/**
 * 模块信息
 *
 * @author 周广明
 * @since 2025-06-13
 */
public class ModuleInfo {
    private final String name;
    private final String comment;
    private final List<EntityInfo> entities;

    private ModuleInfo(String name, String comment, List<EntityInfo> entities) {
        this.name = name;
        this.comment = comment;
        this.entities = entities;
    }

    public static ModuleInfo create(String name, String comment, List<EntityInfo> entities) {
        return new ModuleInfo(name, comment, entities);
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public List<EntityInfo> getEntities() {
        return entities;
    }
}
