package gov.uk.courtdata.ccoutcome.mapper;

import gov.uk.courtdata.dto.RepOrderCCOutComeDTO;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface CCOutComeMapper {

    List<RepOrderCCOutComeDTO> RepOrderCCOutComeEntityToRepOrderCCOutComeDTO(List<RepOrderCCOutComeEntity> repOrderCCOutComeEntities);

    RepOrderCCOutComeDTO RepOrderCCOutComeEntityToRepOrderCCOutComeDTO(RepOrderCCOutComeEntity repOrderCCOutComeEntities);

    RepOrderCCOutComeEntity RepOrderCCOutComeToRepOrderCCOutComeEntity(RepOrderCCOutCome outCome);


    void RepOrderCCOutComeToRepOrderCCOutComeEntity(RepOrderCCOutCome updateRepOrder, @MappingTarget RepOrderCCOutComeEntity repOrderEntity);

}
