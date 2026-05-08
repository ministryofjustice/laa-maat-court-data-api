package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.RepOrderCCOutcomeDTO;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.model.RepOrderCCOutcome;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface CCOutcomeMapper {

    List<RepOrderCCOutcomeDTO> repOrderCCOutComeEntityToRepOrderCCOutcomeDTO(
            List<RepOrderCCOutComeEntity> repOrderCCOutComeEntities);

    @Mapping(target = "repId", source = "repOrder.id")
    RepOrderCCOutcomeDTO repOrderCCOutComeEntityToRepOrderCCOutcomeDTO(
            RepOrderCCOutComeEntity repOrderCCOutComeEntities);

    @Mapping(target = "repOrder.id", source = "repId")
    RepOrderCCOutComeEntity repOrderCCOutcomeToRepOrderCCOutcomeEntity(RepOrderCCOutcome outCome);

    void repOrderCCOutComeMappedToRepOrderCCOutcomeEntity(
            RepOrderCCOutcome updateRepOrder, @MappingTarget RepOrderCCOutComeEntity repOrderEntity);
}
