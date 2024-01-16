package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.dto.IOJAssessorDetails;
import gov.uk.courtdata.util.NameUtils;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Objects;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface RepOrderMapper {

    RepOrderDTO repOrderEntityToRepOrderDTO(final RepOrderEntity repOrder);

    void createRepOrderToRepOrderEntity(CreateRepOrder createRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    void updateRepOrderToRepOrderEntity(UpdateRepOrder updateRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    default IOJAssessorDetails createIOJAssessorDetails(RepOrderEntity repOrder) {
        UserEntity userEntity = repOrder.getUserCreatedEntity();

        String fullName = null;
        if (Objects.nonNull(userEntity)) {
            fullName = NameUtils.toCapitalisedFullName(userEntity.getFirstName(), userEntity.getSurname());
        }

        return IOJAssessorDetails.builder()
                .fullName(fullName)
                .userName(repOrder.getUserCreated())
                .build();
    }
}

