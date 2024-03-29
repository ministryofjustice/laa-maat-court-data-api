package gov.uk.courtdata.authorization.service;


import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.entity.RoleActionEntity;
import gov.uk.courtdata.entity.RoleWorkReasonEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.ReservationsRepository;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private RoleActionsRepository roleActionsRepository;

    @Mock
    private RoleWorkReasonsRepository roleWorkReasonsRepository;

    @Mock
    private ReservationsRepository reservationsRepository;

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
                Optional.of(RoleActionEntity.builder().action("CREATE_ASSESSMENT").build())
        );
        assertThat(authorizationService.isRoleActionAuthorized("test-f", "CREATE_ASSESSMENT")).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void givenMissingParameters_whenIsRoleActionAuthorizedIsInvoked_thenValidationExceptionIsThrown() {
        assertThatThrownBy(
                () -> authorizationService.isRoleActionAuthorized("FAKE_USERNAME", null)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("Username and action are required");

        assertThatThrownBy(
                () -> authorizationService.isRoleActionAuthorized(null, "FAKE_ACTION")
        ).isInstanceOf(ValidationException.class).hasMessageContaining("Username and action are required");
    }

    @Test
    public void getInvalidWorkReason_whenIsNewWorkReasonAuthorizedIsInvoked_thenResultIsFalse() {
        when(roleWorkReasonsRepository.getNewWorkReason(any(), any())).thenReturn(
                Optional.empty()
        );
        assertThat(authorizationService.isNewWorkReasonAuthorized("test-f", "FAKE_WORK_REASON")).isEqualTo(Boolean.FALSE);
    }

    @Test
    public void givenValidWorkReason_whenIsNewWorkReasonAuthorizedIsInvoked_thenResultIsTrue() {
        when(roleWorkReasonsRepository.getNewWorkReason(any(), any())).thenReturn(
                Optional.of(RoleWorkReasonEntity.builder().nworCode("FMA").build())
        );
        assertThat(authorizationService.isNewWorkReasonAuthorized("test-f", "FMA")).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void givenMissingParameters_whenIsNewWorkReasonAuthorizedIsInvoked_thenValidationExceptionIsThrown() {
        assertThatThrownBy(
                () -> authorizationService.isNewWorkReasonAuthorized("FAKE_USERNAME", null)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("Username and new work reason are required");

        assertThatThrownBy(
                () -> authorizationService.isNewWorkReasonAuthorized(null, "FAKE_WORK_REASON")
        ).isInstanceOf(ValidationException.class).hasMessageContaining("Username and new work reason are required");
    }

    @Test
    public void givenIncorrectParameters_whenIsReservedIsInvoked_thenResultIsFalse() {
        when(reservationsRepository.findReservationByUserSession(any(), any(), any(), any())).thenReturn(
                Optional.empty()
        );
        assertThat(authorizationService.isReserved(TestModelDataBuilder.getUserReservation())).isEqualTo(Boolean.FALSE);
    }

    @Test
    public void givenCorrectParameters_whenIsReservedIsInvoked_thenResultIsTrue() {
        when(reservationsRepository.findReservationByUserSession(any(), any(), any(), any())).thenReturn(
                Optional.of(ReservationsEntity.builder().build())
        );
        assertThat(authorizationService.isReserved(TestModelDataBuilder.getUserReservation())).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void givenCorrectParameters_whenIsReservedIsInvoked_thenExpiryDateIsUpdated() {
        when(reservationsRepository.findReservationByUserSession(any(), any(), any(), any())).thenReturn(
                Optional.of(ReservationsEntity.builder().build())
        );
        authorizationService.isReserved(TestModelDataBuilder.getUserReservation());
        verify(reservationsRepository).updateReservationExpiryDate(any(), any(), any(), any());
    }
}
