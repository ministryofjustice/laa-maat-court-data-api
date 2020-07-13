package gov.uk.courtdata.integration.link.service;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CourtHouseCodesEntity;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.link.service.CreateLinkListener;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class CreateLinkListenerIntegrationTest {


    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private RepOrderCPDataRepository repOrderDataRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;
    @Autowired
    private CourtHouseCodesRepository courtHouseCodesRepository;
    @Autowired
    private CreateLinkListener createLinkListener;
    @Autowired
    private SolicitorMAATDataRepository solicitorMAATDataRepository;
    @Autowired
    private DefendantMAATDataRepository defendantMAATDataRepository;

    @Before
    public void setUp() {
        repOrderRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        repOrderDataRepository.deleteAll();
        solicitorMAATDataRepository.deleteAll();
        defendantMAATDataRepository.deleteAll();
        courtHouseCodesRepository.deleteAll();
    }

    @Test
    public void givenSaveAndLinkModel_whenSaveAndImplIsInvoked_thenLinkEstablished() {

        //given
        repOrderDataRepository.save(testEntityDataBuilder.getRepOrderEntity());
        repOrderRepository.save(testEntityDataBuilder.getRepOrder());
        courtHouseCodesRepository.save(CourtHouseCodesEntity.builder().code("B16BG").effectiveDateFrom(LocalDateTime.now()).build());
        solicitorMAATDataRepository.save(testEntityDataBuilder.getSolicitorMAATDataEntity());
        defendantMAATDataRepository.save(testEntityDataBuilder.getDefendantMAATDataEntity());

        String saveAndLinkMessage = testModelDataBuilder.getSaveAndLinkString();

        //when
        createLinkListener.receive(saveAndLinkMessage);

        //then
        CourtDataDTO courtDataDTO = testModelDataBuilder.getSaveAndLinkModelRaw();

        verifyWqLinkRegister(courtDataDTO);
        verifyRepOrder(courtDataDTO);

    }

    private void verifyRepOrder(CourtDataDTO courtDataDTO) {
        // Verify CP Rep Order Record is created
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Optional<RepOrderCPDataEntity> retrievedRepOrderEntity = repOrderDataRepository.findByrepOrderId(caseDetails.getMaatId());
        RepOrderCPDataEntity found = retrievedRepOrderEntity.orElse(null);
        assert found != null;
        assertThat(found.getCaseUrn()).isEqualTo(caseDetails.getCaseUrn());
        assertThat(found.getRepOrderId()).isEqualTo(caseDetails.getMaatId());
        assertThat(found.getDefendantId()).isEqualTo(caseDetails.getDefendant().getDefendantId());
    }


    private void verifyWqLinkRegister(CourtDataDTO courtDataDTO) {
        // Verify WQCore Link register Record is created
        List<WqLinkRegisterEntity> retrievedWqLinkRegisterEntity = wqLinkRegisterRepository.findBymaatId(courtDataDTO.getCaseDetails().getMaatId());
        WqLinkRegisterEntity wqLinkRegisterEntity = retrievedWqLinkRegisterEntity.get(0);
        assert wqLinkRegisterEntity != null;
        assertThat(wqLinkRegisterEntity.getMaatId()).isEqualTo(courtDataDTO.getCaseDetails().getMaatId());
    }


}

