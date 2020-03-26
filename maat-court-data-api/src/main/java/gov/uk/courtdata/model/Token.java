package gov.uk.courtdata.model;

import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String access_token;
    private String token_type;
    private Integer expires_in;
    private Integer created_at;
}
