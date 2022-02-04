package gov.uk.courtdata.prosecutionconcluded.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerdictType {
    private UUID verdictTypeId;
    private Integer sequence;
    private String description;
    private String category;
    private String categoryType;
}
