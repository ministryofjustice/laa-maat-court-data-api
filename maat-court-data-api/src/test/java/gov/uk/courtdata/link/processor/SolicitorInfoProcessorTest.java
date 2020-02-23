package gov.uk.courtdata.link.processor;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.SolicitorEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.SolicitorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class SolicitorInfoProcessorTest {

    @Autowired
    private SolicitorInfoProcessor solicitorInfoProcessor;
    @Autowired
    private SolicitorRepository solicitorRepository;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenProcessIsInvoked_thenSolicitorRecordIsCreated() {

        // given
        SaveAndLinkModel saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModel();
        SolicitorMAATDataEntity solicitorMAATDataEntity = saveAndLinkModel.getSolicitorMAATDataEntity();

        // when
        solicitorInfoProcessor.process(saveAndLinkModel);
        Optional<SolicitorEntity> foundOptionalSolicitor = solicitorRepository.findById(saveAndLinkModel.getTxId());
        SolicitorEntity found = foundOptionalSolicitor.orElse(null);


        // then
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(saveAndLinkModel.getTxId());
        assertThat(found.getCaseId()).isEqualTo(saveAndLinkModel.getCaseId());
        assertThat(found.getLaaOfficeAccount()).isEqualTo(solicitorMAATDataEntity.getAccountCode());
        assertThat(found.getFirmName()).isEqualTo(solicitorMAATDataEntity.getAccountName());
        ;


    }
}
