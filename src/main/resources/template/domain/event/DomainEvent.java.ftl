package ${packageName}.${moduleNameLower}.domain.event;

import cn.treedeep.king.core.domain.DomainEvent;
import ${packageName}.${moduleNameLower}.domain.${entityNameCamel}Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "say_hello_events")
@Comment("XXX事件表")
@NoArgsConstructor
@Getter
public class SayHelloEvent extends DomainEvent {

    public SayHelloEvent(${entityNameCamel}Id aggregateId, String data) {
        super(aggregateId);
        setAggregateId(aggregateId.getValue());
        setData(data);
    }
}
