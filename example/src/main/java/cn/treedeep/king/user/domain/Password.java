package cn.treedeep.king.user.domain;

import cn.treedeep.king.core.domain.ValueObjectBase;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

/**
 * 密码值对象
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ValueObject
public class Password extends ValueObjectBase {

    @Column(name = "password_hash", length = 255)
    private String hashedValue;

    private Password(String hashedValue) {
        this.hashedValue = Objects.requireNonNull(hashedValue, "密码哈希值不能为空");
    }

    public static Password of(String hashedValue) {
        return new Password(hashedValue);
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{hashedValue};
    }
}
