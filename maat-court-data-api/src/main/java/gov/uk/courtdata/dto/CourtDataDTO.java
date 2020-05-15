package gov.uk.courtdata.dto;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtDataDTO {

    private Integer caseId;
    private Integer txId;
    private Integer proceedingId;
    private Integer libraId;
    private CaseDetails caseDetails;
    private SolicitorMAATDataEntity solicitorMAATDataEntity;
    private DefendantMAATDataEntity defendantMAATDataEntity;
}
