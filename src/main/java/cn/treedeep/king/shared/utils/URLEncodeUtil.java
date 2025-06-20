package cn.treedeep.king.shared.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class URLEncodeUtil {
    private URLEncodeUtil() {
        throw new IllegalStateException("工具类不允许实例化");
    }

    public static String encodeUtf8(String value) {
        if (value == null) {
            return null;
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String decodeUtf8(String value) {
        if (value == null) {
            return null;
        }
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
