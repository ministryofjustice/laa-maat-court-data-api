package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.SUCCESSFUL;
import static gov.uk.courtdata.enums.CrownCourtCaseType.APPEAL_CC;
import static gov.uk.courtdata.enums.CrownCourtCaseType.CC_ALREADY;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.CONVICTED;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeValidatorTest {

    @Mock
    private RepOrderRepository repOrderRepository;

    @InjectMocks
    private CaseTypeValidator caseTypeValidator;


    @Test(expected = ValidationException.class)
    public void givenCaseTypeNotValidForAppeal_RaisesException() {

        when(repOrderRepository.findById(1001)).thenReturn(repOrderWith(CC_ALREADY.getValue()));
        caseTypeValidator.validate(hearingResultedWith(SUCCESSFUL.getValue()));

    }

    @Test(expected = ValidationException.class)
    public void givenCaseTypeNotValidForTrial_RaisesException() {

        when(repOrderRepository.findById(1001)).thenReturn(repOrderWith(APPEAL_CC.getValue()));
        caseTypeValidator.validate(hearingResultedWith(CONVICTED.getValue()));

    }


    @Test
    public void givenCaseTypeValidForTrial_ValidationSuccess() {


        caseTypeValidator.validate(hearingResultedWith(CONVICTED.getValue()));

    }


    private HearingResulted hearingResultedWith(String outcome) {

        CCOutComeData ccOutComeData = CCOutComeData.builder()
                .ccooOutcome(outcome)
                .build();
        Session session = Session.builder().courtLocation("OU").build();
        Offence offence = Offence.builder()
                .results(Collections.singletonList(Result.builder().resultCode("3030").build()))
                .build();
        Defendant defendant = Defendant.builder()
                .offences(Collections.singletonList(offence))
                .build();

        return HearingResulted.builder()
                .maatId(1001)
                .session(session)
                .ccOutComeData(ccOutComeData)
                .defendant(defendant)
                .build();
    }

    public Optional<RepOrderEntity> repOrderWith(String caseType) {
        return Optional.of(RepOrderEntity.builder()
                .id(1001)
                .dateModified(LocalDateTime.now())
                .catyCaseType(caseType)
                .build());
    }

}
