package gov.uk.courtdata.authorization.service;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.AuthorizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {
    private final AuthorizationRepository authorizationRepository;

    public boolean isRoleActionAuthorized(String username, String action) {
        List<String> validActions = authorizationRepository.getAvailableActions();
        if (!validActions.contains(action)) {
            throw new ValidationException("The specified action does not exist");
        }
        List<String> authorizedActions = authorizationRepository.getAuthorizedActions(username);
        if (authorizedActions.isEmpty()) {
            throw new ValidationException("User does not exist or has invalid role");
        }
        return authorizedActions.contains(action);
    }
}
