package gov.uk.courtdata.validator;


import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DefendantValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private DefendantMAATDataRepository maatDataRepository;

    @InjectMocks
    private DefendantValidator defendantValidator;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenDefendantDetailsExists_returnsEntity() {

        Integer testId = 1000;
        Mockito.when(maatDataRepository.findBymaatId(testId))
                .thenReturn(Optional.of(DefendantMAATDataEntity.builder().maatId(testId).build()));
        Optional<DefendantMAATDataEntity> defendantEntity =
                defendantValidator.validate(testId);

        Assert.assertTrue(defendantEntity.isPresent());
        Assert.assertEquals(testId, defendantEntity.get().getMaatId());

    }

    @Test
    public void testWhenDefendantDetailsNotFound_throwsException() {

        thrown.expect(ValidationException.class);
        thrown.expectMessage("MAAT Defendant details not found.");
        defendantValidator.validate(Mockito.anyInt());
    }

    @After
    public void tearDown() {

    }
}
