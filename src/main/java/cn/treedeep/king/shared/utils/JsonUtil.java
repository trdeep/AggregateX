package cn.treedeep.king.shared.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * 提供基于Jackson库的JSON工具方法。
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper objectMapper = SpringBeanUtil.getBean("defaultObjectMapper", ObjectMapper.class);

    /**
     * 将对象转换为JSON字符串。
     *
     * @param object 需要转换为JSON字符串的对象
     * @return 对象的JSON字符串表示，如果转换失败则返回错误信息
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON string", e);
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * 将JSON字符串转换为Map。
     *
     * @param json 需要转换的JSON字符串
     * @return 转换后的Map对象，如果转换失败则返回空Map
     */
    public static Map<String, Object> jsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            log.error("Failed to convert JSON string to Map", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 将Map转换为JSON字符串。
     *
     * @param map 需要转换为JSON字符串的Map
     * @return Map的JSON字符串表示，如果转换失败则返回错误信息
     */
    public static String mapToJson(Map<String, ?> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert map to JSON string", e);
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象。
     *
     * @param <T>   目标对象的类型
     * @param json  需要转换的JSON字符串
     * @param clazz 目标对象的类
     * @return 转换后的对象，如果转换失败则返回null
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJsonOpt(json, clazz).orElse(null);
    }

    public static <T> Optional<T> fromJsonOpt(String json, Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (IOException e) {
            log.error("Failed to convert JSON string to object", e);
            return Optional.empty();
        }
    }

    /**
     * 将JSON字符串转换为特定类型的对象。
     *
     * @param <T>           目标对象的类型
     * @param json          需要转换的JSON字符串
     * @param typeReference 目标对象的类型引用
     * @return 转换后的对象，如果转换失败则返回null
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        return fromJsonOpt(json, typeReference).orElse(null);
    }

    public static <T> Optional<T> fromJsonOpt(String json, TypeReference<T> typeReference) {
        try {
            return Optional.of(objectMapper.readValue(json, typeReference));
        } catch (IOException e) {
            log.error("Failed to convert JSON string to object", e);
            return Optional.empty();
        }
    }
}
