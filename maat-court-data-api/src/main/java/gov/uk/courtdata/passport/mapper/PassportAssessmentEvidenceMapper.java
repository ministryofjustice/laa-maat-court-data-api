package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import gov.uk.courtdata.exception.ValidationException;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.common.model.evidence.ApiIncomeEvidence;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = PassportAssessmentMapperHelper.class)
public interface PassportAssessmentEvidenceMapper {
    
    @Mapping(target = "passportEvidenceMetadata.evidenceDueDate", source = "passportAssessmentEntity.passportEvidenceDueDate")
    @Mapping(target = "passportEvidenceMetadata.evidenceReceivedDate", source = "passportAssessmentEntity.allPassportEvidenceReceivedDate")
    @Mapping(target = "passportEvidenceMetadata.upliftAppliedDate", source = "passportAssessmentEntity.passportUpliftApplyDate")
    @Mapping(target = "passportEvidenceMetadata.upliftRemovedDate", source = "passportAssessmentEntity.passportUpliftRemoveDate")
    @Mapping(target = "passportEvidenceMetadata.firstReminderDate", source = "passportAssessmentEntity.firstPassportReminderDate")
    @Mapping(target = "passportEvidenceMetadata.secondReminderDate", source = "passportAssessmentEntity.secondPassportReminderDate")
    @Mapping(target = "passportEvidenceMetadata.incomeEvidenceNotes", source = "passportAssessmentEntity.passportEvidenceNotes")
    @Mapping(target = "applicantEvidenceItems", ignore = true)
    @Mapping(target = "partnerEvidenceItems", ignore = true)
ApiGetPassportEvidenceResponse toApiGetPassportEvidenceResponse(
        PassportAssessmentEntity passportAssessmentEntity, @Context Integer partnerLegacyId);
    
    @AfterMapping
    default void mapEvidence(PassportAssessmentEntity passportAssessmentEntity, 
        @Context Integer partnerId, 
        @MappingTarget ApiGetPassportEvidenceResponse apiGetPassportEvidenceResponse) {

        List<PassportAssessmentEvidenceEntity> evidence = passportAssessmentEntity.getPassportAssessmentEvidences();
        List<ApiIncomeEvidence> applicantEvidence = apiGetPassportEvidenceResponse.getApplicantEvidenceItems();
        List<ApiIncomeEvidence> partnerEvidence = apiGetPassportEvidenceResponse.getPartnerEvidenceItems();

        for (PassportAssessmentEvidenceEntity evidenceEntity : evidence) {
            if (isPartnerEvidence(evidenceEntity, partnerId)) {
                ApiIncomeEvidence incomeEvidence = buildIncomeEvidenceItem(evidenceEntity);
                partnerEvidence.add(incomeEvidence);
            } else if (isApplicantEvidence(evidenceEntity, passportAssessmentEntity.getRepOrder().getApplicationId())){
                ApiIncomeEvidence incomeEvidence = buildIncomeEvidenceItem(evidenceEntity);
                applicantEvidence.add(incomeEvidence);
            } else {
                throw new ValidationException("Partner ID or Applicant ID does not match ID on evidence entity");
            }
        }
    }
    
    private boolean isPartnerEvidence(PassportAssessmentEvidenceEntity evidenceEntity, Integer partnerId) {
        return evidenceEntity.getApplicant().getId().equals(null) || evidenceEntity.getApplicant().getId().equals(partnerId);
    }
    
    private boolean isApplicantEvidence(PassportAssessmentEvidenceEntity evidenceEntity, Integer applicationId) {
        return evidenceEntity.getApplicant().getId().equals(applicationId);
    }
    
    private ApiIncomeEvidence buildIncomeEvidenceItem(PassportAssessmentEvidenceEntity evidenceEntity) {
        ApiIncomeEvidence incomeEvidence = new ApiIncomeEvidence();
        
        incomeEvidence.setId(evidenceEntity.getId());
        if (evidenceEntity.getDateReceived() != null) {
            incomeEvidence.setDateReceived(evidenceEntity.getDateReceived().toLocalDate());
        }
        incomeEvidence.setDescription(evidenceEntity.getOtherText());
        incomeEvidence.setMandatory(evidenceEntity.getMandatory() != null && evidenceEntity.getMandatory().equals("Y"));
        incomeEvidence.setEvidenceType(IncomeEvidenceType.getFrom(evidenceEntity.getIncomeEvidence()));
        
        return incomeEvidence;
    }
}
