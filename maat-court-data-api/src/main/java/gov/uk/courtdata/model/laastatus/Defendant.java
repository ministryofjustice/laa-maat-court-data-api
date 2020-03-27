package gov.uk.courtdata.model.laastatus;

import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Defendant {

    private String uuid;
    private String foreName;
    private String surName;


}
