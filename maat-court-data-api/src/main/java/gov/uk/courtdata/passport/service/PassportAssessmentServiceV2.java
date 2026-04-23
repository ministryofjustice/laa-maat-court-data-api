package gov.uk.courtdata.passport.service;

import gov.uk.courtdata.applicant.service.ApplicantService;
import gov.uk.courtdata.assessment.service.AssessmentReplacementService;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.passport.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.reporder.service.RepOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentResponse;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.common.model.passported.PassportedAssessment;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentServiceV2 {

    private final PassportAssessmentPersistenceService passportAssessmentPersistenceService;
    private final PassportAssessmentMapper passportAssessmentMapper;

    private final RepOrderService repOrderService;
    private final AssessmentReplacementService assessmentReplacementService;
    private final ApplicantService applicantService;

    
    @Transactional(readOnly = true)
    public ApiGetPassportedAssessmentResponse find(int passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentPersistenceService.find(passportAssessmentId);

        return passportAssessmentMapper.toApiGetPassportedAssessmentResponse(passportAssessmentEntity);
    }

    @Transactional
    public ApiCreatePassportedAssessmentResponse create(ApiCreatePassportedAssessmentRequest request) {
        verifyRepOrderPresent(request);
        PassportAssessmentEntity entity = passportAssessmentMapper.toPassportAssessmentEntity(request);
        populatePartnerDetails(request, entity);

        entity = passportAssessmentPersistenceService.save(entity);
        assessmentReplacementService.replacePreviousAssessments(entity);

        return passportAssessmentMapper.toApiCreatePassportedAssessmentResponse(entity);
    }

    /***
     * Populates the partner fields on the PassportAssessmentEntity if request passed 3 conditions<ul>
     *     <li>is over 18</li>
     *     <li>has a declared benefit</li>
     *     <li>has a partner id</li>
     * </ul>
     * Otherwise will leave fields unpopulated.
     * @param request Create request sent that is being mapped and saved.
     * @param entity PassportAssessmentEntity that will have fields populated.
     */
    private void populatePartnerDetails(ApiCreatePassportedAssessmentRequest request, PassportAssessmentEntity entity){
        PassportedAssessment assessment = request.getPassportedAssessment();
        DeclaredBenefit declaredBenefit = assessment.getDeclaredBenefit();
        if(Boolean.FALSE.equals(assessment.getDeclaredUnder18())
                && declaredBenefit != null
                && declaredBenefit.getLegacyPartnerId() != null){
            Applicant partner = applicantService.find(declaredBenefit.getLegacyPartnerId());
            entity.setPartnerDob(partner.getDob().atStartOfDay());
            entity.setPartnerFirstName(partner.getFirstName());
            entity.setPartnerSurname(partner.getLastName());
            entity.setPartnerOtherNames(partner.getOtherNames());
            entity.setPartnerNiNumber(partner.getNiNumber());
        }
    }

    private void verifyRepOrderPresent(ApiCreatePassportedAssessmentRequest request){
        Integer repOrderId = request.getPassportedAssessmentMetadata().getLegacyApplicationId();
        boolean repOrderExists = repOrderService.exists(repOrderId);

        if(!repOrderExists){
            log.error("RepOrderId {} specified on Passported Assessment Create Request does not exist", repOrderId);
            throw new CrimeValidationException(List.of(new ErrorMessage("legacyApplicationId","RepOrder does not exist")));
        }
    }
}
