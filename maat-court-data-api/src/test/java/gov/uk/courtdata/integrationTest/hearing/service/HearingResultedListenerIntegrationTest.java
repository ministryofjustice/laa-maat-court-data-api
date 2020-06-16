package gov.uk.courtdata.integrationTest.hearing.service;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.hearing.service.HearingResultedListener;
import gov.uk.courtdata.integrationTest.MockServicesConfig;
import gov.uk.courtdata.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class HearingResultedListenerIntegrationTest {

    @Autowired
    private HearingResultedListener hearingResultedListener;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private  RepOrderRepository repOrderRepository;
    @Autowired
    private  WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private  IdentifierRepository identifierRepository;

    @Autowired
    private AppealTypeRepository appealTypeRepository;

    @Autowired
    private CrownCourtOutcomeRepository crownCourtOutcomeRepository;

    @Autowired
    private CrownCourtProcessingRepository crownCourtProcessingRepository;

    @Autowired XLATOffenceRepository xlatOffenceRepository;

    @Autowired
    private XLATResultRepository xlatResultRepository;

    @Autowired
    private WQCaseRepository wqCaseRepository;

    @Before
    public void setUp() {
        repOrderRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        identifierRepository.deleteAll();
        crownCourtOutcomeRepository.deleteAll();
        appealTypeRepository.deleteAll();
        crownCourtProcessingRepository.deleteAll();
        xlatOffenceRepository.deleteAll();
        xlatResultRepository.deleteAll();
        wqCaseRepository.deleteAll();
    }

    @Test
    public void givenMessage_whenHearingServiceInvoked_thenSave() {
        //given
        repOrderRepository.save(testEntityDataBuilder.getRepOrder());
        wqLinkRegisterRepository.save(testEntityDataBuilder.getWqLinkRegisterEntity());

        crownCourtOutcomeRepository.save(testEntityDataBuilder.getCrownCourtOutComeEntity());
        appealTypeRepository.save(testEntityDataBuilder.getAppealTypeEntity());

        xlatOffenceRepository.save(testEntityDataBuilder.getXLATOffenceEntity());

        xlatResultRepository.save(testEntityDataBuilder.getXLATResultEntity());

        //setup SP


        String message = testModelDataBuilder.hearingString();
        //when
        hearingResultedListener.receive(message);

        //then


    }

    @Test
    public void givenMegMessage_whenHearingServiceInvoked_thenSave() {
        //given
        repOrderRepository.save(testEntityDataBuilder.getRepOrder());
        wqLinkRegisterRepository.save(testEntityDataBuilder.getWqLinkRegisterEntity());

        crownCourtOutcomeRepository.save(testEntityDataBuilder.getCrownCourtOutComeEntity());
        appealTypeRepository.save(testEntityDataBuilder.getAppealTypeEntity());

        xlatOffenceRepository.save(testEntityDataBuilder.getXLATOffenceEntity());

        xlatResultRepository.save(testEntityDataBuilder.getXLATResultEntity());

        //setup SP


        String message = testModelDataBuilder.megCourtPayload();
        //when
        hearingResultedListener.receive(message);

        //then


    }

}
