package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.service.PartnerResolver;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import gov.uk.courtdata.exception.RecordEmptyException;
import gov.uk.courtdata.passport.mapper.PassportAssessmentEvidenceMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.common.model.evidence.ApiIncomeEvidence;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

@ExtendWith(MockitoExtension.class)
class PassportAssessmentEvidenceServiceTest {
    
    private static final Integer APPLICANT_ID = 111111;
    private static final Integer PARTNER_ID = 222222;
    private PassportAssessmentEntity passportAssessmentEntity;
    private PassportAssessmentEvidenceEntity applicantEvidenceEntity;
    private PassportAssessmentEvidenceEntity partnerEvidenceEntity;

    @InjectMocks
    private PassportAssessmentEvidenceService passportAssessmentEvidenceService;

    @Mock
    private PassportAssessmentPersistenceService passportAssessmentPersistenceService;

    @Mock
    private PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper;
    
    @Mock
    private PartnerResolver partnerResolver;

    @Test
    void givenValidPassportAssessmentId_whenFindIsInvoked_thenEvidenceIsSeparatedByApplicantAndPartnerAndReturned() {
        buildEntities();
        
        ApiGetPassportEvidenceResponse apiGetPassportEvidenceResponse = TestModelDataBuilder.getApiGetPassportedEvidenceResponse();
        ApiIncomeEvidence applicantApiGetIncomeEvidenceResponse = new ApiIncomeEvidence()
            .withId(1)
            .withDescription("Applicant evidence")
            .withEvidenceType(IncomeEvidenceType.NINO)
            .withDateReceived(LocalDate.now().minusDays(2))
            .withMandatory(true);

        ApiIncomeEvidence partnerApiGetIncomeEvidenceResponse = new ApiIncomeEvidence()
            .withId(2)
            .withDescription("Partner evidence")
            .withEvidenceType(IncomeEvidenceType.OTHER_ADHOC)
            .withDateReceived(LocalDate.now().minusDays(1))
            .withMandatory(true);
        
        when(passportAssessmentPersistenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenReturn(passportAssessmentEntity);
        when(partnerResolver.getPartnerLegacyId(passportAssessmentEntity.getRepOrder().getId())).thenReturn(PARTNER_ID);
        when(passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(passportAssessmentEntity)).thenReturn(apiGetPassportEvidenceResponse);
        when(passportAssessmentEvidenceMapper.toApiIncomeEvidence(applicantEvidenceEntity)).thenReturn(applicantApiGetIncomeEvidenceResponse);
        when(passportAssessmentEvidenceMapper.toApiIncomeEvidence(partnerEvidenceEntity)).thenReturn(partnerApiGetIncomeEvidenceResponse);
        
        ApiGetPassportEvidenceResponse response = passportAssessmentEvidenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID);

        assertThat(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata())
            .usingRecursiveComparison()
            .isEqualTo(response.getPassportEvidenceMetadata());

        assertThat(apiGetPassportEvidenceResponse.getApplicantEvidenceItems())
            .usingRecursiveComparison()
            .isEqualTo(response.getApplicantEvidenceItems());

        assertThat(apiGetPassportEvidenceResponse.getPartnerEvidenceItems())
            .usingRecursiveComparison()
            .isEqualTo(response.getPartnerEvidenceItems());
    }

    @Test
    void givenValidPassportAssessmentIdWithNoPartnerEvidence_whenFindIsInvoked_thenApplicantEvidenceIsReturned() {
        buildEntities();
        passportAssessmentEntity.getPassportAssessmentEvidences().removeLast();

        ApiGetPassportEvidenceResponse apiGetPassportEvidenceResponse = TestModelDataBuilder.getApiGetPassportedEvidenceResponse();
        ApiIncomeEvidence applicantApiGetIncomeEvidenceResponse = new ApiIncomeEvidence()
            .withId(1)
            .withDescription("Applicant evidence")
            .withEvidenceType(IncomeEvidenceType.NINO)
            .withDateReceived(LocalDate.now().minusDays(2))
            .withMandatory(true);

        when(passportAssessmentPersistenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenReturn(passportAssessmentEntity);
        when(partnerResolver.getPartnerLegacyId(passportAssessmentEntity.getRepOrder().getId())).thenReturn(PARTNER_ID);
        when(passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(passportAssessmentEntity)).thenReturn(apiGetPassportEvidenceResponse);
        when(passportAssessmentEvidenceMapper.toApiIncomeEvidence(applicantEvidenceEntity)).thenReturn(applicantApiGetIncomeEvidenceResponse);

        ApiGetPassportEvidenceResponse response = passportAssessmentEvidenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID);

        assertThat(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata())
            .usingRecursiveComparison()
            .isEqualTo(response.getPassportEvidenceMetadata());

        assertThat(apiGetPassportEvidenceResponse.getApplicantEvidenceItems())
            .usingRecursiveComparison()
            .isEqualTo(response.getApplicantEvidenceItems());

        assertThat(apiGetPassportEvidenceResponse.getPartnerEvidenceItems())
            .isEmpty();
    }

    @Test
    void givenNoApplicantIdOnEvidence_whenFindIsInvoked_thenExceptionIsThrown() {
        buildEntities();
        applicantEvidenceEntity.getApplicant().setId(null);

        when(passportAssessmentPersistenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenReturn(passportAssessmentEntity);
        when(partnerResolver.getPartnerLegacyId(passportAssessmentEntity.getRepOrder().getId())).thenReturn(PARTNER_ID);

        assertThatThrownBy(() -> passportAssessmentEvidenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).isInstanceOf(
                RecordEmptyException.class)
            .hasMessageContaining("Passport assessment evidence is missing applicant reference");
    }
    
    private void buildEntities() {
        passportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        Applicant applicant = Applicant.builder().id(APPLICANT_ID).build();
        Applicant partner = Applicant.builder().id(PARTNER_ID).build();
        passportAssessmentEntity.getRepOrder().setApplicationId(applicant.getId());
        
        applicantEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(
                passportAssessmentEntity, applicant, LocalDateTime.now());

        partnerEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(
                passportAssessmentEntity, partner, LocalDateTime.now());

        passportAssessmentEntity.addPassportAssessmentEvidences(applicantEvidenceEntity);
        passportAssessmentEntity.addPassportAssessmentEvidences(partnerEvidenceEntity);
    }
}
