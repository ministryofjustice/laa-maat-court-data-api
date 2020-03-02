package gov.uk.courtdata.link.processor;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.OffenceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static gov.uk.courtdata.constants.CourtDataConstants.PENDING_IOJ_DECISION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class OffenceInfoProcessorTest {

    @Autowired
    private OffenceInfoProcessor offenceInfoProcessor;
    @Autowired
    private OffenceRepository offenceRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenOffenceRecordIsCreated() {
        // given
        LaaModelManager saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        Offence offence = saveAndLinkModel.getCaseDetails().getDefendant().getOffences().get(0);

        // when
        offenceInfoProcessor.process(saveAndLinkModel);
        Optional<OffenceEntity> foundOffenceOptional = offenceRepository.findById(saveAndLinkModel.getTxId());
        OffenceEntity foundOffence = foundOffenceOptional.orElse(null);

        // then
        assert foundOffence != null;
        assertThat(foundOffence.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(foundOffence.getTxId()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(foundOffence.getAsnSeq()).isEqualTo(offence.getAsnSeq());
        assertThat(foundOffence.getOffenceWording()).isEqualTo(offence.getOffenceWording());
        assertThat(foundOffence.getIojDecision()).isEqualTo(PENDING_IOJ_DECISION);
        assertThat(foundOffence.getWqOffence()).isEqualTo(G_NO);

    }
}
