package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.RepOrderStateDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

class RepOrderMapperTest {

    private static final int USN = 810529;
    private static final int MAAT_REF = 4799873;
    private static final String CASE_ID = "1400466826-10";
    private static final String CASE_TYPE = "SUMMARY ONLY";
    private static final String IOJ_RESULT_PASS = "PASS";
    private static final String IOJ_RESULT_FAIL = "FAIL";
    private static final String IOJ_REASON = null;
    private static final String MEANS_INIT_RESULT_PASS = "PASS";
    private static final String MEANS_INIT_RESULT_FAIL = "FAIL";
    private static final String MEANS_STATUS = "COMPLETE";
    private static final String MEANS_FULL_RESULT_PASS = "PASS";
    private static final String IOJ_ASSESSOR_FULL_NAME = "Maeve OConnor";
    private static final String IOJ_ASSESSOR_USERNAME = "ocon-m";
    private static final LocalDate DATE_APP_CREATED = LocalDate.of(2015, 1, 9);
    private static final LocalDateTime DATE_MEANS_CREATED = LocalDateTime.of(2015, 1, 9, 11, 16, 54);
    private static final String PASSPORT_RESULT_FAIL = "FAIL";
    private static final String PASSPORT_RESULT_PASS = "PASS";
    private static final String PASSPORT_STATUS = "COMPLETE";
    private static final LocalDateTime DATE_PASSPORT_CREATED = LocalDateTime.of(2015, 1, 9, 11, 16, 29);
    private static final String FUNDING_DECISION = "GRANTED";
    private static final String CC_REP_DECISION = "Granted - Passed Means Test";
    private static final String IOJ_APPEAL_RESULT_PASS = "PASS";
    private static final String IOJ_APPEAL_RESULT_FAIL = "FAIL";
    private static final String IOJ_APPEAL_ASSESSOR_NAME = "ocon-m";
    private static final LocalDateTime IOJ_APPEAL_DATE = LocalDateTime.of(2015, 1, 9, 11, 16, 32);
    private static final String MEANS_REVIEW_TYPE = "NAFI";
    private static final String PASSPORT_REVIEW_TYPE = "ER";

    private final RepOrderMapper repOrderMapper = new RepOrderMapperImpl(); // Assuming an implementation exists

    private UserEntity userEntity;
    private PassportAssessmentEntity passportAssessmentPass;
    private PassportAssessmentEntity passportAssessmentFail;
    private FinancialAssessmentEntity financialAssessmentInitialPass;
    private FinancialAssessmentEntity financialAssessmentInitialFail;
    private FinancialAssessmentEntity financialAssessmentFullPass;
    private IOJAppealEntity iojAppealPass;
    private IOJAppealEntity iojAppealFail;
    private RepOrderEntity repOrderEntity;

