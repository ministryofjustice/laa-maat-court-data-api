package gov.uk.courtdata.model.hearing;

import gov.uk.courtdata.enums.JurisdictionType;
import lombok.*;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HearingResulted {

    private Integer maatId;
    private String caseUrn;
    private JurisdictionType jurisdictionType;
    private String asn;
    private String docLanguage;
    private String caseCreationDate;
    private String cjsAreaCode;
    private String ccooOutcome;
    private String crownCourtCode;
    private String benchWarrantIssuedYn;
    private String ccImprisioned;
    private String appealType;
    private Defendant defendant;
    private String inActive;
    private Session session;
    private UUID laaTransactionId;
}
