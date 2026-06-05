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
    private static final String ASSESSOR_FULL_NAME = "Maeve OConnor";
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

    @ParameterizedTest
    @MethodSource("booleanToYesNoArguments")
    void givenPassportAssessmentBooleanFields_whenMapRepOrder_thenMapsToLegacyYesNoFields(
            Boolean sourceValue, String expectedValue) {

        PassportAssessmentEntity passportAssessment = PassportAssessmentEntity.builder().build();

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

        passportAssessment.getPassportAssessmentEvidences().add(evidenceEntity);

        RepOrderEntity repOrder = RepOrderEntity.builder().build();
        repOrder.getPassportAssessments().add(passportAssessment);

        RepOrderDTO result = repOrderMapper.repOrderEntityToRepOrderDTO(repOrder);

        PassportAssessmentDTO passportResult = result.getPassportAssessments().getFirst();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(passportResult.getPartnerBenefitClaimed()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getIncomeSupport()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getJobSeekers()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getStatePensionCredit()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getUnder18FullEducation()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getUnder16()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getBetween16And17()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getUnder18HeardInYouthCourt()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getUnder18HeardInMagsCourt()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getEsa()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getReplaced()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getValid()).isEqualTo(expectedValue);
            s.assertThat(passportResult.getPassportAssessmentEvidences()).hasSize(1);
            s.assertThat(passportResult
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

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(userResult.isEnabled()).isEqualTo(expectedValue);
            s.assertThat(userResult.isLoggedIn()).isEqualTo(expectedValue);
            s.assertThat(userResult.isLocked()).isEqualTo(expectedValue);
        });
    }

    private static Stream<Arguments> yesNoToBooleanArguments() {
        return Stream.of(Arguments.of("Y", true), Arguments.of("N", false));
    }

    @Test
    void givenRepOrderWithAssessmentsAndIojAppeal_whenMapRepOrderState_thenMapsExpectedState() {
        RepOrderEntity repOrderEntity = baseRepOrder();
        repOrderEntity.setIojResult(ReviewResult.FAIL.getResult());

        PassportAssessmentEntity passportAssessment =
                PassportAssessmentEntity.builder()
                        .id(1)
                        .result(PassportAssessmentResult.FAIL.getResult())
                        .pastStatus(CurrentStatus.COMPLETE.getStatus())
                        .dateCreated(DATE_PASSPORT_CREATED)
                        .rtCode(ReviewType.ER.getCode())
                        .userCreatedEntity(assessor())
                        .nworCode(NewWorkReason.FMA.getCode())
                        .build();

        repOrderEntity.getPassportAssessments().add(passportAssessment);

        FinancialAssessmentEntity financialAssessment =
                FinancialAssessmentEntity.builder()
                        .id(1)
                        .initResult(InitAssessmentResult.FULL.getResult())
                        .fassInitStatus(CurrentStatus.COMPLETE.getStatus())
                        .fullResult(FullAssessmentResult.PASS.getResult())
                        .fassFullStatus(CurrentStatus.COMPLETE.getStatus())
                        .dateCreated(DATE_MEANS_CREATED)
                        .userCreatedEntity(assessor())
                        .rtCode(ReviewType.ER.getCode())
                        .newWorkReason(NewWorkReasonEntity.builder().code(NewWorkReason.FMA.getCode()).build())
                        .build();

        repOrderEntity.getFinancialAssessments().add(financialAssessment);

        IOJAppealEntity iojAppeal =
                IOJAppealEntity.builder()
                        .decisionResult(ReviewResult.PASS.getResult())
                        .userCreated(ASSESSOR_USERNAME)
                        .decisionDate(IOJ_APPEAL_DATE)
                        .build();

        repOrderEntity.getIojAppeal().add(iojAppeal);

        RepOrderStateDTO result = repOrderMapper.mapRepOrderState(repOrderEntity);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(result.getUsn()).isEqualTo(USN);
            s.assertThat(result.getMaatRef()).isEqualTo(MAAT_REF);
            s.assertThat(result.getCaseId()).isEqualTo(CASE_ID);
            s.assertThat(result.getCaseType()).isEqualTo(CaseType.SUMMARY_ONLY.getCaseType());
            s.assertThat(result.getIojResult()).isEqualTo(ReviewResult.FAIL.getResult());
            s.assertThat(result.getIojAssessorName()).isEqualTo(ASSESSOR_FULL_NAME);
            s.assertThat(result.getDateAppCreated()).isEqualTo(DATE_APP_CREATED);
            s.assertThat(result.getCcRepDecision()).isEqualTo(CC_REP_DECISION);
            s.assertThat(result.getFundingDecision()).isEqualTo(DecisionReason.GRANTED.getCode());

            s.assertThat(result.getPassportResult()).isEqualTo(PassportAssessmentResult.FAIL.getResult());
            s.assertThat(result.getPassportStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            s.assertThat(result.getPassportAssessorName()).isEqualTo(ASSESSOR_FULL_NAME);
            s.assertThat(result.getDatePassportCreated()).isEqualTo(DATE_PASSPORT_CREATED);
            s.assertThat(result.getPassportReviewType()).isEqualTo(ReviewType.ER.getCode());
            s.assertThat(result.getPassportWorkReason()).isEqualTo(NewWorkReason.FMA.getCode());

            s.assertThat(result.getMeansInitResult()).isEqualTo(InitAssessmentResult.FULL.getResult());
            s.assertThat(result.getMeansInitStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            s.assertThat(result.getMeansFullResult()).isEqualTo(FullAssessmentResult.PASS.getResult());
            s.assertThat(result.getMeansFullStatus()).isEqualTo(CurrentStatus.COMPLETE.getStatus());
            s.assertThat(result.getMeansAssessorName()).isEqualTo(ASSESSOR_FULL_NAME);
            s.assertThat(result.getDateMeansCreated()).isEqualTo(DATE_MEANS_CREATED);
            s.assertThat(result.getMeansReviewType()).isEqualTo(ReviewType.ER.getCode());
            s.assertThat(result.getMeansWorkReason()).isEqualTo(NewWorkReason.FMA.getCode());

            s.assertThat(result.getIojAppealResult()).isEqualTo(ReviewResult.PASS.getResult());
            s.assertThat(result.getIojAppealAssessorName()).isEqualTo(ASSESSOR_USERNAME);
            s.assertThat(result.getIojAppealDate()).isEqualTo(IOJ_APPEAL_DATE);
        });
    }

    @ParameterizedTest
    @MethodSource("unlinkedWqLinkRegisterArguments")
    void givenNoLinking_whenMapMaatSearchResponse_thenReturnsUnlinkedResponse(
            List<WqLinkRegisterEntity> links) {

        MaatSearchResponse response =
                repOrderMapper.mapMaatSearchResponse(TestEntityDataBuilder.REP_ID, links, null);

        assertThat(response.isLinked()).isFalse();
        assertThat(response.getMaatId()).isEqualTo(TestEntityDataBuilder.REP_ID);
    }

    private static Stream<Arguments> unlinkedWqLinkRegisterArguments() {
        return Stream.of(
                Arguments.of((List<WqLinkRegisterEntity>) null),
                Arguments.of(Collections.emptyList())
        );
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
