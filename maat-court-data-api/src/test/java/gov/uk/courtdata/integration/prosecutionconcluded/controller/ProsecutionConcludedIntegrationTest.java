package gov.uk.courtdata.integration.prosecutionconcluded.controller;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.listner.ProsecutionConcludedListener;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.model.Plea;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedService;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.unlink.controller.UnLinkController;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static gov.uk.courtdata.constants.CourtDataConstants.COMMITTAL_FOR_TRIAL_SUB_TYPE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class ProsecutionConcludedIntegrationTest {

    @Autowired
    private OffenceRepository offenceRepository;
    @Autowired
    private WQResultRepository wqResultRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private XLATResultRepository xlatResultRepository;
    @Autowired
    private WQHearingRepository wqHearingRepository;
    @Autowired private ProsecutionConcludedListener prosecutionConcludedListener;
    @Autowired private ReservationsRepository reservationsRepository;
    @Autowired private CrownCourtCodeRepository crownCourtCodeRepository;
    @Autowired private RepOrderRepository repOrderRepository;
    @Autowired private CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;

    @Autowired
    private TestModelDataBuilder testModelDataBuilder;


    @Autowired
    private EntityManager entityManager;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        offenceRepository.deleteAll();
        wqResultRepository.deleteAll();
        resultRepository.deleteAll();
        xlatResultRepository.deleteAll();
    }

    @Test
    public void givenProsCon_whenMessageIsReceived_thenProcessIsConvicted() {
        //given
        WQHearingEntity.builder().build();

        wqHearingRepository.save(WQHearingEntity.builder()
                        .resultCodes("4057,3272,3101,1115,3052,1179")
                        .hearingUUID("908ad01e-5a38-4158-957a-0c1d1a783862")
                        .caseUrn("21GN1208521")
                        .maatId(5635566)
                        .txId(8072786)
                        .caseId(665471)
                        .ouCourtLocation("OU")
                        .wqJurisdictionType(JurisdictionType.CROWN.name())
                .build());

        reservationsRepository.save(ReservationsEntity.builder().recordId(324233).userName("test-user").build());

        offenceRepository.save(OffenceEntity.builder()
                        .txId(8072786)
                        .offenceId("ed0e9d59-cc1c-4869-8fcd-464caf770744")
                        .asnSeq("001")
                        .caseId(665471)
                        .applicationFlag(1)

                .build());

        xlatResultRepository.save(XLATResultEntity.builder().cjsResultCode(4057).wqType(WQType.COMMITTAL_QUEUE.value()).subTypeCode(COMMITTAL_FOR_TRIAL_SUB_TYPE).build());
        resultRepository.save(ResultEntity.builder().resultCode("4057").asn("123456754").asnSeq("1").caseId(665471).txId(8072786).build());
        wqResultRepository.save(WQResultEntity.builder().caseId(665471).txId(8072786).asn("MG25A12456").asnSeq("1").resultCode(4057).build());
        repOrderRepository.save(RepOrderEntity.builder().id(5635566).caseId("665471").catyCaseType("INDICTABLE").aptyCode("INDICTABLE").build());

        crownCourtCodeRepository.save(CrownCourtCode.builder().ouCode("OU").code("121").build());

        String sqsJson = testModelDataBuilder.getProsecutionConcludedJson();

        prosecutionConcludedListener.receive(sqsJson);
    }

}
