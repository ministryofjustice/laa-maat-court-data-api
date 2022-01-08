package gov.uk.courtdata.helper;

import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Converter(autoApply = true)
public class HardshipReviewDetailCodeConverter implements AttributeConverter<HardshipReviewDetailCode, String> {

    @Override
    public String convertToDatabaseColumn(final HardshipReviewDetailCode attribute) {
        return Optional.ofNullable(attribute).map(HardshipReviewDetailCode::getCode).orElse(null);
    }

    @Override
    public HardshipReviewDetailCode convertToEntityAttribute(final String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }
        return Stream.of(HardshipReviewDetailCode.values())
                .filter(f -> f.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}