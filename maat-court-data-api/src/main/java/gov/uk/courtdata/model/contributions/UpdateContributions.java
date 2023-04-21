package gov.uk.courtdata.model.contributions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateContributions extends Contributions {
    private Integer id;

    private String userModified;
}
