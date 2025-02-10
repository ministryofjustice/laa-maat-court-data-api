package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HRProgressDTO extends GenericDTO {

    private Long id;
    private HRProgressActionDTO progressAction;
    private HRProgressResponseDTO progressResponse;
    private Date dateRequested;
    private Date dateRequired;
    private Date dateCompleted;

}
