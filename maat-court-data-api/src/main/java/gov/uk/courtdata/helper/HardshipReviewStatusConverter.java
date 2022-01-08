package gov.uk.courtdata.helper;

import gov.uk.courtdata.enums.HardshipReviewStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Converter(autoApply = true)
public class HardshipReviewStatusConverter implements AttributeConverter<HardshipReviewStatus, String> {

    @Override
    public String convertToDatabaseColumn(final HardshipReviewStatus attribute) {
        return Optional.ofNullable(attribute).map(HardshipReviewStatus::getStatus).orElse(null);
    }

    @Override
    public HardshipReviewStatus convertToEntityAttribute(final String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }
        return Stream.of(HardshipReviewStatus.values())
                .filter(f -> f.getStatus().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}