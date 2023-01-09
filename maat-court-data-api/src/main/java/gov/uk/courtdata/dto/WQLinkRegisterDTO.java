package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WQLinkRegisterDTO {

    private Integer createdTxId;
    private Integer caseId;
    private LocalDateTime createdDate;
    private String createdUserId;
    private Integer removedTxId;
    private LocalDateTime removedDate;
    private String removedUserId;
    private String libraId;
    private Integer maatId;
    private String cjsAreaCode;
    private String cjsLocation;
    private Integer maatCat;
    private Integer proceedingId;
    private Integer mlrCat;
    private String caseOwnerId;
    private String caseUrn;
    private String prosecutionConcluded;
}
