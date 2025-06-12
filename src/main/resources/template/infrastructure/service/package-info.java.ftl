/**
 * <h1>关键设计原则</h1>
 * <p>
 * 基础设施层服务，技术实现（How to do）。
 * <p>
 * 技术实现：封装数据库、缓存、外部API等底层操作。
 * <p>
 * 可替换性：通过接口抽象（如 ${moduleNameCamel}Repository），便于切换实现（如从MySQL到MongoDB）。
 * <p>
 * 无业务逻辑：仅执行技术指令，不参与业务决策。
 */
package ${packageName}.${moduleNameLower}.infrastructure.service;
