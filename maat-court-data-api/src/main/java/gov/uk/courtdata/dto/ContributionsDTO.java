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
    private Integer contributionsFileId;
    private LocalDate effectiveDate;
    private LocalDate calcDate;
    private Integer monthlyContributions;
    private Integer upfrontContributions;
    private String transferStatus;
}
