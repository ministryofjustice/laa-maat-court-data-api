package gov.uk.courtdata.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseDetails {

    private String caseUrn;
    private Integer maatId;
    private String asn;
    private String docLanguage;
    private String caseCreationDate;
    private String cjsAreaCode;
    private String createdUser;
    private Defendant defendant;
    private RepOrder repOrder;
    private List<Session> sessionlist;
    private Solicitor solicitor;

}
