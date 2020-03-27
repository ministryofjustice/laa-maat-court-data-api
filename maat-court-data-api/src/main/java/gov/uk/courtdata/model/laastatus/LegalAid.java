package gov.uk.courtdata.model.laastatus;

import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LegalAid {

    private String status;
    private String statusDate;
    private String startDate;
    private String endDate;

}
