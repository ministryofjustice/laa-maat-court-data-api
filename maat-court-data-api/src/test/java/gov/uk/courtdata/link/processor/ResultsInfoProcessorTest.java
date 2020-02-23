package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.ResultEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.ResultRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class ResultsInfoProcessorTest {


    @Autowired
    private ResultsInfoProcessor resultsInfoProcessor;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;


    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenResultRecordIsCreated() {

        // given
        SaveAndLinkModel saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        Result result = caseDetails.getDefendant().getOffences().get(0).getResults().get(0);
        // when
        resultsInfoProcessor.process(saveAndLinkModel);
        Optional<ResultEntity> foundOptionalResult = resultRepository.findById(saveAndLinkModel.getTxId());
        ResultEntity found = foundOptionalResult.orElse(null);

        // then
        // then
        assert found != null;
        assertThat(found.getResultCode()).isEqualTo(result.getResultCode());
        assertThat(found.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(found.getResultShortTitle()).isEqualTo(result.getResultShortTitle());
        assertThat(found.getWqResult()).isEqualTo(G_NO);
        ;


    }
}
