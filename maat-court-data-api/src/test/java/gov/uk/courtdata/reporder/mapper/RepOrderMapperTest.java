package gov.uk.courtdata.reporder.mapper;

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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.justice.laa.crime.dto.maat.UserDTO;
import uk.gov.justice.laa.crime.enums.AssessmentResult;
import uk.gov.justice.laa.crime.enums.CaseType;
import uk.gov.justice.laa.crime.enums.CurrentStatus;
import uk.gov.justice.laa.crime.enums.DecisionReason;
import uk.gov.justice.laa.crime.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.enums.NewWorkReason;
import uk.gov.justice.laa.crime.enums.PassportAssessmentResult;
import uk.gov.justice.laa.crime.enums.ReviewResult;
import uk.gov.justice.laa.crime.enums.ReviewType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RepOrderMapperImpl.class, YesNoConvertorImpl.class})
class RepOrderMapperTest {

    private static final int USN = 810529;
    private static final int MAAT_REF = 4799873;

    private static final String CASE_ID = "1400466826-10";
    private static final String IOJ_ASSESSOR_FULL_NAME = "Maeve OConnor";
    private static final String ASSESSOR_USERNAME = "ocon-m";
    private static final String CC_REP_DECISION = "Granted - Passed Means Test";

    private static final LocalDate DATE_APP_CREATED = LocalDate.of(2015, 1, 9);
    private static final LocalDateTime IOJ_APPEAL_DATE = LocalDateTime.of(2015, 1, 9, 11, 16, 32);
    private static final LocalDateTime DATE_MEANS_CREATED = LocalDateTime.of(2015, 1, 9, 11, 16, 54);
    private static final LocalDateTime DATE_PASSPORT_CREATED = LocalDateTime.of(2015, 1, 9, 11, 16, 29);

    @Autowired
    private RepOrderMapper repOrderMapper;

    private static UserEntity assessor() {
        return UserEntity.builder()
                .firstName("Maeve")
                .surname("OConnor")
                .username(ASSESSOR_USERNAME)
                .build();
    }

    private static RepOrderEntity baseRepOrder() {
        return RepOrderEntity.builder()
                .usn(USN)
                .id(MAAT_REF)
                .caseId(CASE_ID)
                .catyCaseType(CaseType.SUMMARY_ONLY.getCaseType())
                .iojResult(ReviewResult.PASS.getResult())
                .dateCreated(DATE_APP_CREATED)
                .userCreatedEntity(assessor())
                .userCreated(ASSESSOR_USERNAME)
                .decisionReasonCode(DecisionReason.GRANTED.getCode())
                .crownRepOrderDecision(CC_REP_DECISION)
                .build();
    }

    private static PassportAssessmentEntity passportAssessment(PassportAssessmentResult result) {
        return PassportAssessmentEntity.builder()
                .id(1)
                .result(result.getResult())
                .pastStatus(CurrentStatus.COMPLETE.getStatus())
                .dateCreated(DATE_PASSPORT_CREATED)
                .rtCode(ReviewType.ER.getCode())
                .userCreatedEntity(assessor())
                .nworCode(NewWorkReason.FMA.getCode())
                .build();
    }

    private static FinancialAssessmentEntity initialFinancialAssessment(InitAssessmentResult result) {
        return FinancialAssessmentEntity.builder()
                .id(1)
                .initResult(result.getResult())
                .fassInitStatus(CurrentStatus.COMPLETE.getStatus())
                .dateCreated(DATE_MEANS_CREATED)
                .userCreatedEntity(assessor())
                .rtCode(ReviewType.ER.getCode())
                .newWorkReason(NewWorkReasonEntity.builder().code(NewWorkReason.FMA.getCode()).build())
                .build();
    }

    private static FinancialAssessmentEntity fullFinancialAssessment(FullAssessmentResult fullResult) {
        return FinancialAssessmentEntity.builder()
                .id(2)
                .initResult(InitAssessmentResult.FULL.getResult())
                .fassInitStatus(CurrentStatus.COMPLETE.getStatus())
                .fullResult(fullResult.getResult())
                .fassFullStatus(CurrentStatus.COMPLETE.getStatus())
                .dateCreated(DATE_MEANS_CREATED)
                .userCreatedEntity(assessor())
                .rtCode(ReviewType.ER.getCode())
                .build();
    }

