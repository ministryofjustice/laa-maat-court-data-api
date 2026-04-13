package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.applicant.service.ApplicantService;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import java.util.List;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;

import static uk.gov.justice.laa.crime.enums.BenefitRecipient.PARTNER;

@Component
@RequiredArgsConstructor
public class PassportAssessmentMapperHelper {
    
    private final RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;
    private final RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;
    private final RepOrderService repOrderService;
    private final ApplicantService applicantService;

    @Named("mapRepOrder")
    public RepOrderEntity mapRepOrder(Integer repOrderId) {
        if(repOrderId == null){
            return null;
        }
        return repOrderService.findByRepId(repOrderId);
    }

    @Named("declaredBenefitMapper")
    public DeclaredBenefit mapDeclaredBenefit(PassportAssessmentEntity passportAssessmentEntity) {
        DeclaredBenefit declaredBenefit = new DeclaredBenefit();

        declaredBenefit.setBenefitType(mapBenefitType(passportAssessmentEntity));
        declaredBenefit.setLastSignOnDate(passportAssessmentEntity.getLastSignOnDate());
        declaredBenefit.setBenefitRecipient(mapBenefitRecipient(passportAssessmentEntity));
        declaredBenefit.setLegacyPartnerId(mapPartnerLegacyId(passportAssessmentEntity));

        return declaredBenefit;
    }

    BenefitType mapBenefitType(PassportAssessmentEntity passportAssessmentEntity) {
        if (passportAssessmentEntity.getIncomeSupport() != null
            && passportAssessmentEntity.getIncomeSupport().equals("Y")) {
            return BenefitType.INCOME_SUPPORT;
        } else if (passportAssessmentEntity.getJobSeekers() != null
            && passportAssessmentEntity.getJobSeekers().equals("Y")) {
            return BenefitType.JSA;
        } else if (passportAssessmentEntity.getEsa() != null
            && passportAssessmentEntity.getEsa().equals("Y")) {
            return BenefitType.ESA;
        } else if (passportAssessmentEntity.getStatePensionCredit() != null
            && passportAssessmentEntity.getStatePensionCredit().equals("Y")) {
            return BenefitType.GSPC;
        } else if (passportAssessmentEntity.getUniversalCredit() != null &&
            passportAssessmentEntity.getUniversalCredit().equals("Y")) {
            return BenefitType.UC;
        }

        return null;
    }
    
    BenefitRecipient mapBenefitRecipient(PassportAssessmentEntity passportAssessmentEntity) {
        return passportAssessmentEntity.getPartnerBenefitClaimed() != null
            && passportAssessmentEntity.getPartnerBenefitClaimed().equals("Y")
            ? PARTNER : BenefitRecipient.APPLICANT;
    }
    
     Integer mapPartnerLegacyId(PassportAssessmentEntity passportAssessmentEntity) {
        List<RepOrderApplicantLinksDTO> applicantLinks = findPartner(passportAssessmentEntity);

        if (applicantLinks == null) {
            return null;
        }
        
        return applicantLinks.stream()
            .filter(repOrderApplicantLink ->
                repOrderApplicantLink.getUnlinkDate() == null &&
                repOrderApplicantLink.getLinkDate() != null)
            .map(RepOrderApplicantLinksDTO::getPartnerApplId)
            .findFirst()
            .orElse(null);
    }

    private List<RepOrderApplicantLinksDTO> findPartner(PassportAssessmentEntity passportAssessmentEntity) {
        List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntities = 
            repOrderApplicantLinksRepository.findAllByRepId(passportAssessmentEntity.getRepOrder().getId());
        
        if (repOrderApplicantLinksEntities.isEmpty()) {
            return null;
        }

        return repOrderApplicantLinksMapper.
            mapEntityToDTO(repOrderApplicantLinksEntities);
    }

    protected Applicant getPartnerEntity(Integer id){
        try {
            return applicantService.find(id);
        } catch (RequestedObjectNotFoundException e){
            return null;
        }
    }

}
