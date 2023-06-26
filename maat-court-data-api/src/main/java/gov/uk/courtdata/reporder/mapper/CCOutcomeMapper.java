package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.RepOrderCCOutcomeDTO;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface CCOutcomeMapper {

    List<RepOrderCCOutcomeDTO> repOrderCCOutComeEntityToRepOrderCCOutcomeDTO(List<RepOrderCCOutComeEntity> repOrderCCOutComeEntities);

    RepOrderCCOutcomeDTO repOrderCCOutComeEntityToRepOrderCCOutcomeDTO(RepOrderCCOutComeEntity repOrderCCOutComeEntities);

    RepOrderCCOutComeEntity repOrderCCOutcomeToRepOrderCCOutcomeEntity(RepOrderCCOutcome outCome);

    void repOrderCCOutComeMappedToRepOrderCCOutcomeEntity(RepOrderCCOutcome updateRepOrder, @MappingTarget RepOrderCCOutComeEntity repOrderEntity);

}
