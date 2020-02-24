package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class RepOrderInfoProcessorTest {

    @Autowired
    private RepOrderInfoProcessor repOrderInfoProcessor;
    @Autowired
    private RepOrderCPDataRepository repOrderDataRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenRepOrderRecordIsCreated() {
        // given
        CreateLinkDto saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        repOrderDataRepository.save(testEntityDataBuilder.getRepOrderEntity());
        // when
        repOrderInfoProcessor.process(saveAndLinkModel);
        Optional<RepOrderCPDataEntity> foundOptionalRepOrder = repOrderDataRepository.findByrepOrderId(caseDetails.getMaatId());
        RepOrderCPDataEntity found = foundOptionalRepOrder.orElse(null);


        // then
        assert found != null;
        assertThat(found.getCaseUrn()).isEqualTo(caseDetails.getCaseUrn());
        assertThat(found.getRepOrderId()).isEqualTo(caseDetails.getMaatId());
        assertThat(found.getDefendantId()).isEqualTo(caseDetails.getDefendant().getDefendantId());


    }
}
