package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributionsDTO {
    private Integer id;
    private Integer contFileId;
    private LocalDate effectiveDate;
    private LocalDate calcDate;
    private Integer monthlyContribs;
    private Integer upfrontContribs;
    private String transfreStatus;
}