    @BeforeEach
    void setUp() {
        // Mocking an IOJ Assessor
        userEntity = UserEntity.builder()
                .firstName(IOJ_ASSESSOR_FULL_NAME.split(" ")[0])
                .surname(IOJ_ASSESSOR_FULL_NAME.split(" ")[1])
                .username(IOJ_ASSESSOR_USERNAME)
                .build();

        // Mocking Passport Assessments
        passportAssessmentPass = PassportAssessmentEntity.builder()
                .id(1)
                .result(PASSPORT_RESULT_PASS)
                .pastStatus(PASSPORT_STATUS)
                .dateCreated(DATE_PASSPORT_CREATED)
                .userCreatedEntity(userEntity)
                .rtCode(PASSPORT_REVIEW_TYPE)
                .build();

        passportAssessmentFail = PassportAssessmentEntity.builder()
                .id(1)
                .result(PASSPORT_RESULT_FAIL)
                .pastStatus(PASSPORT_STATUS)
                .dateCreated(DATE_PASSPORT_CREATED)
                .userCreatedEntity(userEntity)
                .build();

        // Mocking Financial Assessments
        financialAssessmentInitialPass = FinancialAssessmentEntity.builder()
                .id(1)
                .initResult(MEANS_INIT_RESULT_PASS)
                .fassInitStatus(MEANS_STATUS)
                .dateCreated(DATE_MEANS_CREATED)
                .userCreatedEntity(userEntity)
                .rtCode(MEANS_REVIEW_TYPE)
                .build();

        financialAssessmentInitialFail = FinancialAssessmentEntity.builder()
                .id(1)
                .initResult(MEANS_INIT_RESULT_FAIL)
                .fassInitStatus(MEANS_STATUS)
                .dateCreated(DATE_MEANS_CREATED)
                .userCreatedEntity(userEntity)
                .build();

        financialAssessmentFullPass = FinancialAssessmentEntity.builder()
                .id(2)
                .initResult(MEANS_INIT_RESULT_FAIL)
                .fassInitStatus(MEANS_STATUS)
                .fullResult(MEANS_FULL_RESULT_PASS)
                .fassFullStatus(MEANS_STATUS)
                .dateCreated(DATE_MEANS_CREATED)
                .userCreatedEntity(userEntity)
                .build();

        // Mocking IoJ Appeals
        iojAppealPass = IOJAppealEntity.builder()
            .decisionResult(IOJ_APPEAL_RESULT_PASS)
            .userCreated(IOJ_APPEAL_ASSESSOR_NAME)
            .decisionDate(IOJ_APPEAL_DATE)
            .build();

        iojAppealFail = IOJAppealEntity.builder()
            .decisionResult(IOJ_APPEAL_RESULT_FAIL)
            .userCreated(IOJ_APPEAL_ASSESSOR_NAME)
            .decisionDate(IOJ_APPEAL_DATE)
            .build();

        // Mocking the RepOrderEntity
        repOrderEntity = RepOrderEntity.builder()
                .usn(USN)
                .id(MAAT_REF)
                .caseId(CASE_ID)
                .catyCaseType(CASE_TYPE)
                .iojResult(IOJ_RESULT_PASS)
                .iojResultNote(IOJ_REASON)
                .dateCreated(DATE_APP_CREATED)
                .userCreatedEntity(userEntity)
                .userCreated(IOJ_ASSESSOR_USERNAME)
                .decisionReasonCode(FUNDING_DECISION)
                .crownRepOrderDecision(CC_REP_DECISION)
                .build();
    }

