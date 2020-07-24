package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtProcessHelperBenchWarrantTest {

    @InjectMocks
    private CrownCourtProcessHelper crownCourtProcessHelper;

    @Mock
    private XLATResultRepository xlatResultRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenHearingResulted_whenCrownCourtProcessorIsInvoked_thenProcessThenSetFlagToY() {

        //given
        List<XLATResultEntity> resultEntityList = Arrays.asList(
                XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(3030).build(),
                XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(3032).build(),
                XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(3033).build()
        );

        //when
        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(resultEntityList);

        String status = crownCourtProcessHelper.isBenchWarrantIssued(getHearingResulted());

        //then
        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertThat(status).isEqualTo("Y");
    }

    @Test
    public void givenHearingResulted_whenResultCodeIsNull_thenSetFlagToN() {
        //when
        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(null);

        String status =crownCourtProcessHelper.isBenchWarrantIssued(getHearingResulted());

        //then
        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertThat(status).isEqualTo("N");
    }

    @Test
    public void givenHearingResulted_whenResultCodeIsEmpty_thenSetFlagToN() {
        //when
        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(Arrays.asList(XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(30).build()));

        String status =crownCourtProcessHelper.isBenchWarrantIssued(getHearingResulted());

        //then
        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertThat(status).isEqualTo("N");
    }

    public HearingResulted getHearingResulted() {

        CCOutComeData ccOutComeData = CCOutComeData.builder().build();
        Session session = Session.builder().courtLocation("OU").build();
        Offence offence = Offence.builder()
                .results(Arrays.asList(Result.builder().resultCode("3030").build()))
                .build();
        Defendant defendant = Defendant.builder()
                .offences(Arrays.asList(offence))
                .build();

        return HearingResulted.builder()
                .session(session)
                .ccOutComeData(ccOutComeData)
                .defendant(defendant)
                .build();
    }
}
