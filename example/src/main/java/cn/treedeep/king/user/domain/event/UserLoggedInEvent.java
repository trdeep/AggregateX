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
 * 用户登录事件
 */
@Entity
@Table(name = "user_logged_in_events")
@Comment("用户登录事件表")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLoggedInEvent extends DomainEvent {

    @Column(name = "user_id", length = 36)
    @Comment("用户ID")
    private String userId;

    @Column(name = "login_ip", length = 45)
    @Comment("登录IP地址")
    private String loginIp;

    @Column(name = "user_agent", length = 500)
    @Comment("用户代理")
    private String userAgent;

    public UserLoggedInEvent(UserId userId, String loginIp, String userAgent) {
        super();
        this.userId = userId.getValue();
        this.loginIp = loginIp;
        this.userAgent = userAgent;
        setAggregateId(userId.getValue());
    }
}
