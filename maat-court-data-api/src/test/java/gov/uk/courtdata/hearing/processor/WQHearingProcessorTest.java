package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WQHearingProcessorTest {

    @InjectMocks
    private WQHearingProcessor wqHearingProcessor;

    @Spy
    private WQHearingRepository wqHearingRepository;

    @Captor
    private ArgumentCaptor<WQHearingEntity> wqHearingEntityArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenWQHearingProcessor_whenProcessIsInvoke_thenSaveWQHearingEntity() {

        wqHearingProcessor.process(getHearingResulted());

        verify(wqHearingRepository).save(wqHearingEntityArgumentCaptor.capture());
        assertThat(wqHearingEntityArgumentCaptor.getValue().getHearingUUID()).isEqualTo("9dc76cb3-a996-4660-8386-bab551007ac7");
        assertThat(wqHearingEntityArgumentCaptor.getValue().getMaatId()).isEqualTo(TestModelDataBuilder.MAAT_ID);
        assertThat(wqHearingEntityArgumentCaptor.getValue().getWqJurisdictionType()).isEqualTo(JurisdictionType.CROWN.name());
        assertThat(wqHearingEntityArgumentCaptor.getValue().getOuCourtLocation()).isEqualTo("1212");
        assertThat(wqHearingEntityArgumentCaptor.getValue().getResultCodes()).isEqualTo("3030,5031,4032");
    }

    private HearingResulted getHearingResulted() {

        return HearingResulted.builder()
                .maatId(9988)
                .caseUrn("1211")
                .hearingId(UUID.fromString("9dc76cb3-a996-4660-8386-bab551007ac7"))
                .jurisdictionType(JurisdictionType.CROWN)
                .session(Session.builder().courtLocation("1212").build())
                .defendant(
                        Defendant.builder()
                                .offences(
                                        Arrays.asList(
                                                Offence.builder().results(Collections.singletonList(Result.builder().resultCode("3030").build())).build(),
                                                Offence.builder().results(Collections.singletonList(Result.builder().resultCode("5031").build())).build(),
                                                Offence.builder().results(Collections.singletonList(Result.builder().resultCode("4032").build())).build()
                                        )
                                ).build())
                .build();
    }
}