package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.CaseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class CaseInfoProcessorTest {

    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private CaseInfoProcessor caseInfoProcessor;
    @Autowired
    private CaseRepository caseRepository;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_theCaseRecordIsCreated() {
        // given
        SaveAndLinkModel saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();

        //when
        caseInfoProcessor.process(saveAndLinkModel);
        Optional<CaseEntity> foundCaseOptionalEntity = caseRepository.findById(saveAndLinkModel.getTxId());
        CaseEntity foundCase = foundCaseOptionalEntity.orElse(null);

        // then
        assert foundCase != null;
        assertThat(foundCase.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(foundCase.getTxId()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(foundCase.getAsn()).isEqualTo(caseDetails.getAsn());
        assertThat(foundCase.getDocLanguage()).isEqualTo(caseDetails.getDocLanguage());
        assertThat(foundCase.getInactive()).isEqualTo(NO);
    }

}
