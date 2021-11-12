package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.entity.AppealTypeEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.AppealTypeRepository;
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
public class AppealTypeValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private AppealTypeValidator appealTypeValidator;

    @Mock
    private AppealTypeRepository appealTypeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAppealTypeCodes_whenInvalidTypeCodeIsPassedIn() {

        //given
        List<AppealTypeEntity> appealTypeEntityList = new ArrayList<>();
        AppealTypeEntity appealTypeEntity1 = AppealTypeEntity.builder().code("ACV").build();
        appealTypeEntityList.add(appealTypeEntity1);
        //given
        CCOutComeData ccOutComeData = CCOutComeData.builder().ccooOutcome("CONVICTED").appealType("XYZ").build();
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .build();


        //then
        when(appealTypeRepository.findAll()).thenReturn(appealTypeEntityList);
        thrown.expect(MAATCourtDataException.class);
        thrown.expectMessage("Invalid Appeal Type : XYZ is passed in for MAAT ID: 12345");
        appealTypeValidator.validate(hearingDetails);

    }

}
