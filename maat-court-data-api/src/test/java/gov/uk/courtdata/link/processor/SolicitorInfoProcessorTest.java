package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SolicitorEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.repository.SolicitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SolicitorInfoProcessorTest {

    @InjectMocks
    private SolicitorInfoProcessor solicitorInfoProcessor;
    @Spy
    private SolicitorRepository solicitorRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<SolicitorEntity> solicitorCaptor;

    @BeforeEach
    public void setUp() {
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenSolicitorRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        SolicitorMAATDataEntity solicitorMAATDataEntity = courtDataDTO.getSolicitorMAATDataEntity();

        // when
        solicitorInfoProcessor.process(courtDataDTO);

        // then
        verify(solicitorRepository).save(solicitorCaptor.capture());
        assertThat(solicitorCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(solicitorCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(solicitorCaptor.getValue().getLaaOfficeAccount()).isEqualTo(solicitorMAATDataEntity.getAccountCode());
        assertThat(solicitorCaptor.getValue().getFirmName()).isEqualTo(solicitorMAATDataEntity.getAccountName());


    }
}
