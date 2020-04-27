package gov.uk.courtdata.hearing.dto;


import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HearingDTO {

    private Integer caseId;
    private Integer txId;
    private Integer proceedingId;
    private CaseDetails caseDetails;
}
