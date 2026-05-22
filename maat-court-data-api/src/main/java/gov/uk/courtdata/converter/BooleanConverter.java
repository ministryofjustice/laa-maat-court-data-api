package gov.uk.courtdata.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return YesNoConvertor.toString(attribute);
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return YesNoConvertor.toBoolean(dbData).orElse(null);
    }
}
