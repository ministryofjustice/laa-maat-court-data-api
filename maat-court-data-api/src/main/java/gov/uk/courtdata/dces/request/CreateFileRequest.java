package gov.uk.courtdata.dces.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFileRequest {

    private Integer recordsSent;
    @ToString.Exclude
    private String xmlContent;
    private String xmlFileName;
    @ToString.Exclude
    private String ackXmlContent;
}
