package cn.treedeep.king.${moduleNameLower}.domain;


import cn.treedeep.king.core.domain.ValueObjectBase;
import cn.treedeep.king.shared.utils.JsonUtil;
import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Copyright © ${copyright} 版权所有
 * <p>
 * Description「值对象」
 * <p>
 * Power by AggregateX
 *
 * @author ${author}
 * @since ${dateTime}
 */
@AllArgsConstructor
@NoArgsConstructor
@ValueObject
@Getter
public class Description extends ValueObjectBase {

    private String id;
    private String value;

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{id, value};
    }

    /**
     * 定义JSON转换器
     * <p>
     * 多属性的一般序列化为 JSON 存储在单个列中
     */
    public static class DescriptionJsonConverter implements AttributeConverter<Description, String> {
        @Override
        public String convertToDatabaseColumn(Description desc) {
            return JsonUtil.toJson(desc);
        }

        @Override
        public Description convertToEntityAttribute(String json) {
            return JsonUtil.fromJson(json, Description.class);
        }
    }
}
