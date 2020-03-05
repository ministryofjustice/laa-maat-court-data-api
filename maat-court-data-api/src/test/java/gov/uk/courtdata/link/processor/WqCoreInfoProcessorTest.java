package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqCoreRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_CREATION_EVENT;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_SUCCESS_STATUS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class WqCoreInfoProcessorTest {

    @Autowired
    private WqCoreInfoProcessor wqCoreInfoProcessor;
    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenWQCoreRecordIsCreated() {

        // given
        CourtDataDTO saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();

        // when
        wqCoreInfoProcessor.process(saveAndLinkModel);
        Optional<WqCoreEntity> foundOptionalWqCore = wqCoreRepository.findById(saveAndLinkModel.getTxId());
        WqCoreEntity found = foundOptionalWqCore.orElse(null);


        // then
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(found.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(found.getWqStatus()).isEqualTo(WQ_SUCCESS_STATUS);
        assertThat(found.getWqType()).isEqualTo(WQ_CREATION_EVENT);
    }
}
