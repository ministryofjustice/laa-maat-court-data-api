package gov.uk.courtdata.model.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    private String appName;
    private String appServer;
    private String session;
    private String username;
}