    // test passport
    @Test
    void givenValidRepOrderEntityWithPassportAssessmentPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getPassportAssessments().add(passportAssessmentPass);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        assertAll("Assert actual RepOrderState",
                () -> assertEquals(USN, repOrderState.getUsn()),
                () -> assertEquals(MAAT_REF, repOrderState.getMaatRef()),
                () -> assertEquals(CASE_ID, repOrderState.getCaseId()),
                () -> assertEquals(CASE_TYPE, repOrderState.getCaseType()),
                () -> assertEquals(IOJ_RESULT_PASS, repOrderState.getIojResult()),
                () -> assertNotNull(repOrderState.getIojAssessorName()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getIojAssessorName()),
                () -> assertEquals(DATE_APP_CREATED, repOrderState.getDateAppCreated()),
                () -> assertNull(repOrderState.getIojReason()),
                () -> assertNull(repOrderState.getMeansInitResult()),
                () -> assertNull(repOrderState.getMeansInitStatus()),
                () -> assertNull(repOrderState.getMeansFullResult()),
                () -> assertNull(repOrderState.getMeansFullStatus()),
                () -> assertNull(repOrderState.getMeansAssessorName()),
                () -> assertNull(repOrderState.getDateMeansCreated()),
                () -> assertEquals(PASSPORT_RESULT_PASS, repOrderState.getPassportResult()),
                () -> assertEquals(PASSPORT_STATUS, repOrderState.getPassportStatus()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getPassportAssessorName()),
                () -> assertEquals(DATE_PASSPORT_CREATED, repOrderState.getDatePassportCreated()),
                () -> assertNull(repOrderState.getIojAppealResult()),
                () -> assertNull(repOrderState.getIojAppealAssessorName()),
                () -> assertNull(repOrderState.getIojAppealDate()),
                () -> assertEquals(FUNDING_DECISION, repOrderState.getFundingDecision()),
                () -> assertEquals(CC_REP_DECISION, repOrderState.getCcRepDecision()),
                () -> assertEquals(PASSPORT_REVIEW_TYPE, repOrderState.getPassportReviewType())
        );
    }

    @Test
    void givenValidRepOrderEntityWithPassportFailAndInitialAssessmentPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getPassportAssessments().add(passportAssessmentFail);
        repOrderEntity.getFinancialAssessments().add(financialAssessmentInitialPass);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        assertAll("Assert actual RepOrderState",
                () -> assertEquals(USN, repOrderState.getUsn()),
                () -> assertEquals(MAAT_REF, repOrderState.getMaatRef()),
                () -> assertEquals(CASE_ID, repOrderState.getCaseId()),
                () -> assertEquals(CASE_TYPE, repOrderState.getCaseType()),
                () -> assertEquals(IOJ_RESULT_PASS, repOrderState.getIojResult()),
                () -> assertNotNull(repOrderState.getIojAssessorName()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getIojAssessorName()),
                () -> assertEquals(DATE_APP_CREATED, repOrderState.getDateAppCreated()),
                () -> assertNull(repOrderState.getIojReason()),
                () -> assertEquals(MEANS_INIT_RESULT_PASS, repOrderState.getMeansInitResult()),
                () -> assertEquals(MEANS_STATUS, repOrderState.getMeansInitStatus()),
                () -> assertNull(repOrderState.getMeansFullResult()),
                () -> assertNull(repOrderState.getMeansFullStatus()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getMeansAssessorName()),
                () -> assertEquals(DATE_MEANS_CREATED, repOrderState.getDateMeansCreated()),
                () -> assertEquals(PASSPORT_RESULT_FAIL, repOrderState.getPassportResult()),
                () -> assertEquals(PASSPORT_STATUS, repOrderState.getPassportStatus()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getPassportAssessorName()),
                () -> assertEquals(DATE_PASSPORT_CREATED, repOrderState.getDatePassportCreated()),
                () -> assertEquals(FUNDING_DECISION, repOrderState.getFundingDecision()),
                () -> assertEquals(CC_REP_DECISION, repOrderState.getCcRepDecision()),
                () -> assertEquals(MEANS_REVIEW_TYPE, repOrderState.getMeansReviewType())
        );
    }

    @Test
    void givenValidRepOrderEntityWithPassportFailAndInitialAssessmentFailAndFullAssessmentPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getPassportAssessments().add(passportAssessmentFail);
        repOrderEntity.getFinancialAssessments().add(financialAssessmentInitialFail);
        repOrderEntity.getFinancialAssessments().add(financialAssessmentFullPass);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        assertAll("Assert actual RepOrderState",
                () -> assertEquals(USN, repOrderState.getUsn()),
                () -> assertEquals(MAAT_REF, repOrderState.getMaatRef()),
                () -> assertEquals(CASE_ID, repOrderState.getCaseId()),
                () -> assertEquals(CASE_TYPE, repOrderState.getCaseType()),
                () -> assertEquals(IOJ_RESULT_PASS, repOrderState.getIojResult()),
                () -> assertNotNull(repOrderState.getIojAssessorName()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getIojAssessorName()),
                () -> assertEquals(DATE_APP_CREATED, repOrderState.getDateAppCreated()),
                () -> assertNull(repOrderState.getIojReason()),
                () -> assertEquals(MEANS_INIT_RESULT_FAIL, repOrderState.getMeansInitResult()),
                () -> assertEquals(MEANS_STATUS, repOrderState.getMeansInitStatus()),
                () -> assertEquals(MEANS_FULL_RESULT_PASS, repOrderState.getMeansFullResult()),
                () -> assertEquals(MEANS_STATUS, repOrderState.getMeansFullStatus()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getMeansAssessorName()),
                () -> assertEquals(DATE_MEANS_CREATED, repOrderState.getDateMeansCreated()),
                () -> assertEquals(PASSPORT_RESULT_FAIL, repOrderState.getPassportResult()),
                () -> assertEquals(PASSPORT_STATUS, repOrderState.getPassportStatus()),
                () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getPassportAssessorName()),
                () -> assertEquals(DATE_PASSPORT_CREATED, repOrderState.getDatePassportCreated()),
                () -> assertEquals(FUNDING_DECISION, repOrderState.getFundingDecision()),
                () -> assertEquals(CC_REP_DECISION, repOrderState.getCcRepDecision())
        );
    }

    @Test
    void givenValidRepOrderEntityWithIojAppealPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getIojAppeal().add(iojAppealPass);
        repOrderEntity.setIojResult(IOJ_RESULT_FAIL);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        assertAll("Assert actual RepOrderState",
            () -> assertEquals(USN, repOrderState.getUsn()),
            () -> assertEquals(MAAT_REF, repOrderState.getMaatRef()),
            () -> assertEquals(CASE_ID, repOrderState.getCaseId()),
            () -> assertEquals(CASE_TYPE, repOrderState.getCaseType()),
            () -> assertEquals(IOJ_RESULT_FAIL, repOrderState.getIojResult()),
            () -> assertNotNull(repOrderState.getIojAssessorName()),
            () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getIojAssessorName()),
            () -> assertEquals(DATE_APP_CREATED, repOrderState.getDateAppCreated()),
            () -> assertNull(repOrderState.getIojReason()),
            () -> assertNull(repOrderState.getMeansInitResult()),
            () -> assertNull(repOrderState.getMeansInitStatus()),
            () -> assertNull(repOrderState.getMeansFullResult()),
            () -> assertNull(repOrderState.getMeansFullStatus()),
            () -> assertNull(repOrderState.getMeansAssessorName()),
            () -> assertNull(repOrderState.getDateMeansCreated()),
            () -> assertNull(repOrderState.getPassportResult()),
            () -> assertNull(repOrderState.getPassportStatus()),
            () -> assertNull(repOrderState.getPassportAssessorName()),
            () -> assertNull(repOrderState.getDatePassportCreated()),
            () -> assertEquals(IOJ_APPEAL_RESULT_PASS, repOrderState.getIojAppealResult()),
            () -> assertEquals(IOJ_APPEAL_ASSESSOR_NAME, repOrderState.getIojAppealAssessorName()),
            () -> assertEquals(IOJ_APPEAL_DATE, repOrderState.getIojAppealDate()),
            () -> assertEquals(FUNDING_DECISION, repOrderState.getFundingDecision()),
            () -> assertEquals(CC_REP_DECISION, repOrderState.getCcRepDecision())
        );
    }


    @Test
    void givenValidRepOrderEntityWithIojAppealFail_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getIojAppeal().add(iojAppealFail);
        repOrderEntity.setIojResult(IOJ_RESULT_FAIL);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        assertAll("Assert actual RepOrderState",
            () -> assertEquals(USN, repOrderState.getUsn()),
            () -> assertEquals(MAAT_REF, repOrderState.getMaatRef()),
            () -> assertEquals(CASE_ID, repOrderState.getCaseId()),
            () -> assertEquals(CASE_TYPE, repOrderState.getCaseType()),
            () -> assertEquals(IOJ_RESULT_FAIL, repOrderState.getIojResult()),
            () -> assertNotNull(repOrderState.getIojAssessorName()),
            () -> assertEquals(IOJ_ASSESSOR_FULL_NAME, repOrderState.getIojAssessorName()),
            () -> assertEquals(DATE_APP_CREATED, repOrderState.getDateAppCreated()),
            () -> assertNull(repOrderState.getIojReason()),
            () -> assertNull(repOrderState.getMeansInitResult()),
            () -> assertNull(repOrderState.getMeansInitStatus()),
            () -> assertNull(repOrderState.getMeansFullResult()),
            () -> assertNull(repOrderState.getMeansFullStatus()),
            () -> assertNull(repOrderState.getMeansAssessorName()),
            () -> assertNull(repOrderState.getDateMeansCreated()),
            () -> assertNull(repOrderState.getPassportResult()),
            () -> assertNull(repOrderState.getPassportStatus()),
            () -> assertNull(repOrderState.getPassportAssessorName()),
            () -> assertNull(repOrderState.getDatePassportCreated()),
            () -> assertEquals(IOJ_APPEAL_RESULT_FAIL, repOrderState.getIojAppealResult()),
            () -> assertEquals(IOJ_APPEAL_ASSESSOR_NAME, repOrderState.getIojAppealAssessorName()),
            () -> assertEquals(IOJ_APPEAL_DATE, repOrderState.getIojAppealDate()),
            () -> assertEquals(FUNDING_DECISION, repOrderState.getFundingDecision()),
            () -> assertEquals(CC_REP_DECISION, repOrderState.getCcRepDecision())
        );
    }
}
