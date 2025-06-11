package cn.treedeep.king.core.domain.cqrs;

import java.io.Serializable;

/**
 * 命令基类
 * <p>
 * 在CQRS架构中，命令代表系统中的写操作意图，用于改变系统状态。
 * 所有业务命令都应该继承此基类，获得基础的命令功能。
 * <p>
 * 命令特征：
 * <ul>
 * <li>不可变性 - 命令一旦创建就不应该被修改</li>
 * <li>唯一标识 - 每个命令都有唯一的ID用于追踪</li>
 * <li>时间戳 - 记录命令创建的时间</li>
 * <li>目标聚合 - 明确指定要操作的聚合根</li>
 * </ul>
 * <p>
 * 设计原则：
 * <ul>
 * <li>命令应该表达业务意图，而不是技术操作</li>
 * <li>命令名称应该使用动词，如CreateOrder、CancelPayment</li>
 * <li>命令应该包含执行操作所需的所有数据</li>
 * <li>避免在命令中包含复杂的业务逻辑</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 * {@code
 * public class CreateOrderCommand extends Command {
 *     private final String customerId;
 *     private final List<OrderItem> items;
 *     
 *     @Override
 *     public String getAggregateId() {
 *         return UUID.randomUUID().toString();
 *     }
 * }
 * }
 * </pre>
 */
public abstract class Command implements Serializable {
    private final String commandId;
    private final long timestamp;

    /**
     * 受保护的构造函数，用于初始化命令基础属性
     */
    protected Command() {
        this.commandId = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 获取命令唯一标识符
     * 
     * @return 命令ID
     */
    public String getCommandId() {
        return commandId;
    }

    /**
     * 获取命令创建时间戳
     * 
     * @return 时间戳（毫秒）
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 获取聚合根ID
     * 子类必须实现此方法以标识命令要操作的聚合根
     * 
     * @return 聚合根ID
     */
    public abstract String getAggregateId();
}
