package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaatIdValidatorTest {
    @InjectMocks
    private MaatIdValidator maatIdValidator;

    @Mock
    private RepOrderRepository repOrderRepository;

    @Mock
    private RepOrderService repOrderService;

    @Test
    public void testWhenMaatIdIsNull_throwsException() {
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> maatIdValidator.validate(null));
        assertThat(validationException.getMessage()).isEqualTo("MAAT ID is required.");
    }

    @Test
    public void testWhenMaatIdIsMissingFromPayload_throwsException() {
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> maatIdValidator.validate(0));
        assertThat(validationException.getMessage()).isEqualTo("MAAT ID is required.");
    }

    @Test
    public void testWhenMaatIsNotNullButNotOnRepOrder_validationPasses() {
        when(repOrderRepository.findById(1000)).thenReturn(Optional.empty());
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> maatIdValidator.validate(1000));
        assertThat(validationException.getMessage()).isEqualTo("MAAT/REP ID: 1000 is invalid.");
    }

    @Test
    public void testWhenMaatIsNotNullButExistOnRepOrder_validationPasses() {
        final RepOrderEntity repOrderEntity = RepOrderEntity.builder().id(1000).build();
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrderEntity));
        Optional<Void> result = maatIdValidator.validate(1000);
        assertThat(result).isNotPresent();
    }

    @Test
    public void testWhenMaatIdIsMissingFromPayloadWhenValidatingMaatIdDoesntExist_throwsException() {
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> maatIdValidator.validateNotExists(0));
        assertThat(validationException.getMessage()).isEqualTo("MAAT ID is required.");
    }

    @Test
    public void testWhenMaatIsNotNullButAlreadyExistsOnRepOrderWhenValidatingThatItDoesntExist_validationFails() {
        when(repOrderService.exists(1000)).thenReturn(true);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> maatIdValidator.validateNotExists(1000));
        assertThat(validationException.getMessage()).isEqualTo("There is already a record with MAAT ID [1000].");
    }
}
