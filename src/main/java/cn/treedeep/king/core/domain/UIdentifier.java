package cn.treedeep.king.core.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Copyright © 深圳市树深计算机系统有限公司 版权所有
 * <p>
 * 通用的标识符
 * </p>
 *
 * @author AggregateX
 * @since 2025-06-10 07:37:38
 */
@Embeddable
@Getter
@EqualsAndHashCode(callSuper = true)
public class UIdentifier extends BaseIdentifier {

    public UIdentifier() {
        super();
    }

    public UIdentifier(String value) {
        super(value);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
    }

    public static UIdentifier generate() {
        return new UIdentifier();
    }

    public static UIdentifier of(String id) {
        return new UIdentifier(id);
    }

}
