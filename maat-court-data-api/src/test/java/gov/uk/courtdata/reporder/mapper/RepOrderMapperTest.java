package gov.uk.courtdata.reporder.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.dto.RepOrderStateDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.mapper.YesNoConvertorImpl;
import gov.uk.courtdata.model.reporder.MaatSearchResponse;
import uk.gov.justice.laa.crime.dto.maat.UserDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RepOrderMapperImpl.class, YesNoConvertorImpl.class})
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
    private static final String MEANS_WORK_REASON = "HR";
    private static final String PASSPORT_WORK_REASON = "FMA";

    @Autowired
    private RepOrderMapper repOrderMapper; // Assuming an implementation exists

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
                .nworCode(PASSPORT_WORK_REASON)
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
                .newWorkReason(
                        NewWorkReasonEntity.builder().code(MEANS_WORK_REASON).build())
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

    @ParameterizedTest
    @MethodSource("booleanToYesNoArguments")
    void givenPassportAssessmentBooleanFields_whenMapRepOrder_thenMapsToLegacyYesNoFields(
            Boolean sourceValue, String expectedValue) {

        PassportAssessmentEntity passportAssessment = new PassportAssessmentEntity();
        passportAssessment.setPartnerBenefitClaimed(sourceValue);
        passportAssessment.setIncomeSupport(sourceValue);
        passportAssessment.setJobSeekers(sourceValue);
        passportAssessment.setStatePensionCredit(sourceValue);
        passportAssessment.setUnder18FullEducation(sourceValue);
        passportAssessment.setUnder16(sourceValue);
        passportAssessment.setBetween16And17(sourceValue);
        passportAssessment.setUnder18HeardInYouthCourt(sourceValue);
        passportAssessment.setUnder18HeardInMagsCourt(sourceValue);
        passportAssessment.setEsa(sourceValue);
        passportAssessment.setReplaced(sourceValue);
        passportAssessment.setValid(sourceValue);

        PassportAssessmentEvidenceEntity evidenceEntity = PassportAssessmentEvidenceEntity.builder()
                .mandatory(sourceValue)
                .build();

        passportAssessment.getPassportAssessmentEvidences().add(evidenceEntity);

        RepOrderEntity repOrder = new RepOrderEntity();
        repOrder.getPassportAssessments().add(passportAssessment);

        RepOrderDTO result = repOrderMapper.repOrderEntityToRepOrderDTO(repOrder);

        PassportAssessmentDTO passportResult = result.getPassportAssessments().getFirst();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(passportResult.getPartnerBenefitClaimed()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getIncomeSupport()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getJobSeekers()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getStatePensionCredit()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getUnder18FullEducation()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getUnder16()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getBetween16And17()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getUnder18HeardInYouthCourt()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getUnder18HeardInMagsCourt()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getEsa()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getReplaced()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getValid()).isEqualTo(expectedValue);
            softly.assertThat(passportResult.getPassportAssessmentEvidences().size())
                    .isEqualTo(1);
            softly.assertThat(passportResult
                            .getPassportAssessmentEvidences()
                            .getFirst()
                            .getMandatory())
                    .isEqualTo(expectedValue);
        });
    }

    private static Stream<Arguments> booleanToYesNoArguments() {
        return Stream.of(Arguments.of(true, "Y"), Arguments.of(false, "N"), Arguments.of(null, null));
    }

    @ParameterizedTest
    @MethodSource("yesNoToBooleanArguments")
    void givenUserEntityYesNoFields_whenMapRepOrder_thenMapsToBooleanFields(String sourceValue, Boolean expectedValue) {

        UserEntity user = new UserEntity();
        user.setEnabled(sourceValue);
        user.setLoggedIn(sourceValue);
        user.setLocked(sourceValue);

        RepOrderEntity repOrder = new RepOrderEntity();
        repOrder.setUserCreatedEntity(user);

        RepOrderDTO result = repOrderMapper.repOrderEntityToRepOrderDTO(repOrder);

        UserDTO userResult = result.getUserCreatedEntity();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userResult.isEnabled()).isEqualTo(expectedValue);
            softly.assertThat(userResult.isLoggedIn()).isEqualTo(expectedValue);
            softly.assertThat(userResult.isLocked()).isEqualTo(expectedValue);
        });
    }

    private static Stream<Arguments> yesNoToBooleanArguments() {
        return Stream.of(Arguments.of("Y", true), Arguments.of("N", false));
    }

    // test passport
    @Test
    void givenValidRepOrderEntityWithPassportAssessmentPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getPassportAssessments().add(passportAssessmentPass);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CASE_TYPE);
            assertThat(repOrderState.getIojResult()).isEqualTo(IOJ_RESULT_PASS);
            assertThat(repOrderState.getIojAssessorName()).isNotNull();
            assertThat(repOrderState.getIojAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            assertThat(repOrderState.getIojReason()).isNull();
            assertThat(repOrderState.getMeansInitResult()).isNull();
            assertThat(repOrderState.getMeansInitStatus()).isNull();
            assertThat(repOrderState.getMeansFullResult()).isNull();
            assertThat(repOrderState.getMeansFullStatus()).isNull();
            assertThat(repOrderState.getMeansAssessorName()).isNull();
            assertThat(repOrderState.getDateMeansCreated()).isNull();
            assertThat(repOrderState.getPassportResult()).isEqualTo(PASSPORT_RESULT_PASS);
            assertThat(repOrderState.getPassportStatus()).isEqualTo(PASSPORT_STATUS);
            assertThat(repOrderState.getPassportAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDatePassportCreated()).isEqualTo(DATE_PASSPORT_CREATED);
            assertThat(repOrderState.getIojAppealResult()).isNull();
            assertThat(repOrderState.getIojAppealAssessorName()).isNull();
            assertThat(repOrderState.getIojAppealDate()).isNull();
            assertThat(repOrderState.getFundingDecision()).isEqualTo(FUNDING_DECISION);
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
            assertThat(repOrderState.getPassportReviewType()).isEqualTo(PASSPORT_REVIEW_TYPE);
            assertThat(repOrderState.getPassportWorkReason()).isEqualTo(PASSPORT_WORK_REASON);
        });
    }

    @Test
    void givenPassportFailAndInitialPass_whenMapRepOrderStateIsInvoked_thenMapsAssessmentResults() {
        repOrderEntity.getPassportAssessments().add(passportAssessmentFail);
        repOrderEntity.getFinancialAssessments().add(financialAssessmentInitialPass);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CASE_TYPE);
            assertThat(repOrderState.getIojResult()).isEqualTo(IOJ_RESULT_PASS);
            assertThat(repOrderState.getIojAssessorName()).isNotNull().isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            assertThat(repOrderState.getIojReason()).isNull();
            assertThat(repOrderState.getMeansInitResult()).isEqualTo(MEANS_INIT_RESULT_PASS);
            assertThat(repOrderState.getMeansInitStatus()).isEqualTo(MEANS_STATUS);
            assertThat(repOrderState.getMeansFullResult()).isNull();
            assertThat(repOrderState.getMeansFullStatus()).isNull();
            assertThat(repOrderState.getMeansAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateMeansCreated()).isEqualTo(DATE_MEANS_CREATED);
            assertThat(repOrderState.getPassportResult()).isEqualTo(PASSPORT_RESULT_FAIL);
            assertThat(repOrderState.getPassportStatus()).isEqualTo(PASSPORT_STATUS);
            assertThat(repOrderState.getPassportAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDatePassportCreated()).isEqualTo(DATE_PASSPORT_CREATED);
            assertThat(repOrderState.getIojAppealResult()).isNull();
            assertThat(repOrderState.getIojAppealAssessorName()).isNull();
            assertThat(repOrderState.getIojAppealDate()).isNull();
            assertThat(repOrderState.getFundingDecision()).isEqualTo(FUNDING_DECISION);
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
            assertThat(repOrderState.getMeansReviewType()).isEqualTo(MEANS_REVIEW_TYPE);
            assertThat(repOrderState.getMeansWorkReason()).isEqualTo(MEANS_WORK_REASON);
        });
    }

    @Test
    void givenPassportFailInitialFailAndFullPass_whenMapRepOrderStateIsInvoked_thenMapsAssessmentResults() {
        repOrderEntity.getPassportAssessments().add(passportAssessmentFail);
        repOrderEntity.getFinancialAssessments().add(financialAssessmentInitialFail);
        repOrderEntity.getFinancialAssessments().add(financialAssessmentFullPass);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CASE_TYPE);
            assertThat(repOrderState.getIojResult()).isEqualTo(IOJ_RESULT_PASS);
            assertThat(repOrderState.getIojAssessorName()).isNotNull().isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            assertThat(repOrderState.getIojReason()).isNull();
            assertThat(repOrderState.getMeansInitResult()).isEqualTo(MEANS_INIT_RESULT_FAIL);
            assertThat(repOrderState.getMeansInitStatus()).isEqualTo(MEANS_STATUS);
            assertThat(repOrderState.getMeansFullResult()).isEqualTo(MEANS_FULL_RESULT_PASS);
            assertThat(repOrderState.getMeansFullStatus()).isEqualTo(MEANS_STATUS);
            assertThat(repOrderState.getMeansAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateMeansCreated()).isEqualTo(DATE_MEANS_CREATED);
            assertThat(repOrderState.getPassportResult()).isEqualTo(PASSPORT_RESULT_FAIL);
            assertThat(repOrderState.getPassportStatus()).isEqualTo(PASSPORT_STATUS);
            assertThat(repOrderState.getPassportAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDatePassportCreated()).isEqualTo(DATE_PASSPORT_CREATED);
            assertThat(repOrderState.getFundingDecision()).isEqualTo(FUNDING_DECISION);
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
        });
    }

    @Test
    void givenValidRepOrderEntityWithIojAppealPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getIojAppeal().add(iojAppealPass);
        repOrderEntity.setIojResult(IOJ_RESULT_FAIL);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CASE_TYPE);
            assertThat(repOrderState.getIojResult()).isEqualTo(IOJ_RESULT_FAIL);
            assertThat(repOrderState.getIojAssessorName()).isNotNull().isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            assertThat(repOrderState.getIojReason()).isNull();
            assertThat(repOrderState.getMeansInitResult()).isNull();
            assertThat(repOrderState.getMeansInitStatus()).isNull();
            assertThat(repOrderState.getMeansFullResult()).isNull();
            assertThat(repOrderState.getMeansFullStatus()).isNull();
            assertThat(repOrderState.getMeansAssessorName()).isNull();
            assertThat(repOrderState.getDateMeansCreated()).isNull();
            assertThat(repOrderState.getPassportResult()).isNull();
            assertThat(repOrderState.getPassportStatus()).isNull();
            assertThat(repOrderState.getPassportAssessorName()).isNull();
            assertThat(repOrderState.getDatePassportCreated()).isNull();
            assertThat(repOrderState.getIojAppealResult()).isEqualTo(IOJ_APPEAL_RESULT_PASS);
            assertThat(repOrderState.getIojAppealAssessorName()).isEqualTo(IOJ_APPEAL_ASSESSOR_NAME);
            assertThat(repOrderState.getIojAppealDate()).isEqualTo(IOJ_APPEAL_DATE);
            assertThat(repOrderState.getFundingDecision()).isEqualTo(FUNDING_DECISION);
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
        });
    }

    @Test
    void givenValidRepOrderEntityWithIojAppealFail_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        repOrderEntity.getIojAppeal().add(iojAppealFail);
        repOrderEntity.setIojResult(IOJ_RESULT_FAIL);

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CASE_TYPE);
            assertThat(repOrderState.getIojResult()).isEqualTo(IOJ_RESULT_FAIL);
            assertThat(repOrderState.getIojAssessorName()).isNotNull().isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            assertThat(repOrderState.getIojReason()).isNull();
            assertThat(repOrderState.getMeansInitResult()).isNull();
            assertThat(repOrderState.getMeansInitStatus()).isNull();
            assertThat(repOrderState.getMeansFullResult()).isNull();
            assertThat(repOrderState.getMeansFullStatus()).isNull();
            assertThat(repOrderState.getMeansAssessorName()).isNull();
            assertThat(repOrderState.getDateMeansCreated()).isNull();
            assertThat(repOrderState.getPassportResult()).isNull();
            assertThat(repOrderState.getPassportStatus()).isNull();
            assertThat(repOrderState.getPassportAssessorName()).isNull();
            assertThat(repOrderState.getDatePassportCreated()).isNull();
            assertThat(repOrderState.getIojAppealResult()).isEqualTo(IOJ_APPEAL_RESULT_FAIL);
            assertThat(repOrderState.getIojAppealAssessorName()).isEqualTo(IOJ_APPEAL_ASSESSOR_NAME);
            assertThat(repOrderState.getIojAppealDate()).isEqualTo(IOJ_APPEAL_DATE);
            assertThat(repOrderState.getFundingDecision()).isEqualTo(FUNDING_DECISION);
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
        });
    }

    @Test
    void givenAValidRequestAndNullLinking_whenMapMaatSearchResponseIsInvoked_thenCorrectResponseShouldReturn() {
        MaatSearchResponse response = repOrderMapper.mapMaatSearchResponse(TestEntityDataBuilder.REP_ID, null, null);
        assertThat(response.getMaatId()).isEqualTo(TestEntityDataBuilder.REP_ID);
        assertThat(response.isLinked()).isFalse();
    }

    @Test
    void givenAValidRequestAndEmptyLinking_whenMapMaatSearchResponseIsInvoked_thenCorrectResponseShouldReturn() {
        MaatSearchResponse response =
                repOrderMapper.mapMaatSearchResponse(TestEntityDataBuilder.REP_ID, Collections.emptyList(), null);
        assertThat(response.getMaatId()).isEqualTo(TestEntityDataBuilder.REP_ID);
        assertThat(response.isLinked()).isFalse();
    }

    @Test
    void givenAValidRequest_whenMapMaatSearchResponseIsInvoked_thenCorrectResponseShouldReturn() {
        List<WqLinkRegisterEntity> wqLinkRegisterEntities =
                List.of(TestEntityDataBuilder.getWQLinkRegisterEntity(1235));
        MaatSearchResponse response = repOrderMapper.mapMaatSearchResponse(
                TestEntityDataBuilder.REP_ID, wqLinkRegisterEntities, TestEntityDataBuilder.CASE_URN);
        assertThat(response.getMaatId()).isEqualTo(TestEntityDataBuilder.REP_ID);
        assertThat(response.isLinked()).isTrue();
        assertThat(response.getLinkingDetail().getLibraId()).isEqualTo(TestEntityDataBuilder.LIBRA_ID);
        assertThat(response.getLinkingDetail().getCaseUrn()).isEqualTo(TestEntityDataBuilder.CASE_URN);
        assertThat(response.getLinkingDetail().getCaseId()).isEqualTo(TestEntityDataBuilder.TEST_CASE_ID);
        assertThat(response.getLinkingDetail().getCjsAreaCode()).isEqualTo("16");
        assertThat(response.getLinkingDetail().getCjsLocation()).isEqualTo("B16BG");
    }
}
