package gov.uk.courtdata.hearing.model;

import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Session;
import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HearingResulted  {

    private String caseUrn;
    private Integer maatId;
    private String asn;
    private String docLanguage;
    private String caseCreationDate;
    private String cjsAreaCode;
    private String cjsLocation;
    private String createdUser; // Not required. Could be System or system-maat-api
    private Defendant defendant;
    private String isActive;
    private Session session;
    private Integer category;
}
