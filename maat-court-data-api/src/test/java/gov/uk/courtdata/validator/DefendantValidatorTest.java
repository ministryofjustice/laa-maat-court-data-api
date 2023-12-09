package gov.uk.courtdata.validator;


import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DefendantValidatorTest {
    @Mock
    private DefendantMAATDataRepository maatDataRepository;

    @InjectMocks
    private DefendantValidator defendantValidator;

    @Test
    public void testWhenDefendantDetailsExists_returnsEntity() {
        Integer testId = 1000;
        Mockito.when(maatDataRepository.findBymaatId(testId))
                .thenReturn(Optional.of(DefendantMAATDataEntity.builder().maatId(testId).build()));
        Optional<DefendantMAATDataEntity> defendantEntity =
                defendantValidator.validate(testId);
        assertTrue(defendantEntity.isPresent());
        assertEquals(testId, defendantEntity.get().getMaatId());
    }

    @Test
    public void testWhenDefendantDetailsNotFound_throwsException() {
        Assertions.assertThrows(ValidationException.class, () ->
                defendantValidator.validate(Mockito.anyInt()));
    }
}
