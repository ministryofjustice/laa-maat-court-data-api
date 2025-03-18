package gov.uk.courtdata.helper;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.util.ReflectionUtils;

public class ReflectionHelper {
    private ReflectionHelper() {}

    public static <T> void updateEntityFromMap(T entity, Map<String, Object> updateFields) {
        updateFields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(entity.getClass(), key);
            ReflectionUtils.makeAccessible(field);

            if (field.getType().equals(LocalDateTime.class)) {
                value = LocalDateTime.parse(String.valueOf(value));
            }

            ReflectionUtils.setField(field, entity, value);
        });
    }

    public static <T> void updateEntityFromObject(T entityToUpdate, T object) {
        for (Field declaredField : object.getClass().getDeclaredFields()) {
            ReflectionUtils.makeAccessible(declaredField);
            Object fieldValue = ReflectionUtils.getField(declaredField, object);

            if (fieldValue != null) {
                ReflectionUtils.setField(declaredField, entityToUpdate, fieldValue);
            }
        }
    }
}
