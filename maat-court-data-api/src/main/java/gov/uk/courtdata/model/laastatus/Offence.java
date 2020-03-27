package gov.uk.courtdata.model.laastatus;

import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offence {

    private String uuid;
    private String offenceCode;
    private String offenceShortTitle;
    private String offenceClassification;
    private String offenceDate;
    private Integer modeOfTrial;
    private LegalAid legalAid;
}
