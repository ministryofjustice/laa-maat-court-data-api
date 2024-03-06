package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppealDTO extends GenericDTO {
    private Boolean available;
    private Date appealReceivedDate;
    private Date appealSentenceOrderDate;
    private Date appealSentOrderDateSet;

    @Builder.Default
    private AppealTypeDTO appealTypeDTO = new AppealTypeDTO();

}
