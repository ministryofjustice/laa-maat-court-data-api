package gov.uk.courtdata.billing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaatReferenceId implements Serializable {
    private Integer maatId;
    private Integer applicantId;
    private Integer applicantHistoryId;
}
