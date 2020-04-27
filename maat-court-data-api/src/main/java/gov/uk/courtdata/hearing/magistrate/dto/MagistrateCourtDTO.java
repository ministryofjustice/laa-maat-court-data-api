package gov.uk.courtdata.hearing.magistrate.dto;

import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MagistrateCourtDTO {

    private String caseUrn;
    private Integer maatId;
    private Integer caseId;
    private Integer txId;
    private Integer proceedingId;
    private String asn;
    private String docLanguage;
    private String caseCreationDate;
    private String cjsAreaCode;
    private String cjsLocation;
    private String createdUser;
    private DefendantDTO defendant;
    private OffenceDTO offence;
    private ResultDTO result;
    private SessionDTO session;
    private String isActive;

}
