package cn.treedeep.king.test;

import cn.treedeep.king.generator.config.DDDJsonConfigTool;
import lombok.extern.slf4j.Slf4j;

/**
 * JSON配置系统终极演示
 * <p>
 * 展示完整的JSON配置DDD代码生成工作流程
 *
 * @author 周广明
 * @since 2025-06-15
 */
@Slf4j
public class UltimateJsonConfigDemo {

    public static void main(String[] args) {
        try {
            new UltimateJsonConfigDemo().runUltimateDemo();
        } catch (Exception e) {
            log.error("❌ 演示过程中出现错误", e);
        }
    }

    public void runUltimateDemo() {
        log.info("🎯 欢迎使用DDD模块JSON配置系统终极演示");
        log.info("🚀 这个演示将展示JSON配置系统的完整功能");

        DDDJsonConfigTool tool = new DDDJsonConfigTool();

        // 演示场景1: 快速生成演示项目
        demonstrateQuickDemo(tool);

        // 演示场景2: 电商项目配置和生成
        demonstrateECommerceProject(tool);

        // 演示场景3: 多模块项目配置
        demonstrateMultiModuleProject(tool);

        printFinalSummary();
    }

    /**
     * 演示场景1: 快速生成演示项目
     */
    private void demonstrateQuickDemo(DDDJsonConfigTool tool) {
        log.info("\n🎬 演示场景1: 快速生成演示项目");
        log.info("📖 用例: 新手用户想要快速了解系统功能");

        String demoPath = "/tmp/ddd-ultimate-demo";

        // 一键生成演示项目
        tool.quickDemo(demoPath, "quick-start-demo", "com.example.quickstart");

        log.info("✨ 场景1完成: 快速演示项目已生成");
        log.info("💡 用户可以查看生成的配置文件和代码结构");
    }

    /**
     * 演示场景2: 电商项目配置和生成
     */
    private void demonstrateECommerceProject(DDDJsonConfigTool tool) {
        log.info("\n🎬 演示场景2: 电商项目配置和生成");
        log.info("📖 用例: 开发团队需要为电商系统生成多个模块");

        String configPath = "/tmp/ecommerce-config.json";
        String projectPath = "/tmp/ecommerce-system";

        // Step 1: 创建电商项目的示例配置
        log.info("📝 Step 1: 创建电商项目配置模板");
        tool.createExampleConfig(configPath);

        // Step 2: 验证配置文件
        log.info("✅ Step 2: 验证配置文件格式");
        boolean isValid = tool.validateConfig(configPath);
        log.info("验证结果: {}", isValid ? "通过" : "失败");

        // Step 3: 生成电商系统代码
        if (isValid) {
            log.info("🏗️ Step 3: 生成电商系统代码");
            tool.generateFromConfig(
                configPath,
                projectPath,
                "com.ecommerce.system",
                true,
                "ECommerce Team",
                "ECommerce Corporation"
            );
        }

        log.info("✨ 场景2完成: 电商系统代码已生成");
        log.info("📂 项目位置: {}", projectPath);
    }

    /**
     * 演示场景3: 多模块项目配置
     */
    private void demonstrateMultiModuleProject(DDDJsonConfigTool tool) {
        log.info("\n🎬 演示场景3: 多模块项目配置");
        log.info("📖 用例: 大型项目需要管理多个DDD模块的配置");

        // 创建用户模块配置
        createUserModuleConfig(tool);

        // 创建订单模块配置
        createOrderModuleConfig(tool);

        // 创建支付模块配置
        createPaymentModuleConfig(tool);

        // 生成完整的多模块项目
        generateMultiModuleProject(tool);

        log.info("✨ 场景3完成: 多模块项目已生成");
    }

