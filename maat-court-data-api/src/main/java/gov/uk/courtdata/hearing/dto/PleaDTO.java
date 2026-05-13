package gov.uk.courtdata.hearing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PleaDTO {
    private String pleaValue;
    private String pleaDate;
    private String offenceId;
}
