package cn.treedeep.king.shared.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * JSON5 解析器
 * <p>
 * 支持解析带注释的 JSON5 格式配置文件，包括：
 * <ul>
 *   <li>单行注释 (//)</li>
 *   <li>多行注释 (/x x/)</li>
 *   <li>尾随逗号</li>
 *   <li>更灵活的属性名（无需引号）</li>
 * </ul>
 *
 * @author 周广明
 * @since 2025-06-15
 */
@Slf4j
public final class Json5Parser {
    private Json5Parser() {
        throw new IllegalStateException("工具类不允许实例化");
    }

    // 匹配单行注释的正则表达式 (但不包括在字符串内的 //)
    private static final Pattern SINGLE_LINE_COMMENT = Pattern.compile(
            "//.*?(?=\\r?\\n|$)", Pattern.MULTILINE);

    // 匹配多行注释的正则表达式
    private static final Pattern MULTI_LINE_COMMENT = Pattern.compile(
            "/\\*.*?\\*/", Pattern.DOTALL);

    // 匹配尾随逗号的正则表达式
    private static final Pattern TRAILING_COMMA = Pattern.compile(
            ",\\s*([}\\]])");

    /**
     * 将 JSON5 字符串转换为标准 JSON 字符串
     *
     * @param json5Content JSON5 内容
     * @return 标准 JSON 字符串
     */
    public static String convertJson5ToJson(String json5Content) {
        if (json5Content == null || json5Content.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON5 内容不能为空");
        }

        try {
            log.debug("开始解析 JSON5 内容");

            String result = json5Content;

            // 1. 移除多行注释
            result = MULTI_LINE_COMMENT.matcher(result).replaceAll("");
            log.debug("已移除多行注释");

            // 2. 移除单行注释（更复杂的处理，避免删除字符串中的 //）
            result = removeSingleLineComments(result);
            log.debug("已移除单行注释");

            // 3. 移除尾随逗号
            result = TRAILING_COMMA.matcher(result).replaceAll("$1");
            log.debug("已移除尾随逗号");

            // 4. 验证处理后的 JSON 是否有效
            validateJson(result);

            log.debug("JSON5 解析完成");
            return result;

        } catch (Exception e) {
            log.error("解析 JSON5 失败: {}", e.getMessage());
            throw new RuntimeException("解析 JSON5 失败: " + e.getMessage(), e);
        }
    }

    /**
     * 智能移除单行注释，避免删除字符串内的 //
     */
    private static String removeSingleLineComments(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\\r?\\n");

        for (String line : lines) {
            String processedLine = removeSingleLineCommentFromLine(line);
            result.append(processedLine).append("\n");
        }

        return result.toString();
    }

    /**
     * 从单行中移除注释，保留字符串内的 //
     */
    private static String removeSingleLineCommentFromLine(String line) {
        if (line.trim().isEmpty()) {
            return line;
        }

        boolean inString = false;
        boolean escaped = false;
        char stringDelimiter = 0;

        for (int i = 0; i < line.length() - 1; i++) {
            char current = line.charAt(i);
            char next = line.charAt(i + 1);

            // 处理转义字符
            if (escaped) {
                escaped = false;
                continue;
            }

            if (current == '\\') {
                escaped = true;
                continue;
            }

            // 处理字符串边界
            if (!inString && (current == '"' || current == '\'')) {
                inString = true;
                stringDelimiter = current;
                continue;
            }

            if (inString && current == stringDelimiter) {
                inString = false;
                stringDelimiter = 0;
                continue;
            }

            // 查找注释
            if (!inString && current == '/' && next == '/') {
                // 找到注释，返回注释前的部分
                return line.substring(0, i).trim();
            }
        }

        return line;
    }

    /**
     * 验证 JSON 格式是否正确
     */
    private static void validateJson(String jsonContent) {
        try {
            String trimmed = jsonContent.trim();
            if (trimmed.startsWith("[")) {
                new JSONArray(jsonContent);
            } else if (trimmed.startsWith("{")) {
                new JSONObject(jsonContent);
            } else {
                throw new RuntimeException("无效的 JSON 格式：必须以 { 或 [ 开头");
            }
        } catch (Exception e) {
            throw new RuntimeException("JSON 格式验证失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查字符串是否为 JSON5 格式
     * （包含注释或其他 JSON5 特性）
     */
    public static boolean isJson5(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        // 检查是否包含 JSON5 特性
        return content.contains("//") ||
                content.contains("/*") ||
                TRAILING_COMMA.matcher(content).find();
    }

    /**
     * 美化 JSON5 输出
     */
    public static String prettifyJson5(String json5Content) {
        try {
            String jsonContent = convertJson5ToJson(json5Content);

            // 使用 JSONObject 或 JSONArray 重新格式化
            String trimmed = jsonContent.trim();
            if (trimmed.startsWith("[")) {
                JSONArray array = new JSONArray(jsonContent);
                return array.toString(2); // 缩进2个空格
            } else {
                JSONObject object = new JSONObject(jsonContent);
                return object.toString(2); // 缩进2个空格
            }
        } catch (Exception e) {
            log.error("美化 JSON5 失败: {}", e.getMessage());
            return json5Content; // 如果失败，返回原内容
        }
    }
}
