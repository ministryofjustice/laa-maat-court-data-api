package gov.uk.courtdata.authorization.service;

import gov.uk.courtdata.entity.RoleActionsEntity;
import gov.uk.courtdata.repository.RoleActionsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private RoleActionsRepository roleActionsRepository;

    @Test
    public void givenInvalidAction_whenIsRoleActionAuthorizedIsInvoked_thenResultIsFalse() {
        when(roleActionsRepository.getRoleAction(any(), any())).thenReturn(
                Optional.empty()
        );
        assertThat(authorizationService.isRoleActionAuthorized("test-f", "FAKE_ACTION")).isEqualTo(Boolean.FALSE);
    }

    @Test
    public void givenValidAction_whenIsRoleActionAuthorizedIsInvoked_thenResultIsTrue() {
        when(roleActionsRepository.getRoleAction(any(), any())).thenReturn(
                Optional.of(RoleActionsEntity.builder().action("CREATE_ASSESSMENT").build())
        );
        assertThat(authorizationService.isRoleActionAuthorized("test-f", "CREATE_ASSESSMENT")).isEqualTo(Boolean.TRUE);
    }
}
