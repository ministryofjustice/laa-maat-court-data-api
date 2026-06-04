package gov.uk.courtdata.mapper;

import org.hibernate.type.YesNoConverter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YesNoConvertor {

    default String booleanToYesNo(Boolean value) {
        Character converted = YesNoConverter.INSTANCE.toRelationalValue(value);
        return converted == null ? null : converted.toString();
    }

    default Boolean yesNoToBoolean(String value) {
        return YesNoConverter.INSTANCE.toDomainValue(value.charAt(0));
    }
}
