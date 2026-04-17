package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
ApiGetPassportEvidenceResponse toApiGetPassportEvidenceResponse(
        PassportAssessmentEntity passportAssessmentEntity);
    
    default ApiIncomeEvidence toApiIncomeEvidence(PassportAssessmentEvidenceEntity evidenceEntity) {
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
