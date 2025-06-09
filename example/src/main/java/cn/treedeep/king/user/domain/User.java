package cn.treedeep.king.user.domain;

import cn.treedeep.king.core.domain.AggregateRoot;
import cn.treedeep.king.user.domain.event.UserLoggedInEvent;
import cn.treedeep.king.user.domain.event.UserRegisteredEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 用户聚合根
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Comment("用户表")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends AggregateRoot<UserId> {

    @EmbeddedId
    @Comment("用户ID")
    private UserId id;

    @Column(name = "username", length = 50, nullable = false)
    @Comment("用户名")
    private String username;

    @Embedded
    @Comment("邮箱")
    private Email email;

    @Embedded
    @Comment("密码")
    private Password password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Comment("用户状态")
    private UserStatus status;

    @Column(name = "last_login_at")
    @Comment("最后登录时间")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", length = 45)
    @Comment("最后登录IP")
    private String lastLoginIp;

    @Column(name = "login_count", nullable = false)
    @Comment("登录次数")
    private Integer loginCount = 0;

    /**
     * 创建新用户
     */
    public static User create(UserId userId, String username, Email email, Password password) {
        User user = new User();
        user.id = userId;
        user.username = username;
        user.email = email;
        user.password = password;
        user.status = UserStatus.ACTIVE;
        user.loginCount = 0;

        // 发布用户注册事件
        user.registerEvent(new UserRegisteredEvent(userId, username, email.getValue()));

        return user;
    }

    /**
     * 用户登录
     */
    public void login(String loginIp, String userAgent) {
        if (immutable()) {
            throw new IllegalStateException("用户已被删除，无法登录");
        }

        if (status == UserStatus.DISABLED) {
            throw new IllegalStateException("用户已被禁用，无法登录");
        }

        this.lastLoginAt = LocalDateTime.now();
        this.lastLoginIp = loginIp;
        this.loginCount++;
        updateVersion();
        updateLastModifiedAt();

        // 发布用户登录事件
        registerEvent(new UserLoggedInEvent(this.id, loginIp, userAgent));
    }

    /**
     * 禁用用户
     */
    public void disable() {
        if (immutable()) {
            throw new IllegalStateException("用户已被删除，无法禁用");
        }

        this.status = UserStatus.DISABLED;
        updateLastModifiedAt();
    }

    /**
     * 启用用户
     */
    public void enable() {
        if (immutable()) {
            throw new IllegalStateException("用户已被删除，无法启用");
        }

        this.status = UserStatus.ACTIVE;
        updateLastModifiedAt();
    }

    /**
     * 更新密码
     */
    public void changePassword(Password newPassword) {
        if (immutable()) {
            throw new IllegalStateException("用户已被删除，无法修改密码");
        }

        this.password = newPassword;
        updateLastModifiedAt();
    }

    /**
     * 检查密码是否匹配
     */
    public boolean checkPassword(String hashedPassword) {
        return this.password.getHashedValue().equals(hashedPassword);
    }

    @Override
    public UserId getId() {
        return id;
    }
}
