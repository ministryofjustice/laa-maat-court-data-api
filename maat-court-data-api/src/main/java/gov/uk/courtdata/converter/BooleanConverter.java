package gov.uk.courtdata.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanConverter implements AttributeConverter<Boolean, String> {

    private static final String STRING_Y = "Y";
    private static final String STRING_N = "N";

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute != null) {
            if (attribute) {
                return STRING_Y;
            } else {
                return STRING_N;
            }
        }
        return null;
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            return dbData.equals(STRING_Y);
        }
        return false;
    }

}