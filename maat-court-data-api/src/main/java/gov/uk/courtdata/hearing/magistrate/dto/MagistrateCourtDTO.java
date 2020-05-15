package gov.uk.courtdata.hearing.magistrate.dto;

import lombok.*;


@Data
@Builder
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
    private String inActive;
    private DefendantDTO defendant;
    private OffenceDTO offence;
    private ResultDTO result;
    private SessionDTO session;

}
