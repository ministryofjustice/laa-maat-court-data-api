package gov.uk.courtdata.helper;

import gov.uk.courtdata.enums.HardshipReviewDetailType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Converter(autoApply = true)
public class HardshipReviewDetailTypeConverter implements AttributeConverter<HardshipReviewDetailType, String> {

    @Override
    public String convertToDatabaseColumn(final HardshipReviewDetailType attribute) {
        return Optional.ofNullable(attribute).map(HardshipReviewDetailType::getCode).orElse(null);
    }

    @Override
    public HardshipReviewDetailType convertToEntityAttribute(final String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }
        return Stream.of(HardshipReviewDetailType.values())
                .filter(f -> f.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}