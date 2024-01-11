package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.reporder.dto.AssessorDetails;
import gov.uk.courtdata.reporder.projection.RepOrderCreatorDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, builder = @Builder(disableBuilder = true))
public interface RepOrderMapper {

    RepOrderDTO repOrderEntityToRepOrderDTO(final RepOrderEntity repOrder);

    void createRepOrderToRepOrderEntity(CreateRepOrder createRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    void updateRepOrderToRepOrderEntity(UpdateRepOrder updateRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    AssessorDetails repOrderCreatorDetailsToAssessorDetails(RepOrderCreatorDetails repOrderCreator);
}
