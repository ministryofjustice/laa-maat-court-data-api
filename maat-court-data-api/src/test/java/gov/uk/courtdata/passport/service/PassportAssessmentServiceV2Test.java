package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.service.ApplicantService;
import gov.uk.courtdata.assessment.service.AssessmentReplacementService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.passport.validator.CreatePassportAssessmentV2Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class PassportAssessmentServiceV2Test {

    @Mock
    private AssessmentReplacementService assessmentReplacementService;

    @Mock
    private PassportAssessmentPersistenceService passportAssessmentPersistenceService;

    @Mock
    private PassportAssessmentMapper passportAssessmentMapper;

    @Mock
    private ApplicantService applicantService;

    @Mock
    private CreatePassportAssessmentV2Validator  createPassportAssessmentV2Validator;

    @Captor
    ArgumentCaptor<PassportAssessmentEntity> passportCaptor;

    @InjectMocks
    private PassportAssessmentServiceV2 passportAssessmentService;

    @Test
    void whenFindByLegacyIdIsInvoked_thenPassportedAssessmentIsRetrieved() {
        var apiGetPassportedAssessmentResponse = TestModelDataBuilder.getApiGetPassportedAssessmentResponse();
        when(passportAssessmentPersistenceService.find(any())).thenReturn(TestEntityDataBuilder.getPassportAssessmentEntity());
        when(passportAssessmentMapper.toApiGetPassportedAssessmentResponse(any(
            PassportAssessmentEntity.class))).thenReturn(apiGetPassportedAssessmentResponse);
        var returnedPassportedAssessment = passportAssessmentService.find(LEGACY_PASSPORT_ASSESSMENT_ID);
        assertEquals(LEGACY_PASSPORT_ASSESSMENT_ID, returnedPassportedAssessment.getLegacyAssessmentId());
    }

    @Test
    void givenRequest_whenCreateIsInvoked_thenShouldSucceed(){
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true );
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        var partner = TestEntityDataBuilder.getApplicant(APPLICANT_ID);
        var response = TestModelDataBuilder.buildValidCreatePassportedAssessmentResponse();

        when(passportAssessmentMapper.toPassportAssessmentEntity(request)).thenReturn(entity);
        when(passportAssessmentPersistenceService.save(entity)).thenReturn(entity);
        when(applicantService.find(APPLICANT_ID)).thenReturn(partner);
        when(passportAssessmentMapper.toApiCreatePassportedAssessmentResponse(entity)).thenReturn(response);

        passportAssessmentService.create(request);

        verify(createPassportAssessmentV2Validator).validateCreateRequest(request);
        verify(passportAssessmentMapper).toPassportAssessmentEntity(request);
        verify(passportAssessmentPersistenceService).save(entity);
        verify(assessmentReplacementService).replacePreviousAssessments(entity);
        verify(applicantService).find(APPLICANT_ID);
        verify(passportAssessmentMapper).toApiCreatePassportedAssessmentResponse(entity);

        validatePartnerDetails(partner, entity);
    }

    /**
     * Representing isUnder18, declaredBenefit is present, and the ID of the partner.
     * Will not populate if isUnder18=true or declaredBenefit=false or has no partner id.
     * Will populate if isUnder18=false, declaredBenefit=true and has a partner id.
     * any, false, APPLICANT_ID is invalid, as partnerId exists on the declaredBenefit.
     */
    private static Stream<Arguments> partnerPopulationConditionsThatShouldNotMap() {
        return Stream.of(
                Arguments.of(true, true, APPLICANT_ID ), // isUnder18 should fail.
                Arguments.of(true, true, null ),         // isUnder18 should fail.
                Arguments.of(true, false, null ),        // isUnder18 should fail.
                Arguments.of(false, true, null ),        // no PartnerId.
                Arguments.of(false, false, null )        // no declaredBenefit.
        );
    }

    @MethodSource(value= "partnerPopulationConditionsThatShouldNotMap")
    @ParameterizedTest
    void givenConditionsShouldNotPopulatePartner_whenCreateIsInvoked_thenPartnerIsNotMapped(boolean isUnder18, boolean hasDeclaredBenefit, Integer partnerId){
        ApiCreatePassportedAssessmentRequest request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, partnerId, isUnder18, hasDeclaredBenefit );
        runCreateForPartnerValidation(request, null);
        verify(applicantService, never()).find(any());
    }

    @Test
    void givenPartnerId_whenCreateIsInvoked_thenPartnerIsMapped(){
        ApiCreatePassportedAssessmentRequest request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true );
        var partner = TestEntityDataBuilder.getApplicant(APPLICANT_ID);
        when(applicantService.find(APPLICANT_ID)).thenReturn(partner);

        runCreateForPartnerValidation(request, partner);

        verify(applicantService).find(any());
    }

    private void runCreateForPartnerValidation(ApiCreatePassportedAssessmentRequest request, Applicant expectedPartner){
        PassportAssessmentEntity entity = new PassportAssessmentEntity();

        var response = TestModelDataBuilder.buildValidCreatePassportedAssessmentResponse();

        when(passportAssessmentMapper.toPassportAssessmentEntity(request)).thenReturn(entity);
        when(passportAssessmentPersistenceService.save(passportCaptor.capture())).thenReturn(entity);
        when(passportAssessmentMapper.toApiCreatePassportedAssessmentResponse(any())).thenReturn(response);

        passportAssessmentService.create(request);

        validatePartnerDetails(expectedPartner, passportCaptor.getValue());
    }

    private void validatePartnerDetails(Applicant expectedPartner, PassportAssessmentEntity entity){
        if (expectedPartner != null){
            assertThat(entity.getPartnerDob().toLocalDate()).isEqualTo(expectedPartner.getDob());
            assertThat(entity.getPartnerSurname()).isEqualTo(expectedPartner.getLastName());
            assertThat(entity.getPartnerFirstName()).isEqualTo(expectedPartner.getFirstName());
            assertThat(entity.getPartnerNiNumber()).isEqualTo(expectedPartner.getNiNumber());
            assertThat(entity.getPartnerOtherNames()).isEqualTo(expectedPartner.getOtherNames());
        } else{
            assertThat(entity.getPartnerDob()).isNull();
            assertThat(entity.getPartnerSurname()).isNull();
            assertThat(entity.getPartnerFirstName()).isNull();
            assertThat(entity.getPartnerNiNumber()).isNull();
            assertThat(entity.getPartnerOtherNames()).isNull();
        }

    }
}
