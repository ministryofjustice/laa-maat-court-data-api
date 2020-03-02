package gov.uk.courtdata.model;

import lombok.*;

import java.util.List;

@ToString
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
    private String cjsLocation;
    private String createdUser;
    private Defendant defendant;
    private boolean isActive;
    private List<Session> sessions;
    private Solicitor solicitor;
    private Integer category;

}
