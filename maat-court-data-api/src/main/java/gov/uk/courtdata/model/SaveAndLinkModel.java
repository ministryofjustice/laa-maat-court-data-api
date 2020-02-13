package gov.uk.courtdata.model;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAndLinkModel {

    private CaseDetails caseDetails;
    private SolicitorMAATDataEntity solicitorMAATDataEntity;
    private DefendantMAATDataEntity defendantMAATDataEntity;
}
