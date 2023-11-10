package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HRDetailDTO extends GenericDTO {

    private Long id;
    private FrequenciesDTO frequency;
    private Date dateReceived;
    private HRDetailDescriptionDTO detailDescription;
    private String otherDescription;
    private Currency amountNumber;
    private Date dateDue;
    private boolean accepted;
    private HRReasonDTO reason;
    private Timestamp timeStamp;
    private String hrReasonNote;

}
