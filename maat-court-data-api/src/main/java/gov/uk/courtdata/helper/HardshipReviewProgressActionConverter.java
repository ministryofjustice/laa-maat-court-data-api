package gov.uk.courtdata.helper;

import gov.uk.courtdata.enums.HardshipReviewProgressAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Converter(autoApply = true)
public class HardshipReviewProgressActionConverter implements AttributeConverter<HardshipReviewProgressAction, String> {

    @Override
    public String convertToDatabaseColumn(final HardshipReviewProgressAction attribute) {
        return Optional.ofNullable(attribute).map(HardshipReviewProgressAction::getCode).orElse(null);
    }

    @Override
    public HardshipReviewProgressAction convertToEntityAttribute(final String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }
        return Stream.of(HardshipReviewProgressAction.values())
                .filter(f -> f.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}