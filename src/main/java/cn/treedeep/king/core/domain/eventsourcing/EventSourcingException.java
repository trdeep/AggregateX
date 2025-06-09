package cn.treedeep.king.core.domain.eventsourcing;

/**
 * 事件溯源异常
 */
public class EventSourcingException extends RuntimeException {

    public EventSourcingException(String message) {
        super(message);
    }

    public EventSourcingException(String message, Throwable cause) {
        super(message, cause);
    }
}
