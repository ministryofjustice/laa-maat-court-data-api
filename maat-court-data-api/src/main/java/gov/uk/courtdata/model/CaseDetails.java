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

    private Integer caseId;
    private Integer txId;
    private Integer maatId;
    private String asn;
    private String docLanguage;
    private String caseCreationDate;
    private String cjsAreaCode;
    private Integer proceedingId;
    private Defendant defendant;
    private RepOrder repOrder;
    private List<Session> sessionlist;
    private Solicitor solicitor;
    private Proceeding proceeding;
}
