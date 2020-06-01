package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MaatIdValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private MaatIdValidator maatIdValidator;

    @Mock
    private RepOrderRepository repOrderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenMaatIdIsNull_throwsException() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("MAAT id is missing.");
        maatIdValidator.validate(null);
    }

    @Test
    public void testWhenMaatIdIsMissingFromPayload_throwsException() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("MAAT id is missing.");
        maatIdValidator.validate(0);
    }

    @Test
    public void testWhenMaatIsNotNullButNotOnRepOrder_validationPasses() {
        when(repOrderRepository.findById(1000)).thenReturn(Optional.empty());
        thrown.expect(ValidationException.class);
        thrown.expectMessage("1000 is Not a Valid MAAT ID");
        maatIdValidator.validate(1000);
    }

    @Test
    public void testWhenMaatIsNotNullButExistOnRepOrder_validationPasses() {
        final RepOrderEntity repOrderEntity = RepOrderEntity.builder().id(1000).build();
        when(repOrderRepository.findById(1000)).thenReturn(Optional.of(repOrderEntity));

        Optional<Void> result = maatIdValidator.validate(1000);
        assertThat(result).isEqualTo(Optional.empty());

    }
}
