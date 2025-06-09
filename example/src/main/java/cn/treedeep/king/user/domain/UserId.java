package cn.treedeep.king.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.types.Identifier;

import java.util.Objects;
import java.util.UUID;

/**
 * 用户唯一标识符
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserId implements Identifier {

    @Column(name = "user_id", length = 36)
    private String value;

    private UserId(String value) {
        this.value = Objects.requireNonNull(value, "用户ID不能为空");
    }

    /**
     * 生成新的用户ID
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    /**
     * 从字符串创建用户ID
     */
    public static UserId of(String value) {
        return new UserId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