    private static IOJAppealEntity iojAppeal(ReviewResult iojResult) {
        return IOJAppealEntity.builder()
                .decisionResult(iojResult.getResult())
                .userCreated(ASSESSOR_USERNAME)
                .decisionDate(IOJ_APPEAL_DATE)
                .build();
    }

    @ParameterizedTest
    @MethodSource("booleanToYesNoArguments")
    void givenPassportAssessmentBooleanFields_whenMapRepOrder_thenMapsToLegacyYesNoFields(
            Boolean sourceValue, String expectedValue) {

        PassportAssessmentEntity passportAssessment = passportAssessment(PassportAssessmentResult.PASS);
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

        PassportAssessmentEvidenceEntity evidenceEntity =
                PassportAssessmentEvidenceEntity.builder()
                        .mandatory(sourceValue)
                        .build();

        passportAssessment.setUserCreatedEntity(null);
        passportAssessment.getPassportAssessmentEvidences().add(evidenceEntity);

        RepOrderEntity repOrder = RepOrderEntity.builder().build();
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

    @Test
    void givenValidRepOrderEntityWithPassportAssessmentPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {

        RepOrderEntity repOrderEntity = baseRepOrder();
        repOrderEntity.getPassportAssessments().add(passportAssessment(PassportAssessmentResult.PASS));

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CaseType.SUMMARY_ONLY.getCaseType());
            assertThat(repOrderState.getIojResult()).isEqualTo(ReviewResult.PASS.getResult());
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
            assertThat(repOrderState.getPassportResult()).isEqualTo(PassportAssessmentResult.PASS.getResult());
            assertThat(repOrderState.getPassportStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            assertThat(repOrderState.getPassportAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDatePassportCreated()).isEqualTo(DATE_PASSPORT_CREATED);
            assertThat(repOrderState.getIojAppealResult()).isNull();
            assertThat(repOrderState.getIojAppealAssessorName()).isNull();
            assertThat(repOrderState.getIojAppealDate()).isNull();
            assertThat(repOrderState.getFundingDecision()).isEqualTo(DecisionReason.GRANTED.getCode());
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
            assertThat(repOrderState.getPassportReviewType()).isEqualTo(ReviewType.ER.getCode());
            assertThat(repOrderState.getPassportWorkReason()).isEqualTo(NewWorkReason.FMA.getCode());
        });
    }

    @Test
    void givenPassportFailAndInitialPass_whenMapRepOrderStateIsInvoked_thenMapsAssessmentResults() {
        RepOrderEntity repOrderEntity = baseRepOrder();
        repOrderEntity.getPassportAssessments().add(passportAssessment(PassportAssessmentResult.FAIL));
        repOrderEntity.getFinancialAssessments().add(initialFinancialAssessment(InitAssessmentResult.PASS));

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CaseType.SUMMARY_ONLY.getCaseType());
            assertThat(repOrderState.getIojResult()).isEqualTo(ReviewResult.PASS.getResult());
            assertThat(repOrderState.getIojAssessorName()).isNotNull().isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            assertThat(repOrderState.getIojReason()).isNull();
            assertThat(repOrderState.getMeansInitResult()).isEqualTo(InitAssessmentResult.PASS.getResult());
            assertThat(repOrderState.getMeansInitStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            assertThat(repOrderState.getMeansFullResult()).isNull();
            assertThat(repOrderState.getMeansFullStatus()).isNull();
            assertThat(repOrderState.getMeansAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateMeansCreated()).isEqualTo(DATE_MEANS_CREATED);
            assertThat(repOrderState.getPassportResult()).isEqualTo(PassportAssessmentResult.FAIL.getResult());
            assertThat(repOrderState.getPassportStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            assertThat(repOrderState.getPassportAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDatePassportCreated()).isEqualTo(DATE_PASSPORT_CREATED);
            assertThat(repOrderState.getIojAppealResult()).isNull();
            assertThat(repOrderState.getIojAppealAssessorName()).isNull();
            assertThat(repOrderState.getIojAppealDate()).isNull();
            assertThat(repOrderState.getFundingDecision()).isEqualTo(DecisionReason.GRANTED.getCode());
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
            assertThat(repOrderState.getMeansReviewType()).isEqualTo(ReviewType.ER.getCode());
            assertThat(repOrderState.getMeansWorkReason()).isEqualTo(NewWorkReason.FMA.getCode());
        });
    }

    @Test
    void givenPassportFailInitialFailAndFullPass_whenMapRepOrderStateIsInvoked_thenMapsAssessmentResults() {
        RepOrderEntity repOrderEntity = baseRepOrder();
        repOrderEntity.getPassportAssessments().add(passportAssessment(PassportAssessmentResult.FAIL));
        repOrderEntity.getFinancialAssessments().add(initialFinancialAssessment(InitAssessmentResult.FAIL));
        repOrderEntity.getFinancialAssessments().add(fullFinancialAssessment(FullAssessmentResult.PASS));

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CaseType.SUMMARY_ONLY.getCaseType());
            assertThat(repOrderState.getIojResult()).isEqualTo(ReviewResult.PASS.getResult());
            assertThat(repOrderState.getIojAssessorName()).isNotNull().isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            assertThat(repOrderState.getIojReason()).isNull();
            assertThat(repOrderState.getMeansInitResult()).isEqualTo(InitAssessmentResult.FULL.getResult());
            assertThat(repOrderState.getMeansInitStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            assertThat(repOrderState.getMeansFullResult()).isEqualTo(FullAssessmentResult.PASS.getResult());
            assertThat(repOrderState.getMeansFullStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            assertThat(repOrderState.getMeansAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDateMeansCreated()).isEqualTo(DATE_MEANS_CREATED);
            assertThat(repOrderState.getPassportResult()).isEqualTo(PassportAssessmentResult.FAIL.getResult());
            assertThat(repOrderState.getPassportStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            assertThat(repOrderState.getPassportAssessorName()).isEqualTo(IOJ_ASSESSOR_FULL_NAME);
            assertThat(repOrderState.getDatePassportCreated()).isEqualTo(DATE_PASSPORT_CREATED);
            assertThat(repOrderState.getFundingDecision()).isEqualTo(DecisionReason.GRANTED.getCode());
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
        });
    }

    @Test
    void givenValidRepOrderEntityWithIojAppealPass_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        RepOrderEntity repOrderEntity = baseRepOrder();
        repOrderEntity.getIojAppeal().add(iojAppeal(ReviewResult.PASS));
        repOrderEntity.setIojResult(ReviewResult.FAIL.getResult());

        // Mapping RepOrderEntity to RepOrderStateDTO
        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        // Asserting the values
        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CaseType.SUMMARY_ONLY.getCaseType());
            assertThat(repOrderState.getIojResult()).isEqualTo(ReviewResult.FAIL.getResult());
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
            assertThat(repOrderState.getIojAppealResult()).isEqualTo(AssessmentResult.PASS.getResult());
            assertThat(repOrderState.getIojAppealAssessorName()).isEqualTo(ASSESSOR_USERNAME);
            assertThat(repOrderState.getIojAppealDate()).isEqualTo(IOJ_APPEAL_DATE);
            assertThat(repOrderState.getFundingDecision()).isEqualTo(DecisionReason.GRANTED.getCode());
            assertThat(repOrderState.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
        });
    }

    @Test
    void givenValidRepOrderEntityWithIojAppealFail_whenMapRepOrderStateIsCalled_thenRepOrderStateIsReturned() {
        RepOrderEntity repOrderEntity = baseRepOrder();
        repOrderEntity.setIojResult(ReviewResult.FAIL.getResult());
        repOrderEntity.getIojAppeal().add(iojAppeal(ReviewResult.FAIL));

        RepOrderStateDTO repOrderState = repOrderMapper.mapRepOrderState(repOrderEntity);

        SoftAssertions.assertSoftly(s -> {
            assertThat(repOrderState.getUsn()).isEqualTo(USN);
            assertThat(repOrderState.getMaatRef()).isEqualTo(MAAT_REF);
            assertThat(repOrderState.getCaseId()).isEqualTo(CASE_ID);
            assertThat(repOrderState.getCaseType()).isEqualTo(CaseType.SUMMARY_ONLY.getCaseType());
            assertThat(repOrderState.getIojResult()).isEqualTo(ReviewResult.FAIL.getResult());
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
            assertThat(repOrderState.getIojAppealResult()).isEqualTo(ReviewResult.FAIL.getResult());
            assertThat(repOrderState.getIojAppealAssessorName()).isEqualTo(ASSESSOR_USERNAME);
            assertThat(repOrderState.getIojAppealDate()).isEqualTo(IOJ_APPEAL_DATE);
            assertThat(repOrderState.getFundingDecision()).isEqualTo(DecisionReason.GRANTED.getCode());
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
