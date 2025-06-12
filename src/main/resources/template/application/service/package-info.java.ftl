/**
 * <h1>关键设计原则</h1>
 * <p>
 * 应用层服务，流程编排（What to do）。
 * <p>
 * 薄层：仅协调流程，不实现业务逻辑。
 * <p>
 * 用例导向：一个方法对应一个业务用例（如 submit${moduleNameCamel}）。
 * <p>
 * 技术无关性：不直接处理数据库、消息队列等细节。
 */
package ${packageName}.${moduleNameLower}.application.service;
