package gov.uk.courtdata.model.laastatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoLaaStatusUpdate {

    private String legalAidStatus;
    private LocalDate legalAidStatusDate;
    private Integer iojDecision;
    private Integer mlrCat;
    private String cjsAreaCode;
}
