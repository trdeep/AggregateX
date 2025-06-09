package cn.treedeep.king.user.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
import cn.treedeep.king.user.domain.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 用户注册事件
 */
@Entity
@Table(name = "user_registered_events")
@Comment("用户注册事件表")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRegisteredEvent extends DomainEvent {

    @Column(name = "user_id", length = 36)
    @Comment("用户ID")
    private String userId;

    @Column(name = "username", length = 50)
    @Comment("用户名")
    private String username;

    @Column(name = "email", length = 255)
    @Comment("邮箱地址")
    private String email;

    public UserRegisteredEvent(UserId userId, String username, String email) {
        super();
        this.userId = userId.getValue();
        this.username = username;
        this.email = email;
        setAggregateId(userId.getValue());
    }
}
