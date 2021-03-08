package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.entity.CrownCourtOutComeEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.CrownCourtOutcomeRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtOutComesValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private CrownCourtOutComesValidator crownCourtOutComesValidator;

    @Mock
    private CrownCourtOutcomeRepository crownCourtOutcomeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCrownCourtOutComes_whenInvalidTypeOutComeIsPassedIn() {

        //given
        List<CrownCourtOutComeEntity> crownCourtOutComeEntities = new ArrayList<>();
        CrownCourtOutComeEntity crownCourtOutComeEntity = CrownCourtOutComeEntity.builder().outcome("CONVICTED").build();
        crownCourtOutComeEntities.add(crownCourtOutComeEntity);
        //given
        CCOutComeData ccOutComeData = CCOutComeData.builder().ccOutcome("ABC").appealType("XYZ").build();
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .build();


        //then
        when(crownCourtOutcomeRepository.findAll()).thenReturn(crownCourtOutComeEntities);
        thrown.expect(MAATCourtDataException.class);
        thrown.expectMessage("Invalid Crown Court Outcome : ABC is passed in for MAAT ID: 12345");
        crownCourtOutComesValidator.validate(hearingDetails);

    }

}
