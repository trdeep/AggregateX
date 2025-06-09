package cn.treedeep.king.core.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实体变更跟踪器
 * <p>
 * 负责追踪聚合根内实体的变更状态:
 * 1. 新增的实体
 * 2. 修改的实体
 * 3. 删除的实体
 */
public class EntityChangeTracker {

    @Getter
    private final Set<EntityBase<?>> addedEntities = new HashSet<>();

    @Getter
    private final Set<EntityBase<?>> modifiedEntities = new HashSet<>();

    @Getter
    private final Set<EntityBase<?>> deletedEntities = new HashSet<>();

    /**
     * 标记实体为新增
     */
    public void trackNewEntity(EntityBase<?> entity) {
        addedEntities.add(entity);
    }

    /**
     * 标记实体为已修改
     */
    public void trackModifiedEntity(EntityBase<?> entity) {
        if (!addedEntities.contains(entity)) {
            modifiedEntities.add(entity);
        }
    }

    /**
     * 标记实体为已删除
     */
    public void trackDeletedEntity(EntityBase<?> entity) {
        addedEntities.remove(entity);
        modifiedEntities.remove(entity);
        deletedEntities.add(entity);
    }

    /**
     * 获取所有变更的实体
     */
    public List<EntityBase<?>> getAllChangedEntities() {
        List<EntityBase<?>> allChanges = new ArrayList<>();
        allChanges.addAll(addedEntities);
        allChanges.addAll(modifiedEntities);
        allChanges.addAll(deletedEntities);
        return allChanges;
    }

    /**
     * 清除变更跟踪
     */
    public void clearTracking() {
        addedEntities.clear();
        modifiedEntities.clear();
        deletedEntities.clear();
    }

    /**
     * 判断实体是否有变更
     */
    public boolean hasChanges(EntityBase<?> entity) {
        return addedEntities.contains(entity) ||
               modifiedEntities.contains(entity) ||
               deletedEntities.contains(entity);
    }

    /**
     * 获取实体的变更类型
     */
    public EntityChangeType getChangeType(EntityBase<?> entity) {
        if (addedEntities.contains(entity)) {
            return EntityChangeType.ADDED;
        }
        if (modifiedEntities.contains(entity)) {
            return EntityChangeType.MODIFIED;
        }
        if (deletedEntities.contains(entity)) {
            return EntityChangeType.DELETED;
        }
        return EntityChangeType.UNCHANGED;
    }
}
