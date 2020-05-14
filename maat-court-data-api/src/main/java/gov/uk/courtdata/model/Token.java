package gov.uk.courtdata.model;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
public class Token {

    private String access_token;
    private String token_type;
    private Integer expires_in;
    private Integer created_at;
}
