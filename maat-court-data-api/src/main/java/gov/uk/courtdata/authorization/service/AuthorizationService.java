package gov.uk.courtdata.authorization.service;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RoleActionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {
    private final RoleActionsRepository roleActionsRepository;

    public boolean isRoleActionAuthorized(String username, String action) {
        if (isAnyBlank(username, action)) {
            throw new ValidationException("Username and action are required");
        }
        return roleActionsRepository.getRoleAction(username, action.toUpperCase()).isPresent();
    }
}
