package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.ProceedingEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.ProceedingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class ProceedingsInfoProcessorTest {

    @Autowired
    private ProceedingsInfoProcessor proceedingsInfoProcessor;
    @Autowired
    private ProceedingRepository proceedingRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenProceedingRecordIsCreated() {
        // given
        CreateLinkDto saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();

        // when
        proceedingsInfoProcessor.process(saveAndLinkModel);
        Optional<ProceedingEntity> foundOptionalProceeding = proceedingRepository.findById(saveAndLinkModel.getTxId());
        ProceedingEntity foundProceeding = foundOptionalProceeding.orElse(null);

        // then
        assert foundProceeding != null;
        assertThat(foundProceeding.getCreatedTxid()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(foundProceeding.getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(foundProceeding.getProceedingId()).isEqualTo(saveAndLinkModel.getProceedingId());
        assertThat(foundProceeding.getCreatedUser()).isEqualTo(caseDetails.getCreatedUser());

    }
}
