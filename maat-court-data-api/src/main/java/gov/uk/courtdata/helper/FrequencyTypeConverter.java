package gov.uk.courtdata.helper;

import gov.uk.courtdata.enums.Frequency;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Component
@Converter(autoApply = true)
public class FrequencyTypeConverter implements AttributeConverter<Frequency, String> {

    @Override
    public String convertToDatabaseColumn(final Frequency attribute) {
        return Optional.ofNullable(attribute).map(Frequency::getCode).orElse(null);
    }

    @Override
    public Frequency convertToEntityAttribute(final String dbData) {
        return Frequency.fromCode(dbData);
    }
}