package gov.uk.courtdata.link;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.link.impl.SaveAndLinkImpl;
import gov.uk.courtdata.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class SaveAndLinkImplTest {

    @Autowired
    private SaveAndLinkImpl saveAndLinkImp;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private SolicitorRepository solicitorRepository;
    @Autowired
    private ProceedingRepository proceedingRepository;
    @Autowired
    private DefendantRepository defendantRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private OffenceRepository offenceRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private RepOrderCPDataRepository repOrderDataRepository;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;


    @Test
    public void givenSaveAndLinkModel_whenSaveAndImplIsInvoked_thenLinkEstablished() {

        //given
        CourtDataDTO saveAndLinkModel = testModelDataBuilder.getSaveAndLinkModelRaw();
         repOrderDataRepository.save(testEntityDataBuilder.getRepOrderEntity());

        //when
        saveAndLinkImp.execute(saveAndLinkModel);

        //then
        verifyCase(saveAndLinkModel);
        verifyWqCore(saveAndLinkModel);
        verifyWqLinkRegister(saveAndLinkModel);
        verifySolicitor(saveAndLinkModel);
        verifyProceeding(saveAndLinkModel);
        verifyDefendant(saveAndLinkModel);
        verifySession(saveAndLinkModel);
        verifyOffence(saveAndLinkModel);
        verifyResult(saveAndLinkModel);
        verifyRepOrder(saveAndLinkModel);

    }

    private void verifyRepOrder(CourtDataDTO CourtDataDTO) {
        // Verify Rep Order Record is created
        Optional<RepOrderCPDataEntity> retrievedRepOrderEntity = repOrderDataRepository.findByrepOrderId(CourtDataDTO.getCaseDetails().getMaatId());
        RepOrderCPDataEntity repOrderEntity = retrievedRepOrderEntity.orElse(null);
        assert repOrderEntity != null;
    }

    private void verifyResult(CourtDataDTO CourtDataDTO) {
        // Verify Result Record is created
        Optional<ResultEntity> retrievedResultEntity = resultRepository.findById(CourtDataDTO.getTxId());
        ResultEntity resultEntity = retrievedResultEntity.orElse(null);
        assert resultEntity != null;
    }

    private void verifyOffence(CourtDataDTO CourtDataDTO) {
        // Verify Offence Record is created
        Optional<OffenceEntity> retrievedOffenceEntity = offenceRepository.findById(CourtDataDTO.getTxId());
        OffenceEntity offenceEntity = retrievedOffenceEntity.orElse(null);
        assert offenceEntity != null;
    }

    private void verifySession(CourtDataDTO CourtDataDTO) {
        // Verify Session Record is created
        Optional<SessionEntity> retrievedSessionEntity = sessionRepository.findById(CourtDataDTO.getTxId());
        SessionEntity sessionEntity = retrievedSessionEntity.orElse(null);
        assert sessionEntity != null;
    }

    private void verifyDefendant(CourtDataDTO CourtDataDTO) {
        // Verify Defendant Record is created
        Optional<DefendantEntity> retrievedDefendantEntity = defendantRepository.findById(CourtDataDTO.getTxId());
        DefendantEntity defendantEntity = retrievedDefendantEntity.orElse(null);
        assert defendantEntity != null;
    }

    private void verifyProceeding(CourtDataDTO CourtDataDTO) {
        // Verify Proceeding Record is created
        Optional<ProceedingEntity> retrievedProceedingEntity = proceedingRepository.findById(CourtDataDTO.getTxId());
        ProceedingEntity proceedingEntity = retrievedProceedingEntity.orElse(null);
        assert proceedingEntity != null;
    }

    private void verifySolicitor(CourtDataDTO CourtDataDTO) {
        // Verify WQCore Link register Record is created
        Optional<SolicitorEntity> retrievedSolicitorEntity = solicitorRepository.findById(CourtDataDTO.getTxId());
        SolicitorEntity solicitorEntity = retrievedSolicitorEntity.orElse(null);
        assert solicitorEntity != null;
    }

    private void verifyWqLinkRegister(CourtDataDTO CourtDataDTO) {
        // Verify WQCore Link register Record is created
        Optional<WqLinkRegisterEntity> retrievedWqLinkRegisterEntity = wqLinkRegisterRepository.findById(CourtDataDTO.getTxId());
        WqLinkRegisterEntity wqLinkRegisterEntity = retrievedWqLinkRegisterEntity.orElse(null);
        assert wqLinkRegisterEntity != null;
    }

    private void verifyWqCore(CourtDataDTO CourtDataDTO) {
        // Verify WQCore Record is created
        Optional<WqCoreEntity> retrievedWqCoreEntity = wqCoreRepository.findById(CourtDataDTO.getTxId());
        WqCoreEntity wqCoreEntity = retrievedWqCoreEntity.orElse(null);
        assert wqCoreEntity != null;
    }

    private void verifyCase(CourtDataDTO CourtDataDTO) {
        // Verify Case record is created
        Optional<CaseEntity> retrievedCaseEntity = caseRepository.findById(CourtDataDTO.getTxId());
        CaseEntity caseEntity = retrievedCaseEntity.orElse(null);
        assert caseEntity != null;
    }


}

