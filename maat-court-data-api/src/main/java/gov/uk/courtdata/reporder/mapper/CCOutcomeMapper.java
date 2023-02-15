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

    List<RepOrderCCOutcomeDTO> RepOrderCCOutComeEntityToRepOrderCCOutcomeDTO(List<RepOrderCCOutComeEntity> repOrderCCOutComeEntities);

    RepOrderCCOutcomeDTO RepOrderCCOutComeEntityToRepOrderCCOutcomeDTO(RepOrderCCOutComeEntity repOrderCCOutComeEntities);

    RepOrderCCOutComeEntity RepOrderCCOutcomeToRepOrderCCOutcomeEntity(RepOrderCCOutcome outCome);

    void RepOrderCCOutComeToRepOrderCCOutcomeEntity(RepOrderCCOutcome updateRepOrder, @MappingTarget RepOrderCCOutComeEntity repOrderEntity);

}
