package gov.uk.courtdata.hearing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PleaDTO {
    private String pleaValue;
    private Date pleaDate;
    private String offenceId;
}