package gov.uk.courtdata.link.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.link.impl.SaveAndLinkImpl;
import gov.uk.courtdata.link.validator.ValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.processor.OffenceCodeRefDataProcessor;
import gov.uk.courtdata.processor.ResultCodeRefDataProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateLinkServiceTest {

    @InjectMocks
    private CreateLinkService createLinkService;

    @Mock
    private SaveAndLinkImpl saveAndLinkImpl;
    @Mock
    private ValidationProcessor validationProcessor;
    @Mock
    private OffenceCodeRefDataProcessor offenceCodeRefDataProcessor;
    @Mock
    private ResultCodeRefDataProcessor resultCodeRefDataProcessor;

    @Test
    public void givenJSONMessageIsReceived_whenCreateLinkServiceIsInvoked_thenSaveAndImplIsCalled() {

        //given
        Result result = Result.builder().resultCode("1234").build();
        List<Result> resultList = Collections.singletonList(result);
        Offence offence = Offence.builder().offenceCode("Code").results(resultList).build();
        List<Offence> offenceList = Collections.singletonList(offence);
        Defendant defendant = Defendant.builder().offences(offenceList).build();
        CaseDetails caseDetails = CaseDetails.builder().defendant(defendant).build();
        CourtDataDTO courtDataDTO = CourtDataDTO.builder().caseDetails(caseDetails).build();

        //when
        when(validationProcessor.validate(caseDetails)).thenReturn(courtDataDTO);
        createLinkService.saveAndLink(caseDetails);

        //then
        verify(saveAndLinkImpl, times(1)).execute(courtDataDTO);
        verify(offenceCodeRefDataProcessor, times(1)).processOffenceCode("Code");
        verify(resultCodeRefDataProcessor, times(1)).processResultCode(1234);

    }
}
