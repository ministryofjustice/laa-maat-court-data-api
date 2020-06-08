package gov.uk.courtdata.integrationTest.link.impl;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.integrationTest.MockServicesConfig;
import gov.uk.courtdata.link.impl.SaveAndLinkImpl;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class SaveAndLinkImplIntegrationTest {

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
    private RepOrderRepository repOrderRepository;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Test
    public void givenSaveAndLinkModel_whenSaveAndImplIsInvoked_thenLinkEstablished() {

        //given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getSaveAndLinkModelRaw();
        repOrderDataRepository.save(testEntityDataBuilder.getRepOrderEntity());
        repOrderRepository.save(testEntityDataBuilder.getRepOrder());

        //when
        saveAndLinkImp.execute(courtDataDTO);

        //then
        verifyCase(courtDataDTO);
        verifyWqCore(courtDataDTO);
        verifyWqLinkRegister(courtDataDTO);
        verifySolicitor(courtDataDTO);
        verifyProceeding(courtDataDTO);
        verifyDefendant(courtDataDTO);
        verifySession(courtDataDTO);
        verifyOffence(courtDataDTO);
        verifyResult(courtDataDTO);
        verifyRepOrder(courtDataDTO);

    }

    private void verifyRepOrder(CourtDataDTO courtDataDTO) {
        // Verify Rep Order Record is created
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Optional<RepOrderCPDataEntity> retrievedRepOrderEntity = repOrderDataRepository.findByrepOrderId(caseDetails.getMaatId());
        RepOrderCPDataEntity found = retrievedRepOrderEntity.orElse(null);
        assert found != null;
        assertThat(found.getCaseUrn()).isEqualTo(caseDetails.getCaseUrn());
        assertThat(found.getRepOrderId()).isEqualTo(caseDetails.getMaatId());
        assertThat(found.getDefendantId()).isEqualTo(caseDetails.getDefendant().getDefendantId());
    }

    private void verifyResult(CourtDataDTO courtDataDTO) {
        // Verify Result Record is created
        Optional<ResultEntity> retrievedResultEntity = resultRepository.findById(courtDataDTO.getTxId());
        ResultEntity found = retrievedResultEntity.orElse(null);
        assert found != null;
        final Result result = courtDataDTO.getCaseDetails().getDefendant().getOffences().get(0).getResults().get(0);
        assertThat(found.getResultCode()).isEqualTo(result.getResultCode());
        assertThat(found.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(found.getResultShortTitle()).isEqualTo(result.getResultShortTitle());
        assertThat(found.getWqResult()).isEqualTo(G_NO);
    }

    private void verifyOffence(CourtDataDTO courtDataDTO) {
        // Verify Offence Record is created
        Optional<OffenceEntity> retrievedOffenceEntity = offenceRepository.findById(courtDataDTO.getTxId());
        OffenceEntity offenceEntity = retrievedOffenceEntity.orElse(null);
        assert offenceEntity != null;
        assertThat(offenceEntity.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(offenceEntity.getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(offenceEntity.getOffenceWording()).isEqualTo(courtDataDTO.getCaseDetails().getDefendant().getOffences().get(0).getOffenceWording());
        assertThat(offenceEntity.getIojDecision()).isEqualTo(PENDING_IOJ_DECISION);
        assertThat(offenceEntity.getWqOffence()).isEqualTo(G_NO);
    }

    private void verifySession(CourtDataDTO courtDataDTO) {
        // Verify Session Record is created
        Optional<SessionEntity> retrievedSessionEntity = sessionRepository.findById(courtDataDTO.getTxId());
        SessionEntity found = retrievedSessionEntity.orElse(null);
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(found.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(found.getPostHearingCustody()).isEqualTo(courtDataDTO.getCaseDetails().getSessions().get(0).getPostHearingCustody());
    }

    private void verifyDefendant(CourtDataDTO courtDataDTO) {
        // Verify Defendant Record is created
        Optional<DefendantEntity> retrievedDefendantEntity = defendantRepository.findById(courtDataDTO.getTxId());
        DefendantEntity defendantEntity = retrievedDefendantEntity.orElse(null);
        assert defendantEntity != null;
        assertThat(defendantEntity.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(defendantEntity.getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(defendantEntity.getDateOfBirth()).isEqualTo(courtDataDTO.getCaseDetails().getDefendant().getDateOfBirth());
        assertThat(defendantEntity.getDatasource()).isEqualTo(CREATE_LINK);
        assertThat(defendantEntity.getSearchType()).isEqualTo(SEARCH_TYPE_0);
    }

    private void verifyProceeding(CourtDataDTO courtDataDTO) {
        // Verify Proceeding Record is created
        Optional<ProceedingEntity> retrievedProceedingEntity = proceedingRepository.findById(courtDataDTO.getTxId());
        ProceedingEntity foundProceeding = retrievedProceedingEntity.orElse(null);
        assert foundProceeding != null;
        assertThat(foundProceeding.getCreatedTxid()).isEqualTo(courtDataDTO.getTxId());
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        assertThat(foundProceeding.getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(foundProceeding.getProceedingId()).isEqualTo(courtDataDTO.getProceedingId());
        assertThat(foundProceeding.getCreatedUser()).isEqualTo(caseDetails.getCreatedUser());
    }

    private void verifySolicitor(CourtDataDTO courtDataDTO) {
        // Verify WQCore Link register Record is created
        Optional<SolicitorEntity> retrievedSolicitorEntity = solicitorRepository.findById(courtDataDTO.getTxId());
        SolicitorEntity found = retrievedSolicitorEntity.orElse(null);
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(found.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        final SolicitorMAATDataEntity solicitorMAATDataEntity = courtDataDTO.getSolicitorMAATDataEntity();
        assertThat(found.getLaaOfficeAccount()).isEqualTo(solicitorMAATDataEntity.getAccountCode());
        assertThat(found.getFirmName()).isEqualTo(solicitorMAATDataEntity.getAccountName());
    }

    private void verifyWqLinkRegister(CourtDataDTO courtDataDTO) {
        // Verify WQCore Link register Record is created
        Optional<WqLinkRegisterEntity> retrievedWqLinkRegisterEntity = wqLinkRegisterRepository.findById(courtDataDTO.getTxId());
        WqLinkRegisterEntity wqLinkRegisterEntity = retrievedWqLinkRegisterEntity.orElse(null);
        assert wqLinkRegisterEntity != null;
        assertThat(wqLinkRegisterEntity.getCreatedTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(wqLinkRegisterEntity.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(wqLinkRegisterEntity.getMaatCat()).isEqualTo(courtDataDTO.getSolicitorMAATDataEntity().getCmuId());
        assertThat(wqLinkRegisterEntity.getMlrCat()).isEqualTo(courtDataDTO.getSolicitorMAATDataEntity().getCmuId());
        assertThat(wqLinkRegisterEntity.getLibraId()).isEqualTo(courtDataDTO.getLibraId());
        assertThat(wqLinkRegisterEntity.getMaatId()).isEqualTo(courtDataDTO.getCaseDetails().getMaatId());
    }

    private void verifyWqCore(CourtDataDTO courtDataDTO) {
        // Verify WQCore Record is created
        Optional<WqCoreEntity> retrievedWqCoreEntity = wqCoreRepository.findById(courtDataDTO.getTxId());
        WqCoreEntity wqCoreEntity = retrievedWqCoreEntity.orElse(null);
        assert wqCoreEntity != null;
        assertThat(wqCoreEntity.getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(wqCoreEntity.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(wqCoreEntity.getWqStatus()).isEqualTo(WQ_SUCCESS_STATUS);
        assertThat(wqCoreEntity.getWqType()).isEqualTo(WQ_CREATION_EVENT);
    }

    private void verifyCase(CourtDataDTO courtDataDTO) {
        // Verify Case record is created
        Optional<CaseEntity> retrievedCaseEntity = caseRepository.findById(courtDataDTO.getTxId());
        CaseEntity caseEntity = retrievedCaseEntity.orElse(null);
        assert caseEntity != null;

        assertThat(caseEntity.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(caseEntity.getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(caseEntity.getAsn()).isEqualTo(courtDataDTO.getCaseDetails().getAsn());
        assertThat(caseEntity.getDocLanguage()).isEqualTo(courtDataDTO.getCaseDetails().getDocLanguage());
        assertThat(caseEntity.getInactive()).isEqualTo(NO);
    }


}

