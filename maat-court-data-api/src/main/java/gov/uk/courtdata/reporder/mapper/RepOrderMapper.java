package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.AtisRepOrderDTO;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.util.UserEntityUtils;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface RepOrderMapper {

    RepOrderDTO repOrderEntityToRepOrderDTO(final RepOrderEntity repOrder);

    void createRepOrderToRepOrderEntity(CreateRepOrder createRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    void updateRepOrderToRepOrderEntity(UpdateRepOrder updateRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    default AssessorDetails createIOJAssessorDetails(RepOrderEntity repOrder) {
        String fullName = UserEntityUtils.extractFullName(repOrder.getUserCreatedEntity());

        return AssessorDetails.builder()
                .fullName(fullName)
                .userName(repOrder.getUserCreated())
                .build();
    }

    default AtisRepOrderDTO mapAtisRepOrder(RepOrderEntity repOrderEntity) {
        if (repOrderEntity == null) {
            return null;
        }

        return AtisRepOrderDTO.builder()
                .usn(repOrderEntity.getUsn())
                .maatRef(repOrderEntity.getId())
                .caseId(repOrderEntity.getCaseId())
                // TODO
                .caseType(null)
                .iojResult(null)
                .iojAssesorName(null)
                .dateAppCreated(null)
                .iojReason(null)
                .meansInitResult(null)
                .meansInitStatus(null)
                .meansFullResult(null)
                .meansFullStatus(null)
                .meansAssessorName(null)
                .dateMeansCreated(null)
                .passportResult(null)
                .passportStatus(null)
                .passportAssessorName(null)
                .datePassportCreated(null)
                .fundingDecision(null)
                .build();
    }
}

