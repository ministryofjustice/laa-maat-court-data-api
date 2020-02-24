package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.SessionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class SessionInfoProcessorTest {

    @Autowired
    private SessionInfoProcessor sessionInfoProcessor;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenSessionRecordIsCreated() {

        // given
        CreateLinkDto saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();

        // when
        sessionInfoProcessor.process(saveAndLinkModel);
        Optional<SessionEntity> foundOptionalSession = sessionRepository.findById(saveAndLinkModel.getTxId());
        SessionEntity found = foundOptionalSession.orElse(null);


        // then
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(found.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(found.getPostHearingCustody()).isEqualTo(caseDetails.getSessions().get(0).getPostHearingCustody());


    }
}
