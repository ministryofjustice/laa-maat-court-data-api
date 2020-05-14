package gov.uk.courtdata.model.laastatus;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Defendant {

    private String uuid;
    private String foreName;
    private String surName;


}
