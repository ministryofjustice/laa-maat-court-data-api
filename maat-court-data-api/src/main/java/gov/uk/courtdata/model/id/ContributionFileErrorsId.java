package gov.uk.courtdata.model.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContributionFileErrorsId implements Serializable {
    private int contributionId;
    private int contributionFileId;

}
