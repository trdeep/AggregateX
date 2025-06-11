/**
 * 领域服务：<b>封装聚合根或值对象无法单独处理的业务逻辑。</b>
 *
 * <p><h1>核心职责</h1>
 * <ul>
 *   <li>处理跨聚合根的领域逻辑（如${moduleComment}支付涉及${moduleComment}和账户聚合）</li>
 *   <li>实现无状态的业务规则（如运费计算、促销折扣策略）</li>
 *   <li>避免领域逻辑泄露到应用层</li>
 * </ul>
 *
 * <p><b>示例：</b>
 * {@code ${moduleNameCamel}PaymentService.processPayment()} 协调${moduleComment}和账户的支付逻辑，
 * {@code PricingService.calculateDiscount()} 计算动态折扣。
 */
package cn.treedeep.king.${moduleNameLower}.domain.service;
