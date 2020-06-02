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
    private CCOutComeData ccOutComeData;
    private Defendant defendant;
    private String inActive;
    private Session session;
    private UUID laaTransactionId;
}
