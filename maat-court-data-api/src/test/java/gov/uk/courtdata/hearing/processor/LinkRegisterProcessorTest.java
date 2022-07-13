package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkRegisterProcessorTest {

    @InjectMocks
    private LinkRegisterProcessor linkRegisterProcessor;

    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterEntityArgumentCaptor;


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenCaseProcessor_whenProcessIsInvoke_thenSaveLinkRegister() {

        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTOForCCOutcome();

        when(wqLinkRegisterRepository.findBymaatId(any()))
                .thenReturn(Arrays
                        .asList(WqLinkRegisterEntity.
                                builder()
                                .maatId(11211)
                                .build()
                        ));

        linkRegisterProcessor.process(hearingDTO);
        verify(wqLinkRegisterRepository).save(wqLinkRegisterEntityArgumentCaptor.capture());
        assertThat(wqLinkRegisterEntityArgumentCaptor.getValue().getProsecutionConcluded()).isEqualTo("true");
    }

    @Test
    public void givenCaseProcessor_whenMaatIdInvalid_thenThrowException() {
        Assertions.assertThrows(MAATCourtDataException.class, () -> {
            HearingDTO hearingDTO = testModelDataBuilder.getHearingDTOForCCOutcome();

            //when
            linkRegisterProcessor.process(hearingDTO);
            verify(wqLinkRegisterRepository).save(wqLinkRegisterEntityArgumentCaptor.capture());
        });
    }
}