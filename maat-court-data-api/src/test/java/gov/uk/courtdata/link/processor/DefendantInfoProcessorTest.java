package gov.uk.courtdata.link.processor;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.DefendantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class DefendantInfoProcessorTest {

    @Autowired
    private DefendantInfoProcessor defendantInfoProcessor;
    @Autowired
    private DefendantRepository defendantRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenDefendantRecordIsCreated() {
        // given
        SaveAndLinkModel saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();

        // when
        defendantInfoProcessor.process(saveAndLinkModel);
        Optional<DefendantEntity> foundDefendantOptional = defendantRepository.findById(saveAndLinkModel.getTxId());
        DefendantEntity foundDefendant = foundDefendantOptional.orElse(null);

        // then
        assert foundDefendant != null;
        assertThat(foundDefendant.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(foundDefendant.getTxId()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(foundDefendant.getDateOfBirth()).isEqualTo(caseDetails.getDefendant().getDateOfBirth());
        assertThat(foundDefendant.getDatasource()).isEqualTo(CREATE_LINK);
        assertThat(foundDefendant.getSearchType()).isEqualTo(SEARCH_TYPE_0);

    }
}
