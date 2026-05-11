package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinkRegisterProcessorTest {

    @InjectMocks
    private LinkRegisterProcessor linkRegisterProcessor;

    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Captor
    ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenCaseProcessor_whenProcessIsInvoke_thenSaveLinkRegister() {

        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTOForCCOutcome();

        when(wqLinkRegisterRepository.findBymaatId(any()))
                .thenReturn(Arrays.asList(
                        WqLinkRegisterEntity.builder().maatId(11211).build()));

        linkRegisterProcessor.process(hearingDTO);
        verify(wqLinkRegisterRepository).save(wqLinkRegisterEntityArgumentCaptor.capture());
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getProsecutionConcluded())
                .isEqualTo("true");
    }

    @Test
    void givenCaseProcessor_whenMaatIdInvalid_thenThrowException() {
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTOForCCOutcome();
        assertThatThrownBy(() -> linkRegisterProcessor.process(hearingDTO)).isInstanceOf(MAATCourtDataException.class);
        verify(wqLinkRegisterRepository, never()).save(any());
    }
}
