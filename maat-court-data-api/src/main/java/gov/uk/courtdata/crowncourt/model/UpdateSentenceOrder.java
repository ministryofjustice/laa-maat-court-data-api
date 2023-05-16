package gov.uk.courtdata.crowncourt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSentenceOrder {
    private Integer repId;
    private String dbUser;
    private LocalDate sentenceOrderDate;
    private LocalDate dateChanged;
}
