package gov.uk.courtdata.model.laastatus;

import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaaStatusUpdate {

    private Integer maatId;
    private Defendant defendant;
    private List<Offence> offences;
    private Solicitor solicitor;


}
