package gov.uk.courtdata.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefendantValidatorTest {
    @Mock
    private DefendantMAATDataRepository maatDataRepository;

    @InjectMocks
    private DefendantValidator defendantValidator;

    @Test
    void testWhenDefendantDetailsExists_returnsEntity() {
        Integer testId = 1000;
        Mockito.when(maatDataRepository.findBymaatId(testId))
                .thenReturn(Optional.of(
                        DefendantMAATDataEntity.builder().maatId(testId).build()));
        Optional<DefendantMAATDataEntity> defendantEntity = defendantValidator.validate(testId);
        assertThat(defendantEntity).isPresent();
        assertThat(defendantEntity.get().getMaatId()).isEqualTo(testId);
    }

    @Test
    void testWhenDefendantDetailsNotFound_throwsException() {
        assertThatThrownBy(() -> defendantValidator.validate(Mockito.anyInt())).isInstanceOf(ValidationException.class);
    }
}
