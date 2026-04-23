package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.passport.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.reporder.service.RepOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    private RepOrderService repOrderService;

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
    void givenOver18AndPartner_whenPopulatePartnerIsInvoked_thenPartnerIsFoundAndMapped(){
        PassportAssessmentEntity entity = new PassportAssessmentEntity();
        Applicant partner = TestEntityDataBuilder.getApplicant(APPLICANT_ID);

        when(applicantService.find(APPLICANT_ID)).thenReturn(partner);

        ApiCreatePassportedAssessmentRequest request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true );
        passportAssessmentService.populatePartnerDetails(request, entity);

        verify(applicantService).find(any());
        validatePartnerDetails(partner, entity);
    }

    @Test
    void givenNoRepOrderId_whenCreateisInvoked_thenShouldError(){
        ApiCreatePassportedAssessmentRequest request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(null, APPLICANT_ID, false, true );

        when(repOrderService.exists(null)).thenReturn(false);
        assertThrows(CrimeValidationException.class,()->passportAssessmentService.create(request));
    }

    @Test
    void givenRepOrderId_whenCreateIsInvoked_thenShouldSucceed(){
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true );
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        var partner = TestEntityDataBuilder.getApplicant(APPLICANT_ID);
        var response = TestModelDataBuilder.buildValidCreatePassportedAssessmentResponse();

        when(repOrderService.exists(REP_ID)).thenReturn(true);
        when(passportAssessmentMapper.toPassportAssessmentEntity(request)).thenReturn(entity);
        when(passportAssessmentPersistenceService.save(entity)).thenReturn(entity);
        when(applicantService.find(APPLICANT_ID)).thenReturn(partner);
        when(passportAssessmentMapper.toApiCreatePassportedAssessmentResponse(entity)).thenReturn(response);

        passportAssessmentService.create(request);

        verify(repOrderService).exists(REP_ID);
        verify(passportAssessmentMapper).toPassportAssessmentEntity(request);
        verify(passportAssessmentPersistenceService).save(entity);
        verify(assessmentReplacementService).replacePreviousAssessments(any(PassportAssessmentEntity.class));
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
    private static Stream<Arguments> validNoPartnerPopulationConditions() {
        return Stream.of(
                Arguments.of(true, true, APPLICANT_ID ),
                Arguments.of(true, true, null ),
                Arguments.of(true, false, null ),
                Arguments.of(false, true, null ),
                Arguments.of(false, false, null )
        );
    }

    @MethodSource(value= "validNoPartnerPopulationConditions")
    @ParameterizedTest
    void givenConditionsShouldNotPopulatePartner_whenPopulatePartnerIsInvoked_thenPartnerIsNotMapped(boolean isUnder18, boolean hasDeclaredBenefit, Integer partnerId){
        PassportAssessmentEntity entity = new PassportAssessmentEntity();

        ApiCreatePassportedAssessmentRequest request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, partnerId, isUnder18, hasDeclaredBenefit );
        passportAssessmentService.populatePartnerDetails(request, entity);

        verify(applicantService, never()).find(any());
        validatePartnerDetails(null, entity);
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
