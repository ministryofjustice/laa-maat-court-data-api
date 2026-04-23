package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.common.model.evidence.ApiIncomeEvidence;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = PassportAssessmentMapperHelper.class)
public interface PassportAssessmentEvidenceMapper {
    
    @Mapping(target = "passportEvidenceMetadata.evidenceDueDate", source = "passportEvidenceDueDate")
    @Mapping(target = "passportEvidenceMetadata.evidenceReceivedDate", source = "allPassportEvidenceReceivedDate")
    @Mapping(target = "passportEvidenceMetadata.upliftAppliedDate", source = "passportUpliftApplyDate")
    @Mapping(target = "passportEvidenceMetadata.upliftRemovedDate", source = "passportUpliftRemoveDate")
    @Mapping(target = "passportEvidenceMetadata.firstReminderDate", source = "firstPassportReminderDate")
    @Mapping(target = "passportEvidenceMetadata.secondReminderDate", source = "secondPassportReminderDate")
    @Mapping(target = "passportEvidenceMetadata.incomeEvidenceNotes", source = "passportEvidenceNotes")
    ApiGetPassportEvidenceResponse toApiGetPassportEvidenceResponse(
        PassportAssessmentEntity passportAssessmentEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateReceived", source = "dateReceived", qualifiedByName = "mapEvidenceDateReceived")
    @Mapping(target = "description", source = "otherText")
    @Mapping(target = "mandatory", source = "mandatory", qualifiedByName = "mapEvidenceMandatory")
    @Mapping(target = "evidenceType", source = "incomeEvidence", qualifiedByName = "mapEvidenceType")
    ApiIncomeEvidence toApiIncomeEvidence(PassportAssessmentEvidenceEntity evidenceEntity);
}
