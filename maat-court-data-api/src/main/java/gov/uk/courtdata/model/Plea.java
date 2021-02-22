package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Plea {
    private String pleaValue;
    private String pleaDate;
    private String offenceId;
}