package cn.treedeep.king.shared.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.extern.slf4j.Slf4j;

/**
 * 提供日期时间格式化、解析和时间戳转换的工具方法
 */
@Slf4j
public class DateTimeUtil {

    /**
     * 默认的日期时间格式化器，格式为 "yyyy-MM-dd HH:mm:ss"。
     */
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 带有时区偏移量的日期时间格式化器，格式为 "yyyy-MM-dd HH:mm:ss XXX"。
     */
    public static final DateTimeFormatter OFFSET_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX");

    /**
     * 服务器默认时区。
     */
    public static final ZoneId SERVER_ZONE_ID = ZoneId.systemDefault();

    /**
     * 将 {@link OffsetDateTime} 转换为字符串，使用默认的带有时区偏移量的格式。
     *
     * @param offsetDateTime 需要格式化的 {@link OffsetDateTime} 对象
     * @return 格式化后的字符串，如果输入为 null 则返回空字符串
     */
    public static String format(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            log.error("Attempted to format a null OffsetDateTime.", new NullPointerException());
            return "";
        }
        return format(offsetDateTime, OFFSET_DATE_TIME_FORMATTER);
    }

    /**
     * 将 {@link OffsetDateTime} 转换为字符串，使用指定的格式。
     *
     * @param offsetDateTime 需要格式化的 {@link OffsetDateTime} 对象
     * @param formatter      指定的格式化器
     * @return 格式化后的字符串，如果输入为 null 则返回空字符串
     */
    public static String format(OffsetDateTime offsetDateTime, DateTimeFormatter formatter) {
        if (offsetDateTime == null || formatter == null) {
            log.error("Attempted to format a null OffsetDateTime or with a null formatter.", new NullPointerException());
            return "";
        }
        return format(offsetDateTime, formatter, null);
    }

    /**
     * 将 {@link OffsetDateTime} 转换为字符串，使用指定的格式。
     *
     * @param offsetDateTime 需要格式化的 {@link OffsetDateTime} 对象
     * @param formatter      指定的格式化器
     * @param zoneId         指定的时区，为 null 时使用服务器默认时区
     * @return 格式化后的字符串，如果输入为 null 则返回空字符串
     */
    public static String format(OffsetDateTime offsetDateTime, DateTimeFormatter formatter, ZoneId zoneId) {
        if (offsetDateTime == null || formatter == null) {
            log.error("Attempted to format a null OffsetDateTime or with a null formatter.", new NullPointerException());
            return "";
        }
        if (zoneId == null) {
            zoneId = SERVER_ZONE_ID;
        }
        // 先转换为指定时区的 ZonedDateTime，然后格式化
        ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(zoneId);

        // 使用指定的格式化器格式化 ZonedDateTime
        return zonedDateTime.format(formatter);
    }


    /**
     * 将 {@link LocalDateTime} 转换为字符串，使用默认的格式。
     *
     * @param localDateTime 需要格式化的 {@link LocalDateTime} 对象
     * @return 格式化后的字符串，如果输入为 null 则返回空字符串
     */
    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            log.error("Attempted to format a null LocalDateTime.", new NullPointerException());
            return "";
        }
        return localDateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 将 {@link LocalDateTime} 转换为字符串，使用指定的格式。
     *
     * @param localDateTime 需要格式化的 {@link LocalDateTime} 对象
     * @param formatter     指定的格式化器
     * @return 格式化后的字符串，如果输入为 null 则返回空字符串
     */
    public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        if (localDateTime == null || formatter == null) {
            log.error("Attempted to format a null LocalDateTime or with a null formatter.", new NullPointerException());
            return "";
        }
        return localDateTime.format(formatter);
    }

    /**
     * 将字符串转换为 {@link OffsetDateTime}，使用默认的带有时区偏移量的格式。
     *
     * @param dateTimeString 需要解析的日期时间字符串
     * @return 解析后的 {@link OffsetDateTime} 对象，如果输入为 null 则返回 null
     * @throws DateTimeParseException 如果字符串格式不正确
     */
    public static OffsetDateTime parse(String dateTimeString) throws DateTimeParseException {
        if (dateTimeString == null) {
            log.error("Attempted to parse a null string to OffsetDateTime.", new NullPointerException());
            return null;
        }
        return OffsetDateTime.parse(dateTimeString, OFFSET_DATE_TIME_FORMATTER);
    }

    /**
     * 将字符串转换为 {@link OffsetDateTime}，使用指定的格式。
     *
     * @param dateTimeString 需要解析的日期时间字符串
     * @param formatter      指定的格式化器
     * @return 解析后的 {@link OffsetDateTime} 对象，如果输入为 null 则返回 null
     * @throws DateTimeParseException 如果字符串格式不正确
     */
    public static OffsetDateTime parse(String dateTimeString, DateTimeFormatter formatter) throws DateTimeParseException {
        if (dateTimeString == null || formatter == null) {
            log.error("Attempted to parse a null string to OffsetDateTime or with a null formatter.", new NullPointerException());
            return null;
        }
        return OffsetDateTime.parse(dateTimeString, formatter);
    }

    /**
     * 将字符串转换为 {@link LocalDateTime}，使用默认的格式。
     *
     * @param dateTimeString 需要解析的日期时间字符串
     * @return 解析后的 {@link LocalDateTime} 对象，如果输入为 null 则返回 null
     * @throws DateTimeParseException 如果字符串格式不正确
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeString) throws DateTimeParseException {
        if (dateTimeString == null) {
            log.error("Attempted to parse a null string to LocalDateTime.", new NullPointerException());
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DEFAULT_FORMATTER);
    }

    /**
     * 将字符串转换为 {@link LocalDateTime}，使用指定的格式。
     *
     * @param dateTimeString 需要解析的日期时间字符串
     * @param formatter      指定的格式化器
     * @return 解析后的 {@link LocalDateTime} 对象，如果输入为 null 则返回 null
     * @throws DateTimeParseException 如果字符串格式不正确
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeString, DateTimeFormatter formatter) throws DateTimeParseException {
        if (dateTimeString == null || formatter == null) {
            log.error("Attempted to parse a null string to LocalDateTime or with a null formatter.", new NullPointerException());
            return null;
        }
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * 将 {@link OffsetDateTime} 转换为时间戳（毫秒）。
     *
     * @param offsetDateTime 需要转换的 {@link OffsetDateTime} 对象
     * @return 时间戳（毫秒），如果输入为 null 则返回 0
     */
    public static long toTimestamp(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            log.error("Attempted to convert a null OffsetDateTime to timestamp.", new NullPointerException());
            return 0L;
        }
        return offsetDateTime.toInstant().toEpochMilli();
    }

    /**
     * 将 {@link LocalDateTime} 转换为时间戳（毫秒），使用服务器默认时区。
     *
     * @param localDateTime 需要转换的 {@link LocalDateTime} 对象
     * @return 时间戳（毫秒），如果输入为 null 则返回 0
     */
    public static long toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            log.error("Attempted to convert a null LocalDateTime to timestamp.", new NullPointerException());
            return 0L;
        }
        return localDateTime.atZone(SERVER_ZONE_ID).toInstant().toEpochMilli();
    }

    /**
     * 将时间戳（毫秒）转换为 {@link OffsetDateTime}，使用UTC时区。
     *
     * @param timestamp 时间戳（毫秒）
     * @return 转换后的 {@link OffsetDateTime} 对象，如果输入为 0 则返回 null
     */
    public static OffsetDateTime fromTimestamp(long timestamp) {
        if (timestamp == 0L) {
            log.error("Attempted to convert a zero timestamp to OffsetDateTime.", new NullPointerException());
            return null;
        }
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
    }

    /**
     * 将时间戳（毫秒）转换为 {@link OffsetDateTime}，使用指定的时区偏移量。
     *
     * @param timestamp 时间戳（毫秒）
     * @param zoneOffset 指定的时区偏移量
     * @return 转换后的 {@link OffsetDateTime} 对象，如果输入为 0 则返回 null
     */
    public static OffsetDateTime fromTimestamp(long timestamp, ZoneOffset zoneOffset) {
        if (timestamp == 0L) {
            log.error("Attempted to convert a zero timestamp to OffsetDateTime.", new NullPointerException());
            return null;
        }
        if (zoneOffset == null) {
            zoneOffset = ZoneOffset.UTC;
        }
        return Instant.ofEpochMilli(timestamp).atOffset(zoneOffset);
    }

    /**
     * 将时间戳（毫秒）转换为 {@link OffsetDateTime}，使用指定的时区。
     *
     * @param timestamp 时间戳（毫秒）
     * @param zoneId 指定的时区，为 null 时使用 UTC
     * @return 转换后的 {@link OffsetDateTime} 对象，如果输入为 0 则返回 null
     */
    public static OffsetDateTime fromTimestamp(long timestamp, ZoneId zoneId) {
        if (timestamp == 0L) {
            log.error("Attempted to convert a zero timestamp to OffsetDateTime.", new NullPointerException());
            return null;
        }
        if (zoneId == null) {
            zoneId = ZoneOffset.UTC;
        }
        return Instant.ofEpochMilli(timestamp).atZone(zoneId).toOffsetDateTime();
    }

    /**
     * 将时间戳（毫秒）转换为 {@link LocalDateTime}，使用服务器默认时区。
     *
     * @param timestamp 时间戳（毫秒）
     * @return 转换后的 {@link LocalDateTime} 对象，如果输入为 0 则返回 null
     */
    public static LocalDateTime fromTimestamp2LocalDateTime(long timestamp) {
        if (timestamp == 0L) {
            log.error("Attempted to convert a zero timestamp to LocalDateTime.", new NullPointerException());
            return null;
        }
        return Instant.ofEpochMilli(timestamp).atZone(SERVER_ZONE_ID).toLocalDateTime();
    }

    /**
     * 将 {@link LocalDateTime} 转换为 {@link OffsetDateTime}，使用指定的时区。
     *
     * @param localDateTime 需要转换的 {@link LocalDateTime} 对象
     * @param zoneId 指定的时区，为 null 时使用服务器默认时区
     * @return 转换后的 {@link OffsetDateTime} 对象，如果输入为 null 则返回 null
     */
    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null) {
            log.error("Attempted to convert a null LocalDateTime to OffsetDateTime.", new NullPointerException());
            return null;
        }
        if (zoneId == null) {
            zoneId = SERVER_ZONE_ID;
        }
        return localDateTime.atZone(zoneId).toOffsetDateTime();
    }

    /**
     * 将 {@link LocalDateTime} 转换为 {@link OffsetDateTime}，使用服务器默认时区。
     *
     * @param localDateTime 需要转换的 {@link LocalDateTime} 对象
     * @return 转换后的 {@link OffsetDateTime} 对象，如果输入为 null 则返回 null
     */
    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return toOffsetDateTime(localDateTime, SERVER_ZONE_ID);
    }

    /**
     * 将 {@link OffsetDateTime} 从一个时区转换到另一个时区。
     *
     * @param offsetDateTime 需要转换的 {@link OffsetDateTime} 对象
     * @param targetZoneId 目标时区
     * @return 转换后的 {@link OffsetDateTime} 对象，如果输入为 null 则返回 null
     */
    public static OffsetDateTime convertToZone(OffsetDateTime offsetDateTime, ZoneId targetZoneId) {
        if (offsetDateTime == null) {
            log.error("Attempted to convert a null OffsetDateTime to another zone.", new NullPointerException());
            return null;
        }
        if (targetZoneId == null) {
            log.error("Target zone cannot be null.", new IllegalArgumentException());
            return offsetDateTime;
        }
        return offsetDateTime.atZoneSameInstant(targetZoneId).toOffsetDateTime();
    }

    /**
     * 将 {@link OffsetDateTime} 转换为服务器默认时区的 {@link LocalDateTime}。
     *
     * @param offsetDateTime 需要转换的 {@link OffsetDateTime} 对象
     * @return 转换后的 {@link LocalDateTime} 对象，如果输入为 null 则返回 null
     */
    public static LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            log.error("Attempted to convert a null OffsetDateTime to LocalDateTime.", new NullPointerException());
            return null;
        }
        return offsetDateTime.atZoneSameInstant(SERVER_ZONE_ID).toLocalDateTime();
    }

    /**
     * 将 {@link OffsetDateTime} 转换为指定时区的 {@link LocalDateTime}。
     *
     * @param offsetDateTime 需要转换的 {@link OffsetDateTime} 对象
     * @param zoneId 目标时区，为 null 时使用服务器默认时区
     * @return 转换后的 {@link LocalDateTime} 对象，如果输入为 null 则返回 null
     */
    public static LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime, ZoneId zoneId) {
        if (offsetDateTime == null) {
            log.error("Attempted to convert a null OffsetDateTime to LocalDateTime.", new NullPointerException());
            return null;
        }
        if (zoneId == null) {
            zoneId = SERVER_ZONE_ID;
        }
        return offsetDateTime.atZoneSameInstant(zoneId).toLocalDateTime();
    }

    /**
     * 获取指定时区当前时间的 {@link OffsetDateTime}。
     *
     * @param zoneId 指定的时区，为 null 时使用服务器默认时区
     * @return 当前时间的 {@link OffsetDateTime} 对象
     */
    public static OffsetDateTime nowInZone(ZoneId zoneId) {
        if (zoneId == null) {
            zoneId = SERVER_ZONE_ID;
        }
        return OffsetDateTime.now(zoneId);
    }

    /**
     * 获取服务器默认时区当前时间的 {@link OffsetDateTime}。
     *
     * @return 当前时间的 {@link OffsetDateTime} 对象
     */
    public static OffsetDateTime now() {
        return OffsetDateTime.now(SERVER_ZONE_ID);
    }
}
