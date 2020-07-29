package gov.uk.courtdata.model.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProceedingMaatId implements Serializable {

    private Integer maatId;
    private Integer proceedingId;
}