    private void createUserModuleConfig(DDDJsonConfigTool tool) {
        log.info("👥 创建用户模块配置");

        String userConfig = """
            [
              {
                "name": "user",
                "comment": "用户管理模块",
                "aggregateRoots": [
                  {
                    "name": "User",
                    "comment": "用户聚合根",
                    "properties": [
                      {
                        "name": "userId",
                        "comment": "用户ID",
                        "type": "REGULAR"
                      },
                      {
                        "name": "username",
                        "comment": "用户名",
                        "type": "REGULAR"
                      },
                      {
                        "name": "email",
                        "comment": "邮箱",
                        "type": "REGULAR"
                      },
                      {
                        "name": "status",
                        "comment": "状态",
                        "type": "REGULAR"
                      }
                    ],
                    "entities": [],
                    "valueObjects": [],
                    "methods": [
                      {
                        "name": "register",
                        "comment": "用户注册",
                        "returnType": "void",
                        "parameters": [
                          {
                            "name": "username",
                            "type": "String",
                            "comment": "用户名"
                          },
                          {
                            "name": "email",
                            "type": "String",
                            "comment": "邮箱"
                          }
                        ]
                      },
                      {
                        "name": "activate",
                        "comment": "激活账户",
                        "returnType": "void",
                        "parameters": []
                      }
                    ]
                  }
                ],
                "domainEvents": [
                  {
                    "name": "UserRegisteredEvent",
                    "comment": "用户注册事件",
                    "aggregateRootName": "User",
                    "tableName": "user_registered_events",
                    "fields": [
                      {
                        "name": "userId",
                        "type": "String",
                        "comment": "用户ID",
                        "columnName": "user_id"
                      },
                      {
                        "name": "username",
                        "type": "String",
                        "comment": "用户名",
                        "columnName": "username"
                      },
                      {
                        "name": "registrationTime",
                        "type": "LocalDateTime",
                        "comment": "注册时间",
                        "columnName": "registration_time"
                      }
                    ]
                  }
                ],
                "applicationServices": [
                  {
                    "name": "UserService",
                    "comment": "用户服务",
                    "interfaceName": "UserService",
                    "implementationName": "UserServiceImpl",
                    "methods": [
                      {
                        "name": "registerUser",
                        "comment": "注册用户",
                        "returnType": "String",
                        "parameters": [
                          {
                            "name": "username",
                            "type": "String",
                            "comment": "用户名"
                          },
                          {
                            "name": "email",
                            "type": "String",
                            "comment": "邮箱"
                          }
                        ]
                      },
                      {
                        "name": "activateUser",
                        "comment": "激活用户",
                        "returnType": "void",
                        "parameters": [
                          {
                            "name": "userId",
                            "type": "String",
                            "comment": "用户ID"
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ]
            """;

        tool.generateFromJsonString(userConfig, "/Users/zhougm/vscode/KingAdmin", "com.multimodule");
        log.info("✅ 用户模块代码已生成");
    }

    private void createOrderModuleConfig(DDDJsonConfigTool tool) {
        log.info("📦 创建订单模块配置");

        String orderConfig = """
            [
              {
                "name": "order",
                "comment": "订单管理模块",
                "aggregateRoots": [
                  {
                    "name": "Order",
                    "comment": "订单聚合根",
                    "properties": [
                      {
                        "name": "orderId",
                        "comment": "订单ID",
                        "type": "REGULAR"
                      },
                      {
                        "name": "customerId",
                        "comment": "客户ID",
                        "type": "REGULAR"
                      },
                      {
                        "name": "status",
                        "comment": "订单状态",
                        "type": "REGULAR"
                      },
                      {
                        "name": "totalAmount",
                        "comment": "总金额",
                        "type": "REGULAR"
                      }
                    ],
                    "entities": [],
                    "valueObjects": [],
                    "methods": [
                      {
                        "name": "createOrder",
                        "comment": "创建订单",
                        "returnType": "void",
                        "parameters": [
                          {
                            "name": "customerId",
                            "type": "String",
                            "comment": "客户ID"
                          }
                        ]
                      },
                      {
                        "name": "confirm",
                        "comment": "确认订单",
                        "returnType": "void",
                        "parameters": []
                      },
                      {
                        "name": "cancel",
                        "comment": "取消订单",
                        "returnType": "void",
                        "parameters": [
                          {
                            "name": "reason",
                            "type": "String",
                            "comment": "取消原因"
                          }
                        ]
                      }
                    ]
                  }
                ],
                "domainEvents": [
                  {
                    "name": "OrderCreatedEvent",
                    "comment": "订单创建事件",
                    "aggregateRootName": "Order",
                    "tableName": "order_created_events",
                    "fields": [
                      {
                        "name": "orderId",
                        "type": "String",
                        "comment": "订单ID",
                        "columnName": "order_id"
                      },
                      {
                        "name": "customerId",
                        "type": "String",
                        "comment": "客户ID",
                        "columnName": "customer_id"
                      }
                    ]
                  }
                ],
                "applicationServices": [
                  {
                    "name": "OrderService",
                    "comment": "订单服务",
                    "interfaceName": "OrderService",
                    "implementationName": "OrderServiceImpl",
                    "methods": [
                      {
                        "name": "createOrder",
                        "comment": "创建订单",
                        "returnType": "String",
                        "parameters": [
                          {
                            "name": "customerId",
                            "type": "String",
                            "comment": "客户ID"
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ]
            """;

        tool.generateFromJsonString(orderConfig, "/Users/zhougm/vscode/KingAdmin", "com.multimodule");
        log.info("✅ 订单模块代码已生成");
    }

