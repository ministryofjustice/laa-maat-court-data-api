package gov.uk.courtdata.model.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseTxnId implements Serializable {

    private Integer txId;
    private Integer caseId;
}
