package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepOrderStateDTO {

    private Integer usn;
    private Integer maatRef;
    private String caseId;
    private String caseType;

    private String iojResult;
    private String iojAssessorName;
    private LocalDate dateAppCreated;
    private String iojReason;

    private String meansInitResult;
    private String meansInitStatus;

    private String meansFullResult;
    private String meansFullStatus;

    private String meansAssessorName;
    private LocalDateTime dateMeansCreated;

    private String passportResult;
    private String passportStatus;
    private String passportAssessorName;
    private LocalDateTime datePassportCreated;

    private String fundingDecision;
    private String ccRepDecision;

    private String iojAppealResult;
    private String iojAppealAssessorName;
    private LocalDateTime iojAppealDate;
}
