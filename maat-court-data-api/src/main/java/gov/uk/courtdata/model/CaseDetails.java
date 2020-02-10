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
    private String asn;
    private String docLanguage;
    private String caseCreationDate;
    private String cjsAreaCode;
    private Number proceedingId;
    private List<Defendant> defendantList;
    private RepOrder repOrder;
    private Session session;
    private Solicitor solicitor;
    private Result result;
    private Proceeding proceeding;
}
