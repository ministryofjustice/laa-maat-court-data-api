package gov.uk.courtdata.helper;

import gov.uk.courtdata.enums.HardshipReviewProgressResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Converter(autoApply = true)
public class HardshipReviewProgressResponseConverter implements AttributeConverter<HardshipReviewProgressResponse, String> {

    @Override
    public String convertToDatabaseColumn(final HardshipReviewProgressResponse attribute) {
        return Optional.ofNullable(attribute).map(HardshipReviewProgressResponse::getCode).orElse(null);
    }

    @Override
    public HardshipReviewProgressResponse convertToEntityAttribute(final String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }
        return Stream.of(HardshipReviewProgressResponse.values())
                .filter(f -> f.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}