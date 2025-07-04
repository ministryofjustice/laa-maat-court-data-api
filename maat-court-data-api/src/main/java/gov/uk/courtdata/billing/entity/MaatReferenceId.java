package gov.uk.courtdata.billing.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaatReferenceId implements Serializable {
    private Integer maatId;
    private Integer applicantId;
    private Integer applicantHistoryId;
}
