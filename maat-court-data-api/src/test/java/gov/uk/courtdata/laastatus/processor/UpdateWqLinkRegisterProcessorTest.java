package gov.uk.courtdata.laastatus.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateWqLinkRegisterProcessorTest {

    @InjectMocks
    private UpdateWqLinkRegisterProcessor wqLinkRegisterProcessor;
    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterCaptor;

    @BeforeEach
    public void setUp() {
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenWQLinkRegisterModel_whenProcessIsInvoked_thenWQLinkRecordIsUpdated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = new ArrayList<>();
        wqLinkRegisterEntityList.add(WqLinkRegisterEntity.builder().maatId(1234).mlrCat(10).build());

        when(wqLinkRegisterRepository.findBymaatId(1234)).thenReturn(wqLinkRegisterEntityList);


        // when
        wqLinkRegisterProcessor.process(courtDataDTO);

        // then
        verify(wqLinkRegisterRepository).save(wqLinkRegisterCaptor.capture());
        assertThat(wqLinkRegisterCaptor.getValue().getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(wqLinkRegisterCaptor.getValue().getMlrCat()).isEqualTo(caseDetails.getCategory());
    }
}
