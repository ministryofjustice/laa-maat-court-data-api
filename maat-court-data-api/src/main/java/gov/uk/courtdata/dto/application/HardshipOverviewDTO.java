package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HardshipOverviewDTO extends GenericDTO {
    @Builder.Default
    private ApplicationHardshipReviewDTO magCourtHardship = new ApplicationHardshipReviewDTO();
    @Builder.Default
    private ApplicationHardshipReviewDTO crownCourtHardship = new ApplicationHardshipReviewDTO();
}