    private void createPaymentModuleConfig(DDDJsonConfigTool tool) {
        log.info("💳 创建支付模块配置");

        String paymentConfig = """
            [
              {
                "name": "payment",
                "comment": "支付管理模块",
                "aggregateRoots": [
                  {
                    "name": "Payment",
                    "comment": "支付聚合根",
                    "properties": [
                      {
                        "name": "paymentId",
                        "comment": "支付ID",
                        "type": "REGULAR"
                      },
                      {
                        "name": "orderId",
                        "comment": "订单ID",
                        "type": "REGULAR"
                      },
                      {
                        "name": "amount",
                        "comment": "支付金额",
                        "type": "REGULAR"
                      },
                      {
                        "name": "status",
                        "comment": "支付状态",
                        "type": "REGULAR"
                      }
                    ],
                    "entities": [],
                    "valueObjects": [],
                    "methods": [
                      {
                        "name": "processPayment",
                        "comment": "处理支付",
                        "returnType": "void",
                        "parameters": [
                          {
                            "name": "paymentMethod",
                            "type": "String",
                            "comment": "支付方式"
                          }
                        ]
                      },
                      {
                        "name": "refund",
                        "comment": "退款",
                        "returnType": "void",
                        "parameters": [
                          {
                            "name": "reason",
                            "type": "String",
                            "comment": "退款原因"
                          }
                        ]
                      }
                    ]
                  }
                ],
                "domainEvents": [
                  {
                    "name": "PaymentProcessedEvent",
                    "comment": "支付处理事件",
                    "aggregateRootName": "Payment",
                    "tableName": "payment_processed_events",
                    "fields": [
                      {
                        "name": "paymentId",
                        "type": "String",
                        "comment": "支付ID",
                        "columnName": "payment_id"
                      },
                      {
                        "name": "orderId",
                        "type": "String",
                        "comment": "订单ID",
                        "columnName": "order_id"
                      },
                      {
                        "name": "amount",
                        "type": "BigDecimal",
                        "comment": "支付金额",
                        "columnName": "amount"
                      }
                    ]
                  }
                ],
                "applicationServices": [
                  {
                    "name": "PaymentService",
                    "comment": "支付服务",
                    "interfaceName": "PaymentService",
                    "implementationName": "PaymentServiceImpl",
                    "methods": [
                      {
                        "name": "processPayment",
                        "comment": "处理支付",
                        "returnType": "String",
                        "parameters": [
                          {
                            "name": "orderId",
                            "type": "String",
                            "comment": "订单ID"
                          },
                          {
                            "name": "amount",
                            "type": "BigDecimal",
                            "comment": "支付金额"
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ]
            """;

        tool.generateFromJsonString(paymentConfig, "/Users/zhougm/vscode/KingAdmin", "com.multimodule");
        log.info("✅ 支付模块代码已生成");
    }

    private void generateMultiModuleProject(DDDJsonConfigTool tool) {
        log.info("🏗️ 生成多模块项目根结构");

        // 创建项目根配置
        String rootConfig = """
            [
              {
                "name": "common",
                "comment": "公共模块",
                "aggregateRoots": [],
                "domainEvents": [],
                "applicationServices": [
                  {
                    "name": "NotificationService",
                    "comment": "通知服务",
                    "interfaceName": "NotificationService",
                    "implementationName": "NotificationServiceImpl",
                    "methods": [
                      {
                        "name": "sendEmail",
                        "comment": "发送邮件",
                        "returnType": "void",
                        "parameters": [
                          {
                            "name": "to",
                            "type": "String",
                            "comment": "收件人"
                          },
                          {
                            "name": "subject",
                            "type": "String",
                            "comment": "主题"
                          },
                          {
                            "name": "content",
                            "type": "String",
                            "comment": "内容"
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ]
            """;

        tool.generateFromJsonString(rootConfig, "/Users/zhougm/vscode/KingAdmin", "com.multimodule");
        log.info("✅ 公共模块代码已生成");
    }

    private void printFinalSummary() {
        log.info("\n🎉 JSON配置系统终极演示完成！");
        log.info("\n📊 演示总结:");
        log.info("✅ 场景1: 快速演示项目 - 展示了一键生成功能");
        log.info("✅ 场景2: 电商项目 - 展示了完整的配置和生成流程");
        log.info("✅ 场景3: 多模块项目 - 展示了复杂项目的模块化管理");

        log.info("\n📂 生成的项目结构:");
        log.info("📁 /tmp/ddd-ultimate-demo/quick-start-demo/ - 快速演示项目");
        log.info("📁 /tmp/ecommerce-system/ - 电商系统项目");
        log.info("📁 /Users/zhougm/vscode/KingAdmin/ - 多模块项目");
        log.info("   ├── user-module/ - 用户管理模块");
        log.info("   ├── order-module/ - 订单管理模块");
        log.info("   ├── payment-module/ - 支付管理模块");
        log.info("   └── common-module/ - 公共模块");

        log.info("\n💡 接下来你可以:");
        log.info("1. 查看生成的代码结构和内容");
        log.info("2. 导入项目到IDE中进行开发");
        log.info("3. 修改JSON配置文件并重新生成");
        log.info("4. 基于生成的代码开始业务开发");
        log.info("5. 参考生成的代码结构设计自己的DDD项目");

        log.info("\n🚀 JSON配置系统已准备就绪，开始你的DDD之旅吧！");
    }
}
