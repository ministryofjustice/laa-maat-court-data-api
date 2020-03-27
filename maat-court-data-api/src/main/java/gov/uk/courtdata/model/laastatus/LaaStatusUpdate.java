package gov.uk.courtdata.model.laastatus;

import lombok.*;

import java.util.List;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LaaStatusUpdate {

    private Integer maatId;
    private Defendant defendant;
    private List<Offence> offences;
    private Solicitor solicitor;


}
