package gov.uk.courtdata.laastatus.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateWqLinkRegisterProcessorTest {

    @InjectMocks
    private UpdateWqLinkRegisterProcessor wqLinkRegisterProcessor;
    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
       ;
        assertThat(wqLinkRegisterCaptor.getValue().getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(wqLinkRegisterCaptor.getValue().getMlrCat()).isEqualTo(caseDetails.getCategory());
    }
}
