package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.COMMON_PLATFORM;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class WqLinkRegisterProcessorTest {

    @Autowired
    private WqLinkRegisterProcessor wqLinkRegisterProcessor;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenWQCoreRecordIsCreated() {

        // given
        CreateLinkDto saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        SolicitorMAATDataEntity solicitorMAATDataEntity = saveAndLinkModel.getSolicitorMAATDataEntity();

        // when
        wqLinkRegisterProcessor.process(saveAndLinkModel);
        Optional<WqLinkRegisterEntity> foundOptionalWqLinkRegister = wqLinkRegisterRepository.findById(saveAndLinkModel.getTxId());
        WqLinkRegisterEntity found = foundOptionalWqLinkRegister.orElse(null);


        // then
        assert found != null;
        assertThat(found.getCreatedTxId()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(found.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(found.getMaatCat()).isEqualTo(solicitorMAATDataEntity.getCmuId());
        assertThat(found.getMlrCat()).isEqualTo(solicitorMAATDataEntity.getCmuId());
        assertThat(found.getLibraId()).isEqualTo(COMMON_PLATFORM + saveAndLinkModel.getLibraId());
        assertThat(found.getMaatId()).isEqualTo(caseDetails.getMaatId());
    }
}
