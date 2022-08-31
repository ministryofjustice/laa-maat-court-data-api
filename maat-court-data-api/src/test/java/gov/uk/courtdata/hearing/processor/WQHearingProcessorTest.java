package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WQHearingRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WQHearingProcessorTest {

    @InjectMocks
    private WQHearingProcessor wqHearingProcessor;

    @Spy
    private WQHearingRepository wqHearingRepository;

    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Spy
    private IdentifierRepository identifierRepository;

    @Captor
    private ArgumentCaptor<WQHearingEntity> wqHearingEntityArgumentCaptor;

    @Test
    public void givenWQHearingProcessor_whenProcessIsInvoke_thenSaveWQHearingEntity() {

        when(wqLinkRegisterRepository.findBymaatId(anyInt()))
                .thenReturn(Collections.singletonList(WqLinkRegisterEntity.builder()
                        .maatId(121112)
                        .build()));

        wqHearingProcessor.process(getHearingResulted());

        verify(wqLinkRegisterRepository).findBymaatId(anyInt());
        verify(identifierRepository).getTxnID();

        verify(wqHearingRepository).save(wqHearingEntityArgumentCaptor.capture());
        assertThat(wqHearingEntityArgumentCaptor.getValue().getHearingUUID()).isEqualTo("9dc76cb3-a996-4660-8386-bab551007ac7");
        assertThat(wqHearingEntityArgumentCaptor.getValue().getMaatId()).isEqualTo(TestModelDataBuilder.REP_ID);
        assertThat(wqHearingEntityArgumentCaptor.getValue().getWqJurisdictionType()).isEqualTo(JurisdictionType.CROWN.name());
        assertThat(wqHearingEntityArgumentCaptor.getValue().getOuCourtLocation()).isEqualTo("1212");
        assertThat(wqHearingEntityArgumentCaptor.getValue().getResultCodes()).isEqualTo("3030,5031,4032");
    }

    private HearingResulted getHearingResulted() {

        return HearingResulted.builder()
                .maatId(TestModelDataBuilder.REP_ID)

                .caseUrn("1211")
                .hearingId(UUID.fromString("9dc76cb3-a996-4660-8386-bab551007ac7"))
                .jurisdictionType(JurisdictionType.CROWN)
                .session(Session.builder().courtLocation("1212").build())
                .defendant(
                        Defendant.builder()
                                .offences(
                                        Arrays.asList(
                                                Offence.builder().results(Collections.singletonList(Result.builder()
                                                        .resultCode("3030").build())).build(),
                                                Offence.builder().results(Collections.singletonList(Result.builder()
                                                        .resultCode("5031").build())).build(),
                                                Offence.builder().results(Collections.singletonList(Result.builder()
                                                        .resultCode("4032").build())).build()
                                        )
                                ).build())
                .build();
    }
